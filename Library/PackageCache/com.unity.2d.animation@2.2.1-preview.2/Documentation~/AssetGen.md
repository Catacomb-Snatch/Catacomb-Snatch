## Asset Generation Behaviour

The following is the steps that Unity takes to generate the various Sprite Swap components.

1. When you import a PSB file with the [PSD Importer](https://docs.unity3d.com/Packages/com.unity.2d.psdimporter@latest/index.html?preview=1), Unity generates a Prefab and creates a [Sprite Library Asset](SLAsset.md) as a sub-Asset of this Prefab.

   

2. Unity then generates a GameObject for each Sprite in the Prefab that does not belong to a __Category__, or is the first Label in the Category.

   

3. Unity attaches the [Sprite Resolver component](SRComponent.md) to all Sprite GameObjects that belong to a Category.

   

4. Unity then attaches the [Sprite Library component](SLComponent.md) to the root GameObject, and the component is set to reference the Sprite Library Asset created in Step 1.
