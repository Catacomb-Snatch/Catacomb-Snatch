package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import net.catacombsnatch.game.world.Direction;

public class Rotation implements Component {
    public Direction direction = Direction.SOUTH;


    public void forVector(Vector2 vector) {
        direction = Direction.getDirectionFor(vector);
    }

    /**
     * Rotates by 'turns' (Currently in 45 degrees).
     * If the number is negative, it rotates counter clockwise, else clockwise.
     * Each turn constitutes the smallest directional change.
     *
     * @param turns Number of turns to make
     */
    public void rotate(int turns) {
        int index = direction.ordinal() + turns;
        if (index < 0) index += Direction.count;

        direction = Direction.values[index % Direction.count];
    }

}
