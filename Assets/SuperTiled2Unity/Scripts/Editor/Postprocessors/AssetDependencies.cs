﻿using System;
using System.Collections.Generic;
using System.Linq;

namespace SuperTiled2Unity.Editor
{
    public class AssetDependencies
    {
        private List<string> m_Dependencies = new List<string>();
        private List<string> m_References = new List<string>();

        public string AssetPath { get; private set; }
        public IEnumerable<string> Dependencies { get { return m_Dependencies; } }
        public IEnumerable<string> References { get { return m_References; } }

        public AssetDependencies(string assetPath)
        {
            AssetPath = assetPath;
        }

        public void AssignDependencies(IEnumerable<string> assetPaths)
        {
            m_Dependencies = assetPaths.ToList();
        }

        public void AddReference(string path)
        {
            if (!m_References.Contains(path, StringComparer.OrdinalIgnoreCase))
            {
                m_References.Add(path);
            }
        }

        public void RemoveReference(string path)
        {
            m_References.RemoveAll(r => r.Equals(path, StringComparison.OrdinalIgnoreCase));
        }
    }
}
