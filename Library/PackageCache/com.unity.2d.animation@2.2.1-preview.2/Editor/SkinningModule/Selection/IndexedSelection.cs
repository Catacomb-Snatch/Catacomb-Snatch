using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

namespace UnityEditor.Experimental.U2D.Animation
{
    [Serializable]
    internal class IndexedSelection : SerializableSelection<int>
    {
        protected override int GetInvalidElement() { return -1; }
    }
}
