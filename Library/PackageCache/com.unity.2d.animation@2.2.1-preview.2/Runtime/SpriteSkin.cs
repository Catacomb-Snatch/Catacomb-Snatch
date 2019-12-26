#pragma warning disable 0168 // variable declared but not used.

using UnityEngine.Scripting;
using UnityEngine.Experimental.U2D.Common;
using Unity.Collections;
using UnityEngine.Profiling;
using UnityEngine.Scripting.APIUpdating;

namespace UnityEngine.Experimental.U2D.Animation
{
    [Preserve]
    [ExecuteInEditMode]
    [DisallowMultipleComponent]
    [RequireComponent(typeof(SpriteRenderer))]
    [AddComponentMenu("2D Animation/Sprite Skin")]

    internal class SpriteSkin : MonoBehaviour
    {
        [SerializeField]
        private Transform m_RootBone;
        [SerializeField]
        private Transform[] m_BoneTransforms;
        [SerializeField]
        private Bounds m_Bounds;
        [SerializeField]
        private bool m_UseBatching = true;

        private NativeArray<Vector3> m_DeformedVertices;
        private SpriteRenderer m_SpriteRenderer;
        private Sprite m_CurrentDeformSprite;
        private int m_TransformsHash = 0;
        private bool m_ForceSkinning;
        private bool m_BatchSkinning = false;

        public bool batchSkinning
        {
            get { return m_BatchSkinning; }
            set { m_BatchSkinning = value; }
        }

#if UNITY_EDITOR
        internal static Events.UnityEvent onDrawGizmos = new Events.UnityEvent();
        private void OnDrawGizmos() { onDrawGizmos.Invoke(); }

        private bool m_IgnoreNextSpriteChange = true;
        public bool ignoreNextSpriteChange
        {
            get { return m_IgnoreNextSpriteChange; }
            set { m_IgnoreNextSpriteChange = value; }
        }
#endif

#if ENABLE_ANIMATION_BURST
        void OnEnable()
        {
            if (m_UseBatching)
            {
                SpriteSkinComposite.instance.AddSpriteSkin(this);
                m_BatchSkinning = true;
            }
        }
#endif

        internal void UseBatching(bool value)
        {
            m_UseBatching = value;
            if (m_UseBatching)
            {
                SpriteSkinComposite.instance.AddSpriteSkin(this);
                m_BatchSkinning = true;
            }
            else
            {
                SpriteSkinComposite.instance.RemoveSpriteSkin(this);
                m_BatchSkinning = false;
            }
        }

        internal NativeArray<Vector3> deformedVertices
        {
            get
            {
                if (sprite != null)
                {
                    var spriteVertexCount = sprite.GetVertexCount();
                    if (m_DeformedVertices.IsCreated)
                    {
                        if (m_DeformedVertices.Length != spriteVertexCount)
                        {
                            m_DeformedVertices.Dispose();
                            m_DeformedVertices = new NativeArray<Vector3>(spriteVertexCount, Allocator.Persistent);
                            m_TransformsHash = 0;
                        }
                    }
                    else
                    {
                        m_DeformedVertices = new NativeArray<Vector3>(spriteVertexCount, Allocator.Persistent);
                        m_TransformsHash = 0;
                    }
                }
                return m_DeformedVertices;
            }
        }

        void OnDisable()
        {
            DeactivateSkinning();
            if (m_DeformedVertices.IsCreated)
                m_DeformedVertices.Dispose();
#if ENABLE_ANIMATION_BURST
            SpriteSkinComposite.instance.RemoveSpriteSkin(this);
            m_BatchSkinning = false;
#endif
        }

        void LateUpdate()
        {
            if (m_CurrentDeformSprite != sprite)
            {
                DeactivateSkinning();
                m_CurrentDeformSprite = sprite;
            }
            if (isValid && !batchSkinning)
            {
                var inputVertices = deformedVertices;
                var transformHash = SpriteSkinUtility.CalculateTransformHash(this);
                if (inputVertices.Length > 0 && m_TransformsHash != transformHash)
                {
                    SpriteSkinUtility.Deform(sprite, gameObject.transform.worldToLocalMatrix, boneTransforms, ref inputVertices);
                    SpriteSkinUtility.UpdateBounds(this);
                    InternalEngineBridge.SetDeformableBuffer(spriteRenderer, inputVertices);
                    m_TransformsHash = transformHash;
                    m_CurrentDeformSprite = sprite;
                }
            }
        }

        internal bool GetSpriteSkinBatchData(ref SpriteSkinBatchData data)
        {
            if (m_CurrentDeformSprite != sprite)
            {
                DeactivateSkinning();
                m_CurrentDeformSprite = sprite;
            }
            if (isValid)
            {
                Profiler.BeginSample("SpriteSkin.UpdateBounds");
                SpriteSkinUtility.UpdateBounds(this);
                Profiler.EndSample();
                data.sprite = sprite;
                data.boneTransform = boneTransforms;
                Profiler.BeginSample("SpriteSkin.worldToLocalMatrix");
                data.worldToLocalMatrix = gameObject.transform.worldToLocalMatrix;
                Profiler.EndSample();
                return true;
            }
            return false;
        }
        
        internal Sprite sprite
        {
            get
            {
                if (spriteRenderer == null)
                    return null;
                return spriteRenderer.sprite;
            }
        }

        internal SpriteRenderer spriteRenderer
        {
            get
            {
                if (m_SpriteRenderer == null)
                    m_SpriteRenderer = GetComponent<SpriteRenderer>();
                return m_SpriteRenderer;
            }
        }

        internal Transform[] boneTransforms
        {
            get { return m_BoneTransforms; }
            set { m_BoneTransforms = value; }
        }

        internal Transform rootBone
        {
            get { return m_RootBone; }
            set { m_RootBone = value; }
        }

        internal Bounds bounds
        {
            get { return m_Bounds; }
            set { m_Bounds = value; }
        }

        internal bool isValid
        {
            get { return this.Validate() == SpriteSkinValidationResult.Ready; }
        }

        protected virtual void OnDestroy()
        {
            DeactivateSkinning();
        }

        internal void DeactivateSkinning()
        {
            var sprite = spriteRenderer.sprite;

            if (sprite != null)
                InternalEngineBridge.SetLocalAABB(spriteRenderer, sprite.bounds);

            SpriteRendererDataAccessExtensions.DeactivateDeformableBuffer(spriteRenderer);
        }
    }
}
