package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Position extends Component {

	protected Vector2 vec;

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
	
}
