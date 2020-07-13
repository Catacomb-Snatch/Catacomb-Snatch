# Character rigging

Follow the below steps to create the bones and skeleton for animating your character:

1. Select the![icon_small_CreateBone](images/icon_small_CreateBone.png)__Create Bone__ tool to begin creating the bones of the character skeleton.

2. With the tool selected, click in the __Sprite Editor __window to define the start-point of the bone. Move the cursor to where the bone should end, and click again to set the bone’s end-point.

3. To create a continuous chain of bones, select the ![icon_small_CreateBone](images/icon_small_CreateBone.png)__Create Bone__ tool and click the end-point of an existing bone. The new bone is started from the end-point, creating a chain.

4. Once all bones are created, generate the mesh geometry for the Sprites. It is recommended to use the ![icon_small_GenGeo](images/icon_small_GenGeo.png)__Auto Geometry__ tool to autogenerate the Sprites’s geometry mesh. Then refine the geometry with the ![icon_small_CreateVertex](images/icon_small_CreateVertex.png)__Create Vertex__ and ![icon_small_CreateEdge](images/icon_small_CreateEdge.png)__Create Edge__ tools.

5. To edit the bones that influence a Sprite, select the ![icon_small_BoneInfluence](images/icon_small_BoneInfluence.png)__Bone Influence__ tool and double-click a Sprite to select it. A list of bones currently influencing the Sprite appears.

   - To remove any of the listed bones, select it in the list and click '-' to remove them.

   - To add a bone, select it in the __Sprite Window__ and click + to add it to the list.

6. The weight attached to vertices affects the influence between bones and the Sprites' geometry. Select the  ![icon_small_GenWeight](images/icon_small_GenWeight.png)__Auto Weights__ tool to autogenerate the weight of a selected Sprites. To generate weights for all Sprites at once, deselect all Sprites before selecting the tool. Refine the weights of the vertices with the ![icon_small_WeightPainter](images/icon_small_WeightPainter.png)__Weight Brush__ and ![icon_small_WeightSlider](images/icon_small_WeightSlider.png)__Weight Slider__ tools.

7. Test the skeleton rig previewing poses with the ![icon_small_PreviewPose](images/icon_small_PreviewPose.png)__Preview Pose__ tool. Move and rotate the different bones to check their influence on the geometry mesh. Previewing poses can also be done while using the following tools: the ![icon_small_WeightPainter](images/icon_small_WeightPainter.png)__Weight Brush__, ![icon_small_WeightSlider](images/icon_small_WeightSlider.png)__Weight Slider__, ![icon_small_BoneInfluence](images/icon_small_BoneInfluence.png)__Bone Influence__, ![icon_small_GenWeight](images/icon_small_GenWeight-1543300126435.png)__Auto Weights__, and ![icon_small_Visibility](images/icon_small_Visibility.png)__Visibility__ tools.
- To restore a skeleton rig to its original pose, select ![icon_small_RestoreBind](images/icon_small_RestoreBind.png)__Reset Pose__. The default pose can be edited with the ![icon_small_EditJoints](images/icon_small_EditJoints.png)__Edit Joints__ tool.


## Animating

To begin animating, drag your character Prefab into the Scene. This creates a GameObject that reassembles the imported character as it originally appeared in the PSB file. The GameObject contains multiple child GameObjects in the order that represents the bone hierarchy.

With the new GameObject in the scene, begin animating it with the usual [Unity animation workflow and tools](https://docs.unity3d.com/Manual/AnimationSection.html).
