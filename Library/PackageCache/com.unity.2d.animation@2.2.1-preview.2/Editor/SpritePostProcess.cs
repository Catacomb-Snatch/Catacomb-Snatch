using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Experimental.U2D;
using Unity.Collections;
using System.Linq;
using UnityEditor.U2D.Sprites;
using UnityEngine.Rendering;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IAnimationAssetPostProcess
    {
        bool OnAfterPostProcess();
    }

    internal class SpritePostProcess : AssetPostprocessor
    {
        private static List<object> m_AssetList;

        static void OnPostprocessAllAssets(string[] importedAssets, string[] deletedAssets, string[] movedAssets, string[] movedFromAssetPaths)
        {
            var dataProviderFactories = new SpriteDataProviderFactories();
            dataProviderFactories.Init();
            m_AssetList = new List<object>();
            List<string> assetPathModified = new List<string>();
            
            foreach (var importedAsset in importedAssets)
            {
                var asset = AssetDatabase.LoadMainAssetAtPath(importedAsset);
                ISpriteEditorDataProvider ai = dataProviderFactories.GetSpriteEditorDataProviderFromObject(asset);
                if (ai != null)
                {
                    ai.InitSpriteEditorDataProvider();
                    var assets = AssetDatabase.LoadAllAssetsAtPath(importedAsset);
                    var sprites = assets.OfType<Sprite>().ToArray<Sprite>();
                    bool dataChanged = false;
                    dataChanged = PostProcessBoneData(ai,  sprites);
                    dataChanged |= PostProcessSpriteMeshData(ai, sprites);
                    if (ai is IAnimationAssetPostProcess)
                        dataChanged |= ((IAnimationAssetPostProcess)ai).OnAfterPostProcess();
                    if (dataChanged)
                    {
                        assetPathModified.Add(importedAsset);
                        m_AssetList.AddRange(assets);
                    }
                }
            }

            if (assetPathModified.Count > 0 && m_AssetList.Count > 0)
            {
                var originalValue = EditorPrefs.GetBool("VerifySavingAssets", false);
                EditorPrefs.SetBool("VerifySavingAssets", false);
                AssetDatabase.ForceReserializeAssets(assetPathModified, ForceReserializeAssetsOptions.ReserializeMetadata);
                EditorPrefs.SetBool("VerifySavingAssets", originalValue);
                m_AssetList.Clear();
                BoneGizmo.instance.ClearSpriteBoneCache();
            }
        }

        static void CalculateLocaltoWorldMatrix(int i, SpriteRect spriteRect, float definitionScale, float pixelsPerUnit, List<SpriteBone> spriteBone, ref SpriteBone?[] outpriteBone, ref NativeArray<Matrix4x4> bindPose)
        {
            if (outpriteBone[i] != null)
                return;
            SpriteBone sp = spriteBone[i];
            var isRoot = sp.parentId == -1;
            var position = isRoot ? (spriteBone[i].position - Vector3.Scale(spriteRect.rect.size, spriteRect.pivot)) : spriteBone[i].position;
            position.z = 0f;
            sp.position = position * definitionScale / pixelsPerUnit;
            sp.length = spriteBone[i].length * definitionScale / pixelsPerUnit;
            outpriteBone[i] = sp;

            // Calculate bind poses
            var worldPosition = Vector3.zero;
            var worldRotation = Quaternion.identity;

            if (sp.parentId == -1)
            {
                worldPosition = sp.position;
                worldRotation = sp.rotation;
            }
            else
            {
                if (outpriteBone[sp.parentId] == null)
                {
                    CalculateLocaltoWorldMatrix(sp.parentId, spriteRect, definitionScale, pixelsPerUnit, spriteBone, ref outpriteBone, ref bindPose);
                }
                var parentBindPose = bindPose[sp.parentId];
                var invParentBindPose = Matrix4x4.Inverse(parentBindPose);

                worldPosition = invParentBindPose.MultiplyPoint(sp.position);
                worldRotation = sp.rotation * invParentBindPose.rotation;
            }

            // Practically Matrix4x4.SetTRInverse
            var rot = Quaternion.Inverse(worldRotation);
            Matrix4x4 mat = Matrix4x4.identity;
            mat = Matrix4x4.Rotate(rot);
            mat = mat * Matrix4x4.Translate(-worldPosition);


            bindPose[i] = mat;
        }

        static bool PostProcessBoneData(ISpriteEditorDataProvider spriteDataProvider, Sprite[] sprites)
        {
            var boneDataProvider = spriteDataProvider.GetDataProvider<ISpriteBoneDataProvider>();
            var textureDataProvider = spriteDataProvider.GetDataProvider<ITextureDataProvider>();

            if (sprites == null || sprites.Length == 0 || boneDataProvider == null || textureDataProvider == null)
                return false;

            bool dataChanged = false;

            float definitionScale = CalculateDefinitionScale(textureDataProvider);

            foreach (var sprite in sprites)
            {
                var guid = sprite.GetSpriteID();
                {
                    SpriteRect spriteRect = spriteDataProvider.GetSpriteRects().First(s => { return s.spriteID == guid; });
                    var spriteBone = boneDataProvider.GetBones(guid);
                    if (spriteBone == null)
                        continue;

                    var spriteBoneCount = spriteBone.Count;
                    var bindPose = new NativeArray<Matrix4x4>(spriteBoneCount, Allocator.Temp);
                    var outputSpriteBones = new SpriteBone ? [spriteBoneCount];
                    for (int i = 0; i < spriteBoneCount; ++i)
                    {
                        CalculateLocaltoWorldMatrix(i, spriteRect, definitionScale, sprite.pixelsPerUnit, spriteBone, ref outputSpriteBones, ref bindPose);
                    }
                    sprite.SetBindPoses(bindPose);
                    sprite.SetBones(outputSpriteBones.Select(x => x.Value).ToArray());
                    bindPose.Dispose();

                    dataChanged = true;
                }
            }

            return dataChanged;
        }

        static bool PostProcessSpriteMeshData(ISpriteEditorDataProvider spriteDataProvider, Sprite[] sprites)
        {
            var spriteMeshDataProvider = spriteDataProvider.GetDataProvider<ISpriteMeshDataProvider>();
            var boneDataProvider = spriteDataProvider.GetDataProvider<ISpriteBoneDataProvider>();
            var textureDataProvider = spriteDataProvider.GetDataProvider<ITextureDataProvider>();
            if (sprites == null || sprites.Length == 0 || spriteMeshDataProvider == null || textureDataProvider == null)
                return false;

            bool dataChanged = false;
            float definitionScale = CalculateDefinitionScale(textureDataProvider);

            foreach (var sprite in sprites)
            {
                var guid = sprite.GetSpriteID();
                var spriteRect = spriteDataProvider.GetSpriteRects().First(s => { return s.spriteID == guid; });
                var spriteBone = boneDataProvider.GetBones(guid);

                var hasBones = spriteBone != null && spriteBone.Count > 0;
                var hasInvalidWeights = false;

                var vertices = spriteMeshDataProvider.GetVertices(guid);
                var indices = spriteMeshDataProvider.GetIndices(guid);

                if (vertices.Length > 2 && indices.Length > 2)
                {
                    var vertexArray = new NativeArray<Vector3>(vertices.Length, Allocator.Temp);
                    var boneWeightArray = new NativeArray<BoneWeight>(vertices.Length, Allocator.Temp);

                    for (int i = 0; i < vertices.Length; ++i)
                    {
                        var boneWeight = vertices[i].boneWeight;

                        vertexArray[i] = (Vector3)(vertices[i].position - Vector2.Scale(spriteRect.rect.size, spriteRect.pivot)) * definitionScale / sprite.pixelsPerUnit;
                        boneWeightArray[i] = boneWeight;

                        if (hasBones && !hasInvalidWeights)
                        {
                            var sum = boneWeight.weight0 + boneWeight.weight1 + boneWeight.weight2 + boneWeight.weight3;
                            hasInvalidWeights = sum < 0.999f;
                        }
                    }

                    var indicesArray = new NativeArray<ushort>(indices.Length, Allocator.Temp);

                    for (int i = 0; i < indices.Length; ++i)
                        indicesArray[i] = (ushort)indices[i];

                    sprite.SetVertexCount(vertices.Length);
                    sprite.SetVertexAttribute<Vector3>(VertexAttribute.Position, vertexArray);
                    sprite.SetIndices(indicesArray);
                    sprite.SetVertexAttribute<BoneWeight>(VertexAttribute.BlendWeight, boneWeightArray);
                    vertexArray.Dispose();
                    boneWeightArray.Dispose();
                    indicesArray.Dispose();

                    dataChanged = true;

                    if (hasBones && hasInvalidWeights)
                        Debug.LogWarning("Sprite \"" + spriteRect.name + "\" contains bone weights which sum zero or are not normalized. To avoid visual artifacts please consider fixing them.");
                }
                else
                {
                    var boneWeightArray = new NativeArray<BoneWeight>(sprite.GetVertexCount(), Allocator.Temp);
                    var defaultBoneWeight = new BoneWeight() { weight0 = 1f };

                    for (var i = 0; i < boneWeightArray.Length; ++i)
                        boneWeightArray[i] = defaultBoneWeight;

                    sprite.SetVertexAttribute<BoneWeight>(VertexAttribute.BlendWeight, boneWeightArray);
                }
            }

            return dataChanged;
        }

        static float CalculateDefinitionScale(ITextureDataProvider dataProvider)
        {
            float definitionScale = 1;
            var texture = dataProvider.texture;
            if (texture != null)
            {
                int actualWidth = 0, actualHeight = 0;
                dataProvider.GetTextureActualWidthAndHeight(out actualWidth, out actualHeight);
                float definitionScaleW = texture.width / (float)actualWidth;
                float definitionScaleH = texture.height / (float)actualHeight;
                definitionScale = Mathf.Min(definitionScaleW, definitionScaleH);
            }
            return definitionScale;
        }
    }
}
