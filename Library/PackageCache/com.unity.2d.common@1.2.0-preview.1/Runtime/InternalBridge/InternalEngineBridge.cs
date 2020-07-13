using UnityEngine.UIElements;
using Unity.Collections;

namespace UnityEngine.Experimental.U2D.Common
{
    internal static class InternalEngineBridge
    {
        public static void SetLocalAABB(SpriteRenderer spriteRenderer, Bounds aabb)
        {
            spriteRenderer.SetLocalAABB(aabb);
        }

        public static void SetDeformableBuffer(SpriteRenderer spriteRenderer, NativeArray<Vector3> src)
        {
            spriteRenderer.SetDeformableBuffer(src);
        }

        public static Vector2 GUIUnclip(Vector2 v)
        {
            return GUIClip.Unclip(v);
        }

        public static Rect GetGUIClipTopMostRect()
        {
            return GUIClip.topmostRect;
        }

        public static Rect GetGUIClipTopRect()
        {
            return GUIClip.GetTopRect();
        }

#if UNITY_EDITOR
        public static void SetLocalEulerHint(Transform t)
        {
            t.SetLocalEulerHint(t.GetLocalEulerAngles(t.rotationOrder));
        }

        public static bool IsChecked(this VisualElement element)
        {
            return (element.pseudoStates & PseudoStates.Checked) == PseudoStates.Checked;
        }

        public static void SetChecked(this VisualElement element, bool isChecked)
        {
            if (isChecked)
            {
                element.pseudoStates |= PseudoStates.Checked;
            }
            else
            {
                element.pseudoStates &= ~PseudoStates.Checked;
            }
        }

#endif
    }
}
