﻿using System;
using Unity.Entities;

namespace Unity.Transforms
{
    /// <summary>
    /// Copy Transform to GameObject associated with Entity from TransformMatrix.
    /// </summary>
    public struct CopyTransformToGameObject : IComponentData { }

    [UnityEngine.DisallowMultipleComponent]
    public class CopyTransformToGameObjectProxy : ComponentDataProxy<CopyTransformToGameObject> { } 
}
