using UnityEngine;
using UnityEditor;
using System;
using System.Collections.Generic;
using UnityEngine.Experimental.U2D;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal static class ColorExtensions
    {
        public static Color AlphaMultiplied(this Color c, float multiplier)
        {
            return new Color(c.r, c.g, c.b, c.a * multiplier);
        }
    }
}
