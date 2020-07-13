using UnityEngine;
using System.Collections.Generic;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IRectSelector<T> : ISelector<T>
    {
        Rect rect { get; set; }
    }
}
