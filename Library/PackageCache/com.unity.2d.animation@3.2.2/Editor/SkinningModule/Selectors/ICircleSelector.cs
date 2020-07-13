using UnityEngine;
using System.Collections.Generic;

namespace UnityEditor.U2D.Animation
{
    internal interface ICircleSelector<T> : ISelector<T>
    {
        float radius { get; set; }
    }
}
