using UnityEngine;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal class Unselector<T> : ISelector<T>
    {
        public ISelection<T> selection { get; set; }

        public void Select()
        {
            selection.Clear();
        }
    }
}
