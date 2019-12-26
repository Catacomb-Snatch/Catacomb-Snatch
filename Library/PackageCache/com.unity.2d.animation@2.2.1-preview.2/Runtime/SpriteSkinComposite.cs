using System;
using Unity.Mathematics;
using System.Collections.Generic;
using Unity.Collections;
using Unity.Jobs;
using Unity.Collections.LowLevel.Unsafe;
using UnityEngine.Jobs;
using UnityEngine.Experimental.U2D.Common;
using UnityEngine.Profiling;
#if ENABLE_ANIMATION_BURST
using Unity.Burst;
#endif

namespace UnityEngine.Experimental.U2D.Animation
{
    internal struct SpriteSkinBatchData
    {
        public Sprite sprite;
        public Matrix4x4 worldToLocalMatrix;
        public Transform[] boneTransform;
    }

    internal struct ExtractBoneTransformJobData
    {
        public List<NativeArray<float4x4>> boneTransformOut;
        public List<Transform> transformAccessArray;
    }

    internal struct PerSkinJobData
    {
        public int2 bindPosesIndex;
        public int2 verticesIndex;
    }

    internal struct NatliveSliceIntPtr<T> where T : struct
    {
        [NativeDisableUnsafePtrRestriction]
        public IntPtr data;
        public int length;
        public int stride;
#if ENABLE_UNITY_COLLECTIONS_CHECKS
        public AtomicSafetyHandle safetyHandle;
#endif

        public unsafe NatliveSliceIntPtr(NativeSlice<T> nativeSlice)
        {
#if ENABLE_UNITY_COLLECTIONS_CHECKS
            safetyHandle = NativeSliceUnsafeUtility.GetAtomicSafetyHandle(nativeSlice);
#endif
            data = new IntPtr(nativeSlice.GetUnsafeReadOnlyPtr());
            length = nativeSlice.Length;
            stride = nativeSlice.Stride;
        }

        public unsafe NativeSlice<T> ToNativeSlice()
        {
            var p = NativeSliceUnsafeUtility.ConvertExistingDataToNativeSlice<T>(data.ToPointer(), stride, length);
#if ENABLE_UNITY_COLLECTIONS_CHECKS
            NativeSliceUnsafeUtility.SetAtomicSafetyHandle(ref p,safetyHandle);
#endif
            return p;
        }

        public int Length { get { return length; } }
    }

    internal struct SpriteSkinData
    {
        public float4x4 rootInv;
        public NatliveSliceIntPtr<Vector3> vertices;
        public NatliveSliceIntPtr<BoneWeight> boneWeights;
        public NatliveSliceIntPtr<Matrix4x4> bindPoses;
        public NatliveSliceIntPtr<float4x4> boneTransforms;
        public NatliveSliceIntPtr<Vector4> tangents;
        public bool hasTangents;
        public int spriteVertexStreamSize;
        public int spriteVertexCount;
        public int tangentVertexOffset;
        public int spriteSkinIndex;
        public int deformVerticesStartPos;
    }

#if ENABLE_ANIMATION_BURST
    [BurstCompile]
#endif 
    internal struct ExtractBoneTransformJob : IJobParallelForTransform
    {
        [NativeDisableContainerSafetyRestriction]
        [NativeDisableUnsafePtrRestriction]
        public NativeArray<IntPtr>  boneTransformOutput;
        [ReadOnly]
        public NativeArray<int> nativeArraySize;
#if ENABLE_UNITY_COLLECTIONS_CHECKS
        [ReadOnly]
        public NativeArray<AtomicSafetyHandle> nativeArrayNativeSafetyHandles;
#endif
        public unsafe void Execute(int index, TransformAccess transform)
        {
            float4x4 rotationTranslationMatrix = new float4x4(transform.rotation, transform.position);
            float4x4 scaleMatrix = float4x4.Scale(transform.localScale);
            float4x4 matrix = math.mul(rotationTranslationMatrix, scaleMatrix);
            //matrix = float4x4.TRS(transform.position, transform.rotation, new float3(1,1,1));
            //var b = boneTransformOutput.ToNativeSlice();
            for (int i = 0; i < boneTransformOutput.Length; ++i)
            {
                if (index < nativeArraySize[i])
                {
                    var array = NativeSliceUnsafeUtility.ConvertExistingDataToNativeSlice<float4x4>(boneTransformOutput[i].ToPointer(), sizeof(float4x4), nativeArraySize[i]);
#if ENABLE_UNITY_COLLECTIONS_CHECKS
                    NativeSliceUnsafeUtility.SetAtomicSafetyHandle(ref array, nativeArrayNativeSafetyHandles[i]);
#endif
                    array[index] = matrix;
                    break;
                }
                index -= nativeArraySize[i];
            }
        }
    }

#if ENABLE_ANIMATION_BURST
    [BurstCompile]
#endif 
    internal struct PrepareDeformJob : IJob
    {
        [ReadOnly]
        public NativeArray<SpriteSkinData> spriteSkinData;
        [ReadOnly]
        public NativeArray<PerSkinJobData> perSkinJobData;
        public int batchDataSize;
        // Output
        public NativeArray<float4x4> rootInv;
        // Lookup Data for Bones.
        public NativeArray<int3> boneLookupData;
        // VertexLookup
        public NativeArray<int3> vertexLookupData;

        public void Execute()
        {
            for (int i = 0; i < batchDataSize; ++i)
            {
                var jobData = perSkinJobData[i];
                var skinData = spriteSkinData[i];
                rootInv[i] = skinData.rootInv;
                var bindPoses = skinData.bindPoses.ToNativeSlice();
                for (int j = 0; j < bindPoses.Length; ++j)
                {
                    int x = jobData.bindPosesIndex.x + j;
                    boneLookupData[x] = new int3(i, i, j);
                }
                for (int k = 0, j = jobData.verticesIndex.x; j < jobData.verticesIndex.y; ++j, ++k)
                {
                    vertexLookupData[j] = new int3(i, i, k);
                }
            }
        }
    }

#if ENABLE_ANIMATION_BURST
    [BurstCompile]
#endif 
    internal struct BoneDeformBatchedJob : IJobParallelFor
    {
        [ReadOnly]
        public NativeArray<int3> boneLookupData;
        [ReadOnly]
        public NativeArray<float4x4> rootInv;
        [ReadOnly]
        public NativeArray<SpriteSkinData> spriteSkinData;
        // Output and Input.
        public NativeArray<float4x4> finalBoneTransforms;

        public void Execute(int i)
        {
            int x = boneLookupData[i].x;
            int y = boneLookupData[i].y;
            int z = boneLookupData[i].z;
            var aa = spriteSkinData[y].boneTransforms.ToNativeSlice()[z];
            var bb = spriteSkinData[y].bindPoses.ToNativeSlice()[z];
            finalBoneTransforms[i] = math.mul(rootInv[x], math.mul(aa, bb));
        }
    }

#if ENABLE_ANIMATION_BURST
    [BurstCompile]
#endif 

    internal struct SkinDeformBatchedJob : IJobParallelFor
    {
        public NativeSlice<byte> vertices;
        [ReadOnly]
        public NativeArray<float4x4> finalBoneTransforms;
        [ReadOnly]
        public NativeArray<PerSkinJobData> perSkinJobData;
        [ReadOnly]
        public NativeArray<SpriteSkinData> spriteSkinData;
        [ReadOnly]
        public NativeArray<int3> vertexLookupData;

        public unsafe void Execute(int i)
        {
            int j = vertexLookupData[i].x;
            int y = vertexLookupData[i].y;
            int z = vertexLookupData[i].z;
            PerSkinJobData perSkinData = perSkinJobData[j];
            float3 srcVertex = spriteSkinData[y].vertices.ToNativeSlice().SliceWithStride<float3>()[z];
            float4 tangents = spriteSkinData[y].tangents.ToNativeSlice().SliceWithStride<float4>()[z];
            var influence = spriteSkinData[y].boneWeights.ToNativeSlice()[z];

            int bone0 = influence.boneIndex0 + perSkinData.bindPosesIndex.x;
            int bone1 = influence.boneIndex1 + perSkinData.bindPosesIndex.x;
            int bone2 = influence.boneIndex2 + perSkinData.bindPosesIndex.x;
            int bone3 = influence.boneIndex3 + perSkinData.bindPosesIndex.x;

            byte* deformedPosOffset = (byte*)vertices.GetUnsafePtr();
            NativeSlice<float3> deformableVerticesFloat3 = NativeSliceUnsafeUtility.ConvertExistingDataToNativeSlice<float3>(deformedPosOffset + spriteSkinData[y].deformVerticesStartPos, spriteSkinData[y].spriteVertexStreamSize, spriteSkinData[y].spriteVertexCount);
#if ENABLE_UNITY_COLLECTIONS_CHECKS
            NativeSliceUnsafeUtility.SetAtomicSafetyHandle(ref deformableVerticesFloat3, NativeSliceUnsafeUtility.GetAtomicSafetyHandle(vertices));
#endif
            if (spriteSkinData[y].hasTangents)
            {
                byte* deformedTanOffset = deformedPosOffset + spriteSkinData[y].tangentVertexOffset + spriteSkinData[y].deformVerticesStartPos;
                var deformableTangentsFloat4 = NativeSliceUnsafeUtility.ConvertExistingDataToNativeSlice<float4>(deformedTanOffset , spriteSkinData[y].spriteVertexStreamSize, spriteSkinData[y].spriteVertexCount);
                var tangent = new float4(tangents.xyz, 0.0f);

                tangent =
                    math.mul(finalBoneTransforms[bone0], tangent) * influence.weight0 +
                    math.mul(finalBoneTransforms[bone1], tangent) * influence.weight1 +
                    math.mul(finalBoneTransforms[bone2], tangent) * influence.weight2 +
                    math.mul(finalBoneTransforms[bone3], tangent) * influence.weight3;
#if ENABLE_UNITY_COLLECTIONS_CHECKS
                NativeSliceUnsafeUtility.SetAtomicSafetyHandle(ref deformableTangentsFloat4, NativeSliceUnsafeUtility.GetAtomicSafetyHandle(vertices));
#endif
                deformableTangentsFloat4[z] = new float4(math.normalize(tangent.xyz), tangents.w);
            }
            
            deformableVerticesFloat3[z] =
                math.transform(finalBoneTransforms[bone0], srcVertex) * influence.weight0 +
                math.transform(finalBoneTransforms[bone1], srcVertex) * influence.weight1 +
                math.transform(finalBoneTransforms[bone2], srcVertex) * influence.weight2 +
                math.transform(finalBoneTransforms[bone3], srcVertex) * influence.weight3;
        }
    }

    internal static class NativeArrayHelpers
    {
        public static unsafe void ResizeIfNeeded<T>(ref NativeArray<T> nativeArray, int size) where T : struct
        {
            if (!nativeArray.IsCreated || nativeArray.Length != size)
            {
                nativeArray.Dispose();
                nativeArray = new NativeArray<T>(size, Allocator.Persistent);
            }
        }

        public static unsafe void DisposeIfCreated<T>(this NativeArray<T> nativeArray) where T : struct
        {
            if (nativeArray.IsCreated)
                nativeArray.Dispose();
        }

        [WriteAccessRequired]
        public static unsafe void CopyFromNativeSlice<T, S>(this NativeArray<T> nativeArray, int dstStartIndex, int dstEndIndex, NativeSlice<S> slice, int srcStartIndex, int srcEndIndex) where T : struct where S : struct
        {
            if ((dstEndIndex - dstStartIndex) != (srcEndIndex - srcStartIndex))
                throw new System.ArgumentException($"Destination and Source copy counts must match.", nameof(slice));

            var dstSizeOf = UnsafeUtility.SizeOf<T>();
            var srcSizeOf = UnsafeUtility.SizeOf<T>();

            byte* srcPtr = (byte*)slice.GetUnsafeReadOnlyPtr();
            srcPtr = srcPtr + (srcStartIndex * srcSizeOf);
            byte* dstPtr = (byte*)nativeArray.GetUnsafePtr();
            dstPtr = dstPtr + (dstStartIndex * dstSizeOf);
            UnsafeUtility.MemCpyStride(dstPtr, srcSizeOf, srcPtr, slice.Stride, dstSizeOf, srcEndIndex - srcStartIndex);
        }

    }

    internal class SpriteSkinComposite : ScriptableObject
    {

        static SpriteSkinComposite m_Instance;

        public static SpriteSkinComposite instance
        {
            get
            {
                if (m_Instance == null)
                {
                    var composite = Resources.FindObjectsOfTypeAll<SpriteSkinComposite>();
                    if (composite.Length > 0)
                        m_Instance = composite[0];
                    else
                        m_Instance = ScriptableObject.CreateInstance<SpriteSkinComposite>();
                    m_Instance.hideFlags = HideFlags.HideAndDontSave;
                }
                return m_Instance;
            }
        }

        List<SpriteSkin> m_SpriteSkins = new List<SpriteSkin>();
        NativeArray<float4x4>[] m_BoneTransformsList = new NativeArray<float4x4>[0];

        // Root's worldToLocalMatrix
        NativeArray<byte> m_DeformedVertices;
        NativeArray<float4x4> m_SpriteSkinRootInvMatrix;
        NativeArray<float4x4> m_FinalBoneTransforms;
        NativeArray<PerSkinJobData> m_PerSkinJobData;
        NativeArray<SpriteSkinData> m_SpriteSkinData;
        NativeArray<int3> m_BoneLookupData;
        NativeArray<int3> m_VertexLookupData;
        PerSkinJobData m_SkinBatch = new PerSkinJobData();
        //List<TransformAccessArray> m_BoneTransformAccessArrayList = new List<TransformAccessArray>();
        [SerializeField]
        GameObject m_Helper;

        Action<SpriteRenderer, NativeArray<Vector3>> SetDeformableBuffer = InternalEngineBridge.SetDeformableBuffer;

        internal Action<SpriteRenderer, NativeArray<Vector3>> spriteRendererSetDeformableBuffer
        {
            set
            {
                SetDeformableBuffer = value;
                if(SetDeformableBuffer == null)
                    SetDeformableBuffer = InternalEngineBridge.SetDeformableBuffer;
            }
        }

        internal GameObject helperGameObject
        {
            get => m_Helper;
        }

        internal void AddSpriteSkin(SpriteSkin spriteSkin)
        {
            if (spriteSkin == null)
                return;
            Debug.Assert(m_SpriteSkins.Contains(spriteSkin) == false, string.Format("SpriteSkin {0} is already added", spriteSkin.gameObject.name));
            m_SpriteSkins.Add(spriteSkin);
        }

        internal void RemoveSpriteSkin(SpriteSkin spriteSkin)
        {
            m_SpriteSkins.Remove(spriteSkin);
        }

        public void OnEnable()
        {
            m_Instance = this;
            if (m_Helper == null)
            {
                m_Helper = new GameObject("SpriteSkinManager");
                m_Helper.hideFlags = HideFlags.HideAndDontSave;
                m_Helper.AddComponent<SpriteSkinManager.SpriteSkinManagerInternal>();
            }

            m_DeformedVertices = new NativeArray<byte>(1, Allocator.Persistent);
            m_SpriteSkinRootInvMatrix = new NativeArray<float4x4>(1, Allocator.Persistent);
            m_FinalBoneTransforms = new NativeArray<float4x4>(1, Allocator.Persistent);
            m_PerSkinJobData = new NativeArray<PerSkinJobData>(1, Allocator.Persistent);
            m_SpriteSkinData = new NativeArray<SpriteSkinData>(1, Allocator.Persistent);
            m_BoneLookupData = new NativeArray<int3>(1, Allocator.Persistent);
            m_VertexLookupData = new NativeArray<int3>(1, Allocator.Persistent);
            foreach (var spriteSkin in m_SpriteSkins)
                spriteSkin.batchSkinning = true;
        }

        private void OnDisable()
        {
            foreach (var spriteSkin in m_SpriteSkins)
                spriteSkin.batchSkinning = false;
            m_SpriteSkinRootInvMatrix.DisposeIfCreated();
            m_DeformedVertices.DisposeIfCreated();
            m_PerSkinJobData.DisposeIfCreated();
            m_SpriteSkinData.DisposeIfCreated();
            m_BoneLookupData.DisposeIfCreated();
            m_VertexLookupData.DisposeIfCreated();
            m_FinalBoneTransforms.DisposeIfCreated();
            if (m_Helper != null)
                GameObject.DestroyImmediate(m_Helper);
        }

        unsafe void AddDeform(ref SpriteSkinBatchData batchData, int batchIndex, int spriteSkinIndex, ref int vertexBufferSize)
        {
            var sprite = batchData.sprite;
            Debug.Assert(sprite != null);
            var vertices = sprite.GetVertexAttribute<Vector3>(UnityEngine.Rendering.VertexAttribute.Position);
            var tangents = sprite.GetVertexAttribute<Vector4>(UnityEngine.Rendering.VertexAttribute.Tangent);
            var boneWeights = sprite.GetVertexAttribute<BoneWeight>(UnityEngine.Rendering.VertexAttribute.BlendWeight);
            var bindPoses = sprite.GetBindPoses();

            var boneTransformsArray = batchData.boneTransform;
            Debug.Assert(bindPoses.Length == boneTransformsArray.Length);
            Debug.Assert(boneWeights.Length == sprite.GetVertexCount());

            var boneTransforms = new NativeArray<float4x4>(boneTransformsArray.Length, Allocator.TempJob, NativeArrayOptions.UninitializedMemory);
            m_BoneTransformsList[batchIndex] = boneTransforms;
            for (int i = 0; i < boneTransformsArray.Length; ++i)
                boneTransforms[i] = boneTransformsArray[i].localToWorldMatrix;
            SpriteSkinData skinData = new SpriteSkinData();
            skinData.vertices = new NatliveSliceIntPtr<Vector3>(vertices);
            skinData.bindPoses = new NatliveSliceIntPtr<Matrix4x4>(bindPoses);
            skinData.boneWeights = new NatliveSliceIntPtr<BoneWeight>(boneWeights);
            skinData.boneTransforms = new NatliveSliceIntPtr<float4x4>(boneTransforms);
            skinData.tangents = new NatliveSliceIntPtr<Vector4>(tangents);
            skinData.hasTangents = false;
            skinData.spriteVertexStreamSize = 12;
            skinData.spriteVertexCount = sprite.GetVertexCount();
            skinData.tangentVertexOffset = 0;
            skinData.rootInv = batchData.worldToLocalMatrix;
            skinData.spriteSkinIndex = spriteSkinIndex;
            skinData.deformVerticesStartPos = vertexBufferSize;
            m_SpriteSkinData[batchIndex] = skinData;
            m_SkinBatch.verticesIndex.x = m_SkinBatch.verticesIndex.y;
            m_SkinBatch.verticesIndex.y = m_SkinBatch.verticesIndex.x + skinData.spriteVertexCount;
            vertexBufferSize += skinData.spriteVertexCount * skinData.spriteVertexStreamSize;
            m_SkinBatch.bindPosesIndex.x = m_SkinBatch.bindPosesIndex.y;
            m_SkinBatch.bindPosesIndex.y = m_SkinBatch.bindPosesIndex.x + bindPoses.Length;
            m_PerSkinJobData[batchIndex] = m_SkinBatch;

            //extractBoneTransformJobData.boneTransformOut.Add(boneTransforms);
            //for(int i = 0; i < boneTransformsArray.Length; ++i)
            //    extractBoneTransformJobData.transformAccessArray.Add(boneTransformsArray[i]);
        }

        internal unsafe void LateUpdate()
        {
            int batchIndex = 0;
            m_SkinBatch.verticesIndex = int2.zero;
            m_SkinBatch.bindPosesIndex = int2.zero;
            int vertexBufferSize = 0;

            Profiler.BeginSample("SpriteSkinComposite.PrepareData");
            NativeArrayHelpers.ResizeIfNeeded(ref m_PerSkinJobData, m_SpriteSkins.Count);
            NativeArrayHelpers.ResizeIfNeeded(ref m_SpriteSkinData, m_SpriteSkins.Count);
            if(m_SpriteSkins.Count != m_BoneTransformsList.Length)
                Array.Resize(ref m_BoneTransformsList, m_SpriteSkins.Count);
            var spriteSkinBatchData = new SpriteSkinBatchData();
            //var extractBoneTransformJobData = new ExtractBoneTransformJobData()
            //{
            //    boneTransformOut = new List<NativeArray<float4x4>>(),
            //    transformAccessArray = new List<Transform>()
            //};
            for(int i = 0; i < m_SpriteSkins.Count; ++i)
            {
                Profiler.BeginSample("SpriteSkinComposite.GetSpriteSkinBatchData");
                if(m_SpriteSkins[i].GetSpriteSkinBatchData(ref spriteSkinBatchData))
                {
                    Profiler.BeginSample("SpriteSkinComposite.AddDeform");
                    AddDeform(ref spriteSkinBatchData, batchIndex, i, ref vertexBufferSize);
                    Profiler.EndSample();
                    ++batchIndex;
                }
                Profiler.EndSample();
            }
            Profiler.EndSample();

//            var boneTransformOut = new NativeArray<IntPtr>(extractBoneTransformJobData.boneTransformOut.Count, Allocator.TempJob);
//            var boneTransformSize = new NativeArray<int>(extractBoneTransformJobData.boneTransformOut.Count, Allocator.TempJob);
//#if ENABLE_UNITY_COLLECTIONS_CHECKS
//            var boneTransformSafetyHandle = new NativeArray<AtomicSafetyHandle>(extractBoneTransformJobData.boneTransformOut.Count, Allocator.TempJob);
//#endif
//            for(int i = 0; i < boneTransformOut.Length; ++i)
//            {
//                boneTransformOut[i] = new IntPtr(extractBoneTransformJobData.boneTransformOut[i].GetUnsafePtr());
//                boneTransformSize[i] = extractBoneTransformJobData.boneTransformOut[i].Length;
//#if ENABLE_UNITY_COLLECTIONS_CHECKS
//                boneTransformSafetyHandle[i] = NativeArrayUnsafeUtility.GetAtomicSafetyHandle(extractBoneTransformJobData.boneTransformOut[i]);
//#endif
//            }
//            var transformAccess = new TransformAccessArray(extractBoneTransformJobData.transformAccessArray.ToArray());
//            var extractBoneTransformJob = new ExtractBoneTransformJob()
//            {
//                boneTransformOutput = boneTransformOut,
//                nativeArraySize = boneTransformSize
//#if ENABLE_UNITY_COLLECTIONS_CHECKS
//                ,nativeArrayNativeSafetyHandles = boneTransformSafetyHandle
//#endif
//            };
//            var extractJobHandle = extractBoneTransformJob.Schedule(transformAccess);
            if (batchIndex > 0)
            {
                NativeArrayHelpers.ResizeIfNeeded(ref m_SpriteSkinRootInvMatrix, m_PerSkinJobData.Length);
                NativeArrayHelpers.ResizeIfNeeded(ref m_FinalBoneTransforms, m_SkinBatch.bindPosesIndex.y);
                NativeArrayHelpers.ResizeIfNeeded(ref m_DeformedVertices, vertexBufferSize);
                NativeArrayHelpers.ResizeIfNeeded(ref m_BoneLookupData, m_SkinBatch.bindPosesIndex.y);
                NativeArrayHelpers.ResizeIfNeeded(ref m_VertexLookupData, m_SkinBatch.verticesIndex.y);


                Profiler.BeginSample("SpriteSkin.Prepare");
                PrepareDeformJob prepareJob = new PrepareDeformJob
                {
                    rootInv = m_SpriteSkinRootInvMatrix,
                    spriteSkinData = m_SpriteSkinData,
                    perSkinJobData = m_PerSkinJobData,
                    boneLookupData = m_BoneLookupData,
                    vertexLookupData = m_VertexLookupData,
                    batchDataSize = batchIndex
                };
                var jobHandle = prepareJob.Schedule();
                Profiler.EndSample();


                BoneDeformBatchedJob boneJobBatched = new BoneDeformBatchedJob()
                {
                    rootInv = m_SpriteSkinRootInvMatrix,
                    spriteSkinData = m_SpriteSkinData,
                    boneLookupData = m_BoneLookupData,
                    finalBoneTransforms = m_FinalBoneTransforms,
                };

                //jobHandle = JobHandle.CombineDependencies(extractJobHandle, jobHandle);
                jobHandle = boneJobBatched.Schedule(m_SkinBatch.bindPosesIndex.y, 8, jobHandle);

                SkinDeformBatchedJob skinJobBatched = new SkinDeformBatchedJob()
                {
                    vertices = m_DeformedVertices,
                    vertexLookupData = m_VertexLookupData,
                    spriteSkinData = m_SpriteSkinData,
                    perSkinJobData = m_PerSkinJobData,
                    finalBoneTransforms = m_FinalBoneTransforms,
                };
                jobHandle = skinJobBatched.Schedule(m_SkinBatch.verticesIndex.y, 16, jobHandle);
                JobHandle.ScheduleBatchedJobs();
                jobHandle.Complete();

                Vector3* ptrVertices = (Vector3*)m_DeformedVertices.GetUnsafeReadOnlyPtr();
                for (int i = 0; i < batchIndex; ++i)
                {
                    var skinData = m_SpriteSkinData[i];
                    var vertexBufferLength = skinData.spriteVertexCount;
                    var copyFrom = NativeArrayUnsafeUtility.ConvertExistingDataToNativeArray<Vector3>(ptrVertices, vertexBufferLength, Allocator.None);

#if ENABLE_UNITY_COLLECTIONS_CHECKS
                    NativeArrayUnsafeUtility.SetAtomicSafetyHandle(ref copyFrom, NativeArrayUnsafeUtility.GetAtomicSafetyHandle(m_DeformedVertices));
#endif
                    SetDeformableBuffer(m_SpriteSkins[skinData.spriteSkinIndex].spriteRenderer, copyFrom);
                    ptrVertices = ptrVertices + vertexBufferLength;
                }
            }

            for(int i = 0; i < m_BoneTransformsList.Length; ++i)
            {
                if(m_BoneTransformsList[i].IsCreated)
                    m_BoneTransformsList[i].Dispose();
                m_BoneTransformsList[i] = default(NativeArray<float4x4>);
            }
            //m_BoneTransformAccessArrayList.Clear();
//            boneTransformOut.Dispose();
//            transformAccess.Dispose();
//            boneTransformSize.Dispose();
//#if ENABLE_UNITY_COLLECTIONS_CHECKS
//            boneTransformSafetyHandle.Dispose();
//#endif
        }
    }
}
