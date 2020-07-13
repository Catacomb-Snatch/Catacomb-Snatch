using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IMeshPreviewBehaviour
    {
        float GetWeightMapOpacity(SpriteCache sprite);
        bool DrawWireframe(SpriteCache sprite);
        bool Overlay(SpriteCache sprite);
        bool OverlayWireframe(SpriteCache sprite);
    }
}
