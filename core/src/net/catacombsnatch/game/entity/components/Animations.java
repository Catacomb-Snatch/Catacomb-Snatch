package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.resource.Art;

public class Animations implements Component {
    private final Animation[] animations;
    private float stateTime;


    public Animations(Animation a) {
        animations = new Animation[]{a};
    }

    public Animations(Animation a, Animation a1) {
        animations = new Animation[]{a, a1};
    }

    public Animations(Animation a, Animation a1, Animation a2) {
        animations = new Animation[]{a, a1, a2};
    }

    public Animations(Animation... a) {
        animations = a;
    }

    public Animations(TextureRegion[][] r) {
        animations = new Animation[r.length];
        for (int i = 0; i < r.length; i++) {
            animations[i] = new Animation(0.2f, new Array<>(Art.lordLard[i]), Animation.PlayMode.LOOP);
        }
    }

    public void updateStateTime(float delta) {
        stateTime += delta;
    }

    public Animation getAnimation(int index) {
        return animations[index];
    }

    /**
     * returns the current keyframe for that animation
     *
     * @param index of the animation to pull from
     * @return a TextureRegion of the frame
     */
    public TextureRegion getKeyFrame(int index) {
        return animations[index].getKeyFrame(stateTime);
    }

    /**
     * @return the number of animations
     */
    public int count() {
        return animations.length;
    }

}
