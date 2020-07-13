using System.Collections.Generic;
using System.Text;

namespace Unity.Properties
{
    public class PropertyPath
    {
        public const int InvalidListIndex = -1;

        public struct Part
        {
            public string Name;
            public int Index;
            public bool IsListItem => Index >= 0;
        }

        readonly List<Part> m_Parts;

        public int PartsCount => m_Parts.Count;
        public Part this[int index] => m_Parts[index];

        public PropertyPath()
        {
            m_Parts = new List<Part>(32);
        }

        public void Push(string name, int index = InvalidListIndex)
        {
            if (index < 0)
            {
                index = InvalidListIndex;
            }

            m_Parts.Add(new Part
            {
                Name = name,
                Index = index
            });
        }

        public void Pop()
        {
            m_Parts.RemoveAt(m_Parts.Count - 1);
        }

        public void Clear()
        {
            m_Parts.Clear();
        }

        public override string ToString()
        {
            if (m_Parts.Count == 0)
            {
                return string.Empty;
            }

            var builder = new StringBuilder(16);

            foreach (var part in m_Parts)
            {
                if (!part.IsListItem && builder.Length > 0)
                {
                    builder.Append('.');
                }

                builder.Append(part.Name);
            }

            return builder.ToString();
        }
    }
}
