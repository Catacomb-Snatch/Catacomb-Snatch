using UnityEngine;
using UnityEngine.Experimental.U2D.Common;
using UnityEngine.UIElements;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class Toolbar : VisualElement
    {
        public class ToolbarFactory : UxmlFactory<Toolbar, ToolbarUxmlTraits> {}
        public class ToolbarUxmlTraits : VisualElement.UxmlTraits {}

        public Toolbar()
        {
            AddToClassList("Toolbar");
            styleSheets.Add(Resources.Load<StyleSheet>("ToolbarStyle"));
            if (EditorGUIUtility.isProSkin)
                AddToClassList("Dark");
        }

        public void SetButtonChecked(Button toCheck)
        {
            var buttons = this.Query<Button>();
            buttons.ForEach((button) => { button.SetChecked(button == toCheck); });
        }

        protected void SetButtonChecked(Button button, bool check)
        {
            if (button.IsChecked() != check)
            {
                if (check)
                    button.AddToClassList("Checked");
                else
                    button.RemoveFromClassList("Checked");
                button.SetChecked(check);
            }
        }

        public void CollapseToolBar(bool collapse)
        {
            if (collapse)
                AddToClassList("Collapse");
            else
                RemoveFromClassList("Collapse");
        }
    }
}
