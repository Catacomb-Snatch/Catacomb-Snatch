using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal static class IconUtility
    {
        static public readonly string k_LightIconResourcePath = "Icons/Light";
        static public readonly string k_DarkIconResourcePath = "Icons/Dark";
        static public readonly string k_SelectedResourceIconPath = "Icons/Selected";

        public static Texture2D LoadIconResource(string name, string personalPath, string proPath)
        {
            string iconPath = "";

            if (EditorGUIUtility.isProSkin && !string.IsNullOrEmpty(proPath))
                iconPath = System.IO.Path.Combine(proPath, "d_" + name);
            else
                iconPath = System.IO.Path.Combine(personalPath, name);
            if (EditorGUIUtility.pixelsPerPoint > 1.0f)
            {
                var icon2x = Resources.Load<Texture2D>(iconPath + "@2x");
                if (icon2x != null)
                    return icon2x;
            }

            return Resources.Load<Texture2D>(iconPath);
        }
    }
}
