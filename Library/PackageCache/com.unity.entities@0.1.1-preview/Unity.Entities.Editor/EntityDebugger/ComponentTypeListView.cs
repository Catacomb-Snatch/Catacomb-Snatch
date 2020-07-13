﻿using System.Collections.Generic;
using UnityEditor;
using UnityEditor.IMGUI.Controls;
using UnityEngine;

namespace Unity.Entities.Editor
{
    
    internal class ComponentTypeListView : TreeView
    {
        private List<ComponentType> types;
        private List<bool> typeSelections;
        private List<GUIContent> typeNames;

        private CallbackAction callback;

        public ComponentTypeListView(TreeViewState state, List<ComponentType> types, List<bool> typeSelections, CallbackAction callback) : base(state)
        {
            this.callback = callback;
            this.types = types;
            this.typeSelections = typeSelections;
            typeNames = new List<GUIContent>(types.Count);
            for (var i = 0; i < types.Count; ++i)
                typeNames.Add(new GUIContent(EntityQueryGUI.SpecifiedTypeName(types[i].GetManagedType())));
            Reload();
        }

        protected override TreeViewItem BuildRoot()
        {
            var root  = new TreeViewItem { id = -1, depth = -1, displayName = "Root" };
            if (types.Count == 0)
            {
                root.AddChild(new TreeViewItem { id = 1, displayName = "No types" });
            }
            else
            {
                for (var i = 0; i < types.Count; ++i)
                {
                    var displayName = (types[i].AccessModeType == ComponentType.AccessMode.Exclude ? "" : "+") + typeNames[i].text;
                    root.AddChild(new TreeViewItem {id = i, displayName = displayName});
                }
            }

            SetupDepthsFromParentsAndChildren(root);
            return root;
        }

        protected override void RowGUI(RowGUIArgs args)
        {
            EditorGUI.BeginChangeCheck();
            typeSelections[args.item.id] = EditorGUI.Toggle(args.rowRect, typeSelections[args.item.id]);
            var style = types[args.item.id].AccessModeType == ComponentType.AccessMode.Exclude
                ? EntityDebuggerStyles.ComponentExclude
                : EntityDebuggerStyles.ComponentRequired;
            var indent = GetContentIndent(args.item);
            var content = typeNames[args.item.id];
            var labelRect = args.rowRect;
            labelRect.xMin = labelRect.xMin + indent;
            labelRect.size = style.CalcSize(content);
            GUI.Label(labelRect, content, style);
            if (EditorGUI.EndChangeCheck())
            {
                callback();
            }
        }
    }
}
