using System;
using System.Linq;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class SelectListView : ListView
    {
        public class CustomUxmlFactory : UxmlFactory<SelectListView, UxmlTraits> {}

        public new void AddToSelection(int index)
        {
            base.AddToSelection(index);
        }

        public new void ClearSelection()
        {
            base.ClearSelection();
        }
    }

    internal class SpriteBoneInfluenceListWidget : VisualElement
    {
        public class CustomUxmlFactory : UxmlFactory<SpriteBoneInfluenceListWidget, CustomUxmlTraits> {}
        public class CustomUxmlTraits : UxmlTraits {}

        private BoneCache[] m_BoneInfluences;
        private SelectListView m_ListView;
        bool m_IgnoreSelectionChange = false;
        private Button m_AddButton;
        private Button m_RemoveButton;
        public Action onAddBone = () => {};
        public Action onRemoveBone = () => {};
        public Action<BoneCache[]> onSelectionChanged = (s) => {};
        public Func<SpriteBoneInflueceToolController> GetController = () => null;

        public SpriteBoneInfluenceListWidget()
        {
            var visualTree = Resources.Load("SpriteBoneInfluenceListWidget") as VisualTreeAsset;
            var ve = visualTree.CloneTree().Q("Container");
            ve.styleSheets.Add(Resources.Load<StyleSheet>("SpriteBoneInfluenceListWidgetStyle"));
            if (EditorGUIUtility.isProSkin)
                AddToClassList("Dark");
            this.Add(ve);
            BindElements();
        }

        private void BindElements()
        {
            m_ListView = this.Q<SelectListView>();
            m_ListView.selectionType = SelectionType.Multiple;
            m_ListView.itemsSource = m_BoneInfluences;
            m_ListView.makeItem = () =>
            {
                var label = new Label()
                {
                    name = "ListRow"
                };
                return label;
            };
            m_ListView.bindItem = (e, index) =>
            {
                if (m_BoneInfluences[index] == null)
                    return;

                (e as Label).text = m_BoneInfluences[index].name;
                if (index % 2 == 0)
                {
                    e.RemoveFromClassList("ListRowOddColor");
                    e.AddToClassList("ListRowEvenColor");
                }
                else
                {
                    e.RemoveFromClassList("ListRowEvenColor");
                    e.AddToClassList("ListRowOddColor");
                }
            };

            m_ListView.onSelectionChanged += OnListViewSelectionChanged;
            m_AddButton = this.Q<Button>("AddButton");
            m_AddButton.clickable.clicked += OnAddButtonClick;
            m_RemoveButton = this.Q<Button>("RemoveButton");
            m_RemoveButton.clickable.clicked += OnRemoveButtonClick;
        }

        private void OnListViewSelectionChanged(List<object> o)
        {
            if (m_IgnoreSelectionChange)
                return;

            var selectedBones = o.ConvertAll(x => x as BoneCache).ToArray();

            onSelectionChanged(selectedBones);
        }

        private void OnAddButtonClick()
        {
            onAddBone();
        }

        private void OnRemoveButtonClick()
        {
            onRemoveBone();
        }

        public void Update()
        {
            m_BoneInfluences = GetController().GetSelectedSpriteBoneInfluence();
            m_ListView.itemsSource = m_BoneInfluences;
            m_ListView.Refresh();
        }

        internal void OnBoneSelectionChanged()
        {
            var selectedBones = GetController().GetSelectedBoneForList(m_BoneInfluences);
            m_IgnoreSelectionChange = true;
            m_ListView.ClearSelection();
            foreach (var bone in selectedBones)
            {
                m_ListView.AddToSelection(bone);
            }
            m_IgnoreSelectionChange = false;
            m_AddButton.SetEnabled(GetController().ShouldEnableAddButton(m_BoneInfluences));
            m_RemoveButton.SetEnabled(m_ListView.selectedIndex >= 0);
        }
    }
}
