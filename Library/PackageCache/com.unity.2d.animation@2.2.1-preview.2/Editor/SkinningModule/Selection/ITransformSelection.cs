using System;
using System.Linq;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface ITransformSelection<T> : ISelection<T> where T : TransformCache
    {
        T root { get; }
        T[] roots { get; }
    }
}
