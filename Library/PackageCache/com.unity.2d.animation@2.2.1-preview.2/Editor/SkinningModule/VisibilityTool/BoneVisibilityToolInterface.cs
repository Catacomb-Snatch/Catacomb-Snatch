namespace UnityEditor.Experimental.U2D.Animation
{
    internal interface IBoneVisibilityToolView
    {
        void OnBoneSelectionChange(SkeletonSelection skeleton);
        void OnBoneNameChanged(BoneCache bone);
        void OnSelectionChange(SkeletonCache skeleton);
        void Deactivate();
    }
}
