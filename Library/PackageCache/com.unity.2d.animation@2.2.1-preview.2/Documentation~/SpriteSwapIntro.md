# Sprite Swap Overview

__Sprite Swap__ is a feature that enables you to change a GameObject’s rendered Sprite, but keep the same skeleton rig and Animation Clips. This enables you to quickly and easily create  [multiple characters](SLASwap.md) that look different, but have the same animation rigs. You can also use Sprite Swap to switch the displayed Sprite on each frame at run time, to create [frame-by-frame animation](FFanimation.md).

The workflow for implementing Sprite Swap differs if you are following the workflow [integrated with 2D Animation](2DAnimInter.md), or if you are [manually setting up](SSManual.md) your Sprites.

Below is the general workflow for implementing Sprite Swapping:

1. Create and set up a [Sprite Library Asset](SLAsset.md).
2. Attach a [Sprite Library component](SLComponent.md) and [Sprite Resolver component](SRComponent.md) to the GameObject you want to use Sprite Swapping on. This GameObject must also contain a [Sprite Renderer](https://docs.unity3d.com/Manual/class-SpriteRenderer.html) component.

The __Sprite Library component__ contains the reference to a selected __Sprite Library Asset__, while the __Sprite Resolver component__ is used to request a Sprite registered to that Sprite Library Asset by providing the __Category__ and __Label__ value of the desired Sprite.