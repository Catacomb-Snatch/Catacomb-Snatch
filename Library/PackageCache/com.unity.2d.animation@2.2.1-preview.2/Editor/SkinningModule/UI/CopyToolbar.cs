using System;
using UnityEngine;
using UnityEngine.UIElements;
using UnityEngine.Experimental.U2D.Common;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class CopyToolbar : Toolbar
    {
        public class CopyToolbarFactory : UxmlFactory<CopyToolbar, CopyToolbarUxmlTraits> {}
        public class CopyToolbarUxmlTraits : VisualElement.UxmlTraits {}

        public event Action onDoCopy = () => {};
        public event Action onDoPaste = () => {};

        public CopyToolbar()
        {
            styleSheets.Add(Resources.Load<StyleSheet>("CopyToolbarStyle"));
        }

        public void DoCopy()
        {
            onDoCopy();
        }

        public void DoPaste()
        {
            onDoPaste();
        }

        public void BindElements()
        {
            var copyButton = this.Q<Button>("Copy");
            copyButton.clickable.clicked += DoCopy;

            var pasteButton = this.Q<Button>("Paste");
            pasteButton.clickable.clicked += DoPaste;
        }

        public static CopyToolbar GenerateFromUXML()
        {
            var visualTree = Resources.Load("CopyToolbar") as VisualTreeAsset;
            var clone = visualTree.CloneTree().Q<CopyToolbar>("CopyToolbar");
            clone.BindElements();
            return clone;
        }
    }
}
