package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

import net.catacombsnatch.game.core.world.Direction;

public final class Rotation extends Component {

	private Direction dir;
	
	public Rotation(Direction d) {
		dir = d;
	}
	
	public Rotation(Vector2 v) {
		dir = Direction.getDirectionFor(v);
	}
	
	public Rotation() {
		dir = Direction.EAST;
	}

	public Direction getDirection() {
		return dir;
	}
	
	public void setDirection(Direction d) {
		dir = d;
	}
	
	/**
	 * rotates by `turns.' If turns is negative
	 * it rotates counter clockwise, else clock-
	 * wise. each turn constitutes the smallest
	 * directional change.
	 * 
	 * Current that is 45 degrees.
	 * 
	 * @param turns number of turns to make
	 */
	public void rotate(int turns) {
		int ord = dir.ordinal();
		int index = ord + turns;
		if(index < 0) index += Direction.count;
		index = index % Direction.count;
		
		dir = Direction.values()[index];
	}
}