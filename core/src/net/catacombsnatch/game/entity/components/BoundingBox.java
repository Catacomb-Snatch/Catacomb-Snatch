package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a BoundingBox.
 *
 * @author spaceemotion
 * @version 1.0
 */
public class BoundingBox implements Component {
    public final Rectangle bb = new Rectangle();
    public boolean enabled = true;
    public boolean isTrigger;


    public BoundingBox set(float x, float y, float size) {
        return set(x, y, size, size);
    }

    public BoundingBox set(float x, float y, float width, float height) {
        bb.set(x, y, width, height);
        return this;
    }

}
