# Frame-by-frame Animation

You can use Sprite Swap to quickly swap the Sprites rendered at run time, to create a frame-by-frame animation. This is useful for simple animations, such as to show a character blinking.

The following is the recommended workflow:

1. Place the Sprites for each animation frame in the [Sprite Library Asset](SLAsset.md), and assign them all to the same __Category__.  Give each Sprite a __Label__. Label names must be unique in their Category.

   

2. Select your character Prefab, and bring it into the Scene.

   

3. Open the [Animation](https://docs.unity3d.com/Manual/AnimationOverview.html) window, and select your character Prefab. and Select the __Add Property__ button, and select the [Sprite Resolver component](SRComponent.md)’s __Label__ property. 

   ![](images\2DAnim_SpriteSwap_property.png)

   

4. Change the __Label__ property at each keyframe in the Animation window in order of your Animation frames. This simulates a frame-by-frame animation style.