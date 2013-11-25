package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
	
	public Vector2 vec;
	
	public Velocity() {
		vec = new Vector2();
	}
	
	public void force(float mx, float my) {
		vec.add(mx, my);
	}
	
	public void normalize() {
		vec.nor();
	}
	
	public void reset() {
		vec.set(0, 0);
	}
	
	public float getVelocityX() {
		return vec.x;
	}
	
	public float getVelocityY() {
		return vec.y;
	}
	
	public Vector2 getVelocity() {
		return vec;
	}
}
