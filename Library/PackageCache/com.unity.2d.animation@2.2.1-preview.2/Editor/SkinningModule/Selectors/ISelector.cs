using System.Collections.Generic;

namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface ISelector<T>
    {
        ISelection<T> selection { get; set; }

        void Select();
    }
}
