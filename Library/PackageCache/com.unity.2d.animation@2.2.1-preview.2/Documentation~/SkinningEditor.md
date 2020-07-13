# Skinning Editor module

With the __2D Animation__ package installed in your Project, the __Skinning Editor__ becomes available in the __Sprite Editor.__ The Skinning Editor is the main tool for you to create your animation skeleton and bones, and weighing them to your [imported](PreparingArtwork.md) character artwork.

Select the Skinning Editor by following the steps below:

1. Select the imported Character  and select the __Sprite Editor__.
2. Select the __Skinning Editor__ module from the dropdown menu in the __Sprite Editor__ window. The following options and tools then appear.![The Skinning Editor interface](images/2DAnimationV2_SkinnerUI_expanded.png)The Skinning Editor interface

| Tool                                                         | Default Shortcut | Function                                                     |
| :----------------------------------------------------------- | :--------------: | :----------------------------------------------------------- |
| ![icon_RestoreBind](images/icon_RestoreBind.png)Reset Pose   |    Shift + 1     | Restore a character’s bones and joints to their original positions. |
| ![icon_ToggleView](images/icon_ToggleView.png)Toggle View Mode |    Shift + 2     | Switch between the Character and Sprite Sheet view           |
| ![icon_Copy](images/icon_Copy.png)Copy                       |     Ctrl + C     | Copy the data from the current selection.                    |
| ![icon_Paste](images/icon_Paste.png)Paste                    |     Ctrl + V     | Pastes the copied data.                                      |
| ![icon_Paste](images/icon_Paste.png)Paste                    |    Shift + B     | Show additional pasting options.                             |
| ![icon_Visibility](images/icon_Visibility.png)Visibility     |    Shift + P     | Toggle visibility of selected Sprites or bones.              |
| ![icon_PreviewPose](images/icon_PreviewPose.png)Preview Pose |    Shift + Q     | Preview character poses after rigging.                       |
| ![icon_EditJoints](images/icon_EditJoints.png)Edit Joints    |    Shift + W     | Reposition the bones into a new positions. These changes are automatically saved as the default bind pose for the Restore Bind Pose tool.<br/>Sprite geometry does not deform with the bones in this mode, even if the bones are attached as influencers. |
| ![icon_CreateBone](images/icon_CreateBone.png)Create Bone    |    Shift + E     | Click and drag to create bones.                              |
| ![icon_SplitBone](images/icon_SplitBone.png)Split Bone       |    Shift + R     | Splits the selected bone.                                    |
| ![icon_GenGeo](images/icon_GenGeo.png)Auto Geometry          |    Shift + A     | Autogenerate meshes for Sprites.                             |
| ![icon_EditGeo](images/icon_EditGeo.png)Edit Geometry        |    Shift + S     | Edit generated meshes by repositioning vertices.             |
| ![icon_CreateVertex](images/icon_CreateVertex.png)Create Vertex |    Shift + D     | Create new vertices to create geometry.                      |
| ![icon_CreateEdge](images/icon_CreateEdge.png)Create Edge    |    Shift + G     | Create new edges to create geometry.                         |
| ![icon_SplitEdge](images/icon_SplitEdge.png)Split Edge       |    Shift + H     | Split an existing edge into two.                             |
| ![icon_GenWeights](images/icon_GenWeights.png)Auto Weights   |    Shift + Z     | Autogenerate weights for geometry.                           |
| ![icon_WeightSlider](images/icon_WeightSlider.png)Weight Slider |    Shift + X     | Adjust weights via slider control.                           |
| ![icon_WeightPaint](images/icon_WeightPaint.png)Weight Brush |    Shift + C     | Adjust weights by painting with a brush.                     |
| ![icon_BoneInfluence](images/icon_BoneInfluence.png)Bone Influence |    Shift + V     | Select which bones influence a Sprite.                       |
| Toggle Tool Text                                             |    Shift + `     | Show or hide text on tool buttons                            |

## Selecting a Sprite

To select a Sprite in the Skinning Editor window:

1. Double-click a Sprite to select it.
2. If there are multiple Sprites that overlay each other, double-click to cycle through all Sprites under the cursor location.
3. Double-click on a blank space in the Editor window to deselect all Sprites.

## Selecting Bone and Mesh Vertices

To select a bone or mesh vertex: 

1. Click directly on a bone or mesh vertex to select it.
2. Click and drag a selection box over multiple bones or vertices to select them all at once.
3. Right click to deselect any selected bone or mesh vertices.