# Changing Parts of a Character

Sprite Swap offers a quick way to change one Sprite on a model, while everything else remains identical. This means you can alter part of a character’s appearance (for example, change a scarf to a different color) while the rest of the character remains the same. 

In the following example, one Sprite represents a green scarf, and one represents a red scarf. 

![Example character wearing a green scarf in the left-hand image, and a blue scarf in the right-hand image.](images\bothscarves.PNG)

Here’s an example workflow for how you might change the scarf on a 2D character, while everything else remains the same:

1. Place the Sprites you want to swap in the Sprite Library Asset, and assign them all to a Category named __Scarf__. You can do this directly via the the [Skinning Editor](SkinningEditor.md) (see [Editor Integration](EditInter.md)).
   ![](images\image_11.png)

   
   
2. In the Scene, select the character Prefab, and then select the Sprite of the scarf.

3. To swap between the different Sprites, select the Sprite Resolver component’s __Label__ drop-down, and select which one you want to swap to.

The __Sprite Resolver__ displays the two Sprites available in the ‘Scarf’ Category. You can then swap between all available options in the Sprite Resolver component settings.

![Sprite Resolver’s Label set to ‘green scarf’.](images\image_13.png)

![Sprite Resolver’s Label set to ‘blue scarf’.](images\image_14.png)

