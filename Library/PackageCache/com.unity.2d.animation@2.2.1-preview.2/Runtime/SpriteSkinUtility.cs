using System;
using System.Collections.Generic;
using UnityEngine.Experimental.U2D.Common;
using Unity.Collections;
using Unity.Mathematics;
using Unity.Collections.LowLevel.Unsafe;
using Unity.Burst;
using Unity.Jobs;

namespace UnityEngine.Experimental.U2D.Animation
{

    internal enum SpriteSkinValidationResult
    {
        SpriteNotFound,
        SpriteHasNoSkinningInformation,
        SpriteHasNoWeights,
        RootTransformNotFound,
        InvalidTransformArray,
        InvalidTransformArrayLength,
        TransformArrayContainsNull,
        RootNotFoundInTransformArray,

        Ready
    }

    internal static class SpriteSkinUtility
    {
        internal static SpriteSkinValidationResult Validate(this SpriteSkin spriteSkin)
        {
            if (spriteSkin.spriteRenderer.sprite == null)
                return SpriteSkinValidationResult.SpriteNotFound;

            var bindPoses = spriteSkin.spriteRenderer.sprite.GetBindPoses();

            if (bindPoses.Length == 0)
                return SpriteSkinValidationResult.SpriteHasNoSkinningInformation;

            if (spriteSkin.rootBone == null)
                return SpriteSkinValidationResult.RootTransformNotFound;

            if (spriteSkin.boneTransforms == null)
                return SpriteSkinValidationResult.InvalidTransformArray;

            if (bindPoses.Length != spriteSkin.boneTransforms.Length)
                return SpriteSkinValidationResult.InvalidTransformArrayLength;

            var rootFound = false;
            foreach (var boneTransform in spriteSkin.boneTransforms)
            {
                if (boneTransform == null)
                    return SpriteSkinValidationResult.TransformArrayContainsNull;

                if (boneTransform == spriteSkin.rootBone)
                    rootFound = true;
            }

            if (!rootFound)
                return SpriteSkinValidationResult.RootNotFoundInTransformArray;

            return SpriteSkinValidationResult.Ready;
        }

        internal static void CreateBoneHierarchy(this SpriteSkin spriteSkin)
        {
            if (spriteSkin.spriteRenderer.sprite == null)
                throw new InvalidOperationException("SpriteRenderer has no Sprite set");

            var spriteBones = spriteSkin.spriteRenderer.sprite.GetBones();
            var transforms = new Transform[spriteBones.Length];
            Transform root = null;

            for (int i = 0; i < spriteBones.Length; ++i)
            {
                CreateGameObject(i, spriteBones, transforms, spriteSkin.transform);
                if (spriteBones[i].parentId < 0 && root == null)
                    root = transforms[i];
            }

            spriteSkin.rootBone = root;
            spriteSkin.boneTransforms = transforms;
        }

        private static void CreateGameObject(int index, SpriteBone[] spriteBones, Transform[] transforms, Transform root)
        {
            if (transforms[index] == null)
            {
                var spriteBone = spriteBones[index];
                if (spriteBone.parentId >= 0)
                    CreateGameObject(spriteBone.parentId, spriteBones, transforms, root);

                var go = new GameObject(spriteBone.name);
                var transform = go.transform;
                if (spriteBone.parentId >= 0)
                    transform.SetParent(transforms[spriteBone.parentId]);
                else
                    transform.SetParent(root);
                transform.localPosition = spriteBone.position;
                transform.localRotation = spriteBone.rotation;
                transform.localScale = Vector3.one;
                transforms[index] = transform;
            }
        }

        internal static void ResetBindPose(this SpriteSkin spriteSkin)
        {
            if (!spriteSkin.isValid)
                throw new InvalidOperationException("SpriteSkin is not valid");

            var spriteBones = spriteSkin.spriteRenderer.sprite.GetBones();
            var boneTransforms = spriteSkin.boneTransforms;

            for (int i = 0; i < boneTransforms.Length; ++i)
            {
                var boneTransform = boneTransforms[i];
                var spriteBone = spriteBones[i];

                if (spriteBone.parentId != -1)
                {
                    boneTransform.localPosition = spriteBone.position;
                    boneTransform.localRotation = spriteBone.rotation;
                    boneTransform.localScale = Vector3.one;
                }
            }
        }

        //TODO: Add other ways to find the transforms in case the named path fails
        internal static void Rebind(this SpriteSkin spriteSkin)
        {
            if (spriteSkin.spriteRenderer.sprite == null)
                throw new ArgumentException("SpriteRenderer has no Sprite set");
            if (spriteSkin.rootBone == null)
                throw new ArgumentException("SpriteSkin has no rootBone");

            var spriteBones = spriteSkin.spriteRenderer.sprite.GetBones();
            var boneTransforms = new List<Transform>();

            for (int i = 0; i < spriteBones.Length; ++i)
            {
                var boneTransformPath = CalculateBoneTransformPath(i, spriteBones);
                var boneTransform = spriteSkin.rootBone.Find(boneTransformPath);

                boneTransforms.Add(boneTransform);
            }

            spriteSkin.boneTransforms = boneTransforms.ToArray();
        }

        private static string CalculateBoneTransformPath(int index, SpriteBone[] spriteBones)
        {
            var path = "";

            while (index != -1)
            {
                var spriteBone = spriteBones[index];
                var spriteBoneName = spriteBone.name;
                if (spriteBone.parentId != -1)
                {
                    if (string.IsNullOrEmpty(path))
                        path = spriteBoneName;
                    else
                        path = spriteBoneName + "/" + path;
                }
                index = spriteBone.parentId;
            }

            return path;
        }

        private static int GetHash(Matrix4x4 matrix)
        {
            unsafe
            {
                uint* b = (uint*)&matrix;
                {
                    var c = (char*)b;
                    return (int)math.hash(c, 16 * sizeof(float));
                }
            }
        }

        internal static int CalculateTransformHash(this SpriteSkin spriteSkin)
        {
            int bits = 0;
            int boneTransformHash = GetHash(spriteSkin.transform.localToWorldMatrix) >> bits;
            bits++;
            foreach (var transform in spriteSkin.boneTransforms)
            {
                boneTransformHash ^= GetHash(transform.localToWorldMatrix) >> bits;
                bits = (bits + 1) % 8;
            }
            return boneTransformHash;
        }

        internal static void Deform(float4x4 rootInv, NativeSlice<float3> vertices, NativeSlice<BoneWeight> boneWeights, NativeArray<float4x4> boneTransforms, NativeSlice<float4x4> bindPoses, NativeArray<float3> deformed)
        {
            if (boneTransforms.Length == 0)
                return;

            for (var i = 0; i < boneTransforms.Length; i++)
            {
                var bindPoseMat = bindPoses[i];
                var boneTransformMat = boneTransforms[i];
                boneTransforms[i] = math.mul(rootInv, math.mul(boneTransformMat, bindPoseMat));
            }

            for (var i = 0; i < vertices.Length; i++)
            {
                var bone0 = boneWeights[i].boneIndex0;
                var bone1 = boneWeights[i].boneIndex1;
                var bone2 = boneWeights[i].boneIndex2;
                var bone3 = boneWeights[i].boneIndex3;

                deformed[i] =
                    math.transform(boneTransforms[bone0], vertices[i]) * boneWeights[i].weight0 +
                    math.transform(boneTransforms[bone1], vertices[i]) * boneWeights[i].weight1 +
                    math.transform(boneTransforms[bone2], vertices[i]) * boneWeights[i].weight2 +
                    math.transform(boneTransforms[bone3], vertices[i]) * boneWeights[i].weight3;
            }
        }

        internal unsafe static void Deform(Matrix4x4 rootInv, NativeSlice<Vector3> vertices, NativeSlice<BoneWeight> boneWeights, NativeArray<Matrix4x4> boneTransforms, NativeSlice<Matrix4x4> bindPoses, NativeArray<Vector3> deformableVertices)
        {
            var verticesFloat3 = vertices.SliceWithStride<float3>();
            var bindPosesFloat4x4 = bindPoses.SliceWithStride<float4x4>();
            var boneTransformsFloat4x4 = NativeArrayUnsafeUtility.ConvertExistingDataToNativeArray<float4x4>(boneTransforms.GetUnsafePtr(), boneTransforms.Length, Allocator.None);
            var deformableVerticesFloat3 = NativeArrayUnsafeUtility.ConvertExistingDataToNativeArray<float3>(deformableVertices.GetUnsafePtr(), deformableVertices.Length, Allocator.None);

#if ENABLE_UNITY_COLLECTIONS_CHECKS
            var handle1 = CreateSafetyChecks<float4x4>(ref boneTransformsFloat4x4);
            var handle2 = CreateSafetyChecks<float3>(ref deformableVerticesFloat3);
#endif

            Deform(rootInv, verticesFloat3, boneWeights, boneTransformsFloat4x4, bindPosesFloat4x4, deformableVerticesFloat3);

#if ENABLE_UNITY_COLLECTIONS_CHECKS
            DisposeSafetyChecks(handle1);
            DisposeSafetyChecks(handle2);
#endif
        }

        internal unsafe static void Deform(Sprite sprite, Matrix4x4 invRoot, Transform[] boneTransformsArray, ref NativeArray<Vector3> deformableVertices)
        {
            Debug.Assert(sprite != null);
            Debug.Assert(sprite.GetVertexCount() == deformableVertices.Length);
            
            var vertices = sprite.GetVertexAttribute<Vector3>(UnityEngine.Rendering.VertexAttribute.Position);
            var boneWeights = sprite.GetVertexAttribute<BoneWeight>(UnityEngine.Rendering.VertexAttribute.BlendWeight);
            var bindPoses = sprite.GetBindPoses();
            
            Debug.Assert(bindPoses.Length == boneTransformsArray.Length);
            Debug.Assert(boneWeights.Length == sprite.GetVertexCount());
            
            var boneTransforms = new NativeArray<Matrix4x4>(boneTransformsArray.Length, Allocator.Temp, NativeArrayOptions.UninitializedMemory);

            for (var i = 0; i < boneTransformsArray.Length; ++i)
                boneTransforms[i] = boneTransformsArray[i].localToWorldMatrix;
            Deform(invRoot, vertices, boneWeights, boneTransforms, bindPoses, deformableVertices);
            boneTransforms.Dispose();
        }

#if ENABLE_UNITY_COLLECTIONS_CHECKS
        private static AtomicSafetyHandle CreateSafetyChecks<T>(ref NativeArray<T> array) where T : struct
        {
            var handle = AtomicSafetyHandle.Create();
            AtomicSafetyHandle.SetAllowSecondaryVersionWriting(handle, true);
            AtomicSafetyHandle.UseSecondaryVersion(ref handle);
            NativeArrayUnsafeUtility.SetAtomicSafetyHandle<T>(ref array, handle);
            return handle;
        }

        private static void DisposeSafetyChecks(AtomicSafetyHandle handle)
        {
            AtomicSafetyHandle.Release(handle);
        }
#endif

        internal static Vector3[] Bake(this SpriteSkin spriteSkin)
        {
            if (!spriteSkin.isValid)
                throw new Exception("Bake error: invalid SpriteSkin");

            var sprite = spriteSkin.spriteRenderer.sprite;
            var boneTransformsArray = spriteSkin.boneTransforms;
            var deformableVertices = new NativeArray<Vector3>(sprite.GetVertexCount(), Allocator.Temp, NativeArrayOptions.UninitializedMemory);

            Deform(sprite, Matrix4x4.identity, boneTransformsArray, ref deformableVertices);
            
            var result = deformableVertices.ToArray();
            deformableVertices.Dispose();
            return result;
        }

        internal static void CalculateBounds(this SpriteSkin spriteSkin)
        {
            Debug.Assert(spriteSkin.isValid);

            var rootBone = spriteSkin.rootBone;
            var deformableVertices = spriteSkin.Bake();
            var bounds = new Bounds();

            if (deformableVertices.Length > 0)
            {
                bounds.min = rootBone.InverseTransformPoint(deformableVertices[0]);
                bounds.max = bounds.min;
            }

            foreach(var v in deformableVertices)
                bounds.Encapsulate(rootBone.InverseTransformPoint(v));

            bounds.extents = Vector3.Scale(bounds.extents, new Vector3(1.25f, 1.25f, 1f)); 
            spriteSkin.bounds = bounds;
        }

        internal static void UpdateBounds(this SpriteSkin spriteSkin)
        {
            var worldToLocal = spriteSkin.transform.worldToLocalMatrix;
            var rootLocalToWorld = spriteSkin.rootBone.localToWorldMatrix;
            var unityBounds = spriteSkin.bounds;
            var matrix = math.mul(worldToLocal, rootLocalToWorld);
            var center = new float4(unityBounds.center, 1);
            var extents = new float4(unityBounds.extents, 0);
            var p0 = math.mul(matrix, center + new float4(-extents.x, -extents.y, extents.z, extents.w));
            var p1 = math.mul(matrix, center + new float4(-extents.x, extents.y, extents.z, extents.w));
            var p2 = math.mul(matrix, center + extents);
            var p3 = math.mul(matrix, center + new float4(extents.x, -extents.y, extents.z, extents.w));
            var min = math.min(p0, math.min(p1, math.min(p2, p3)));
            var max = math.max(p0, math.max(p1, math.max(p2, p3)));
            extents = (max - min) * 0.5f;
            center = min + extents;
            var newBounds = new Bounds()
            {
                center = new Vector3(center.x, center.y, center.z),
                extents = new Vector3(extents.x, extents.y, extents.z)
            };            
            InternalEngineBridge.SetLocalAABB(spriteSkin.spriteRenderer, newBounds);
        }
    }
}