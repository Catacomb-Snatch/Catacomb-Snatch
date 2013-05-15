package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
	protected Vector2 vel;
	
	public Velocity() {
		vel = new Vector2();
	}
	
	public void force(float mx, float my) {
		vel.add(mx, my);
	}
	
	public void normalize() {
		vel.nor();
	}
	
	public void reset() {
		vel.set(0, 0);
	}
	
	public float getVelocityX() {
		return vel.x;
	}
	
	public float getVelocityY() {
		return vel.y;
	}
	
}
