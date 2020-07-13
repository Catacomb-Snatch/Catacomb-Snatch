using System;
using UnityEditor.Experimental.U2D.Layout;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class SkeletonToolView
    {
        private BoneInspectorPanel m_BoneInspectorPanel;

        public event Action<string> onBoneNameChanged = (name) => {};
        public event Action<int> onBoneDepthChanged = (depth) => {};

        public SkeletonToolView()
        {
            m_BoneInspectorPanel = BoneInspectorPanel.GenerateFromUXML();
            m_BoneInspectorPanel.onBoneNameChanged += (n) =>  onBoneNameChanged(n);
            m_BoneInspectorPanel.onBoneDepthChanged += (d) => onBoneDepthChanged(d);
            Hide();
        }
        
        public void Initialize(LayoutOverlay layout)
        {
            layout.rightOverlay.Add(m_BoneInspectorPanel);
        }

        public void Show()
        {
            m_BoneInspectorPanel.SetHiddenFromLayout(false);
        }

        public void Hide()
        {
            m_BoneInspectorPanel.SetHiddenFromLayout(true);
        }

        public void Update(string name, int depth)
        {
            m_BoneInspectorPanel.boneName = name;
            m_BoneInspectorPanel.boneDepth = depth;
        }
    }
}
