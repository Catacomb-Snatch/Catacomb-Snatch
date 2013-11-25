package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.world.Direction;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Position extends Component {
	private Direction dir = Direction.SOUTH;
	private final Vector2 vec;


	public Position(Vector2 position) {
		vec = position;
	}
	
	public Position(float x, float y) {
		vec = new Vector2(x, y);
	}
	
	public Vector2 getPosition() {
		return vec;
	}
	
	public float getX() {
		return vec.x;
	}
	
	public float getY() {
		return vec.y;
	}
	
	public void setPosition(float x, float y) {
		vec.x = x;
		vec.y = y;
	}
	
	public void addX(float x) {
		vec.x += x;
	}

	public void addY(float y) {
		vec.y += y;
	}
	
	/** see {@link Vector2#dst} */
	public float getDistanceTo(Position other) {
		return other.vec.dst(vec);
	}
	
	/** see {@link Vector2#dst2} */
	public float getDistanceSqrTo(Position other) {
		return other.vec.dst2(vec);
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
