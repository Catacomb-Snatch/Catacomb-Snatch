package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Represents a component that holds an entity's position.
 *
 * We store an additional z-axis to indicate vertical movement (e.g. bats flying up and down)
 * so we don't have to draw that into the animation, thus X and Y only represent the "floor position" on the screen.
 */
public class Position implements Component {
    public final Vector3 xyz;


    public Position() {
        xyz = new Vector3();
    }

    public float x() {
        return xyz.x;
    }

    public float y() {
        return xyz.y;
    }

    public float z() {
        return xyz.z;
    }

    public Vector2 xy() {
        return new Vector2(xyz.x, xyz.y);
    }

    public Position set(float x, float y) {
        xyz.x = x;
        xyz.y = y;
        return this;
    }

    public Position set(Vector2 position) {
        xyz.set(position, 0);
        return this;
    }

    public Position set(Vector3 position) {
        xyz.set(position);
        return this;
    }

    public void addX(float x) {
        xyz.x += x;
    }

    public void addY(float y) {
        xyz.y += y;
    }

    public void addZ(float z) {
        xyz.z += z;
    }

    /**
     * see {@link Vector3#dst}
     */
    public float getDistanceTo(Position other) {
        return other.xyz.dst(xyz);
    }

    /**
     * see {@link Vector3#dst2}
     */
    public float getDistanceSqrTo(Position other) {
        return other.xyz.dst2(xyz);
    }

}
