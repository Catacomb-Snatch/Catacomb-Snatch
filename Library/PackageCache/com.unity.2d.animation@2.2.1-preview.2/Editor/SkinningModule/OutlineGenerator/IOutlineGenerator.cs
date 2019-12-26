using UnityEditor.U2D.Sprites;
using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IOutlineGenerator
    {
        void GenerateOutline(ITextureDataProvider textureDataProvider, Rect rect, float detail, byte alphaTolerance, bool holeDetection, out Vector2[][] paths);
    }
}
