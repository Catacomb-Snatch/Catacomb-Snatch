using System;
using UnityEngine;
using UnityEngine.UIElements;

internal static class LayoutOverlayUtility
{
    public static Button CreateButton(string name, Action clickEvent, string tooltip = null, string text = null, string imageResourcePath = null, string stylesheetPath = null)
    {
        Button button = new Button(clickEvent);
        button.name = name;
        button.tooltip = tooltip;

        if (!String.IsNullOrEmpty(text))
            button.text = text;
        if (!String.IsNullOrEmpty(imageResourcePath))
        {
            var texture = Resources.Load<Texture>(imageResourcePath);
            if (texture != null)
            {
                Image image = new Image();
                image.image = texture;
                button.Add(image);
            }
        }
        if (!String.IsNullOrEmpty(stylesheetPath))
            button.styleSheets.Add(Resources.Load<StyleSheet>(stylesheetPath));

        return button;
    }
}
