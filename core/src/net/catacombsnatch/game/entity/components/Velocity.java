package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity implements Component {
    public final Vector2 vector;


    public Velocity() {
        vector = new Vector2();
    }

    public void add(float mx, float my) {
        vector.add(mx, my);
    }

    public void normalize() {
        vector.nor();
    }

    public Velocity reset() {
        vector.set(0, 0);
        return this;
    }

    public float x() {
        return vector.x;
    }

    public float y() {
        return vector.y;
    }

}
