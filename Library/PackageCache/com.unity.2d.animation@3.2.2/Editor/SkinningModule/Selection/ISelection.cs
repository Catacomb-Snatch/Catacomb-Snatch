namespace UnityEditor.U2D.Animation
{
    internal interface ISelection<T>
    {
        int Count { get; }
        T activeElement { get; set; }
        T[] elements { get; set; }
        void Clear();
        void BeginSelection();
        void EndSelection(bool select);
        void Select(T element, bool select);
        bool Contains(T element);
    }
}
