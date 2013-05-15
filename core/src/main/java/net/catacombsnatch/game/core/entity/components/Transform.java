package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.world.Direction;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Transform extends Component {
	protected Vector2 vec;
	protected Direction dir;
	
	public Transform(Vector2 position) {
		vec = position;
		setDirection(Direction.SOUTH);
	}
	
	public Transform(float x, float y) {
		this(new Vector2(x, y));
	}
	
	// Positioning
	
	public Vector2 getPosition() {
		return vec;
	}
	
	public float getX() {
		return vec.x;
	}
	
	public float getY() {
		return vec.y;
	}
	
	public final void setLocation(float x, float y) {
		vec = new Vector2(x, y);
	}
	
	public void addX(float x) {
		vec.x += x;
	}

	public void addY(float y) {
		vec.y += y;
	}
	
	public float getDistanceTo(Transform other) {
		return other.vec.dst(vec);
	}
	
	// Rotating
	
	public Direction getDirection() {
		return dir;
	}
	
	public final void setDirection(Direction direction) {
		dir = direction;
	}
	
}
