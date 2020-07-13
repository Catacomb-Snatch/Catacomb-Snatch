using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IWeightsGenerator
    {
        BoneWeight[] Calculate(Vector2[] vertices, Edge[] edges, Vector2[] controlPoints, Edge[] bones, int[] pins);
    }
}
