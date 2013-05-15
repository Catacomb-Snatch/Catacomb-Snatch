package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class Velocity extends Component {
	protected Vector2 vel;
	
	public Velocity() {
		vel = new Vector2();
	}
	
	public void normalize() {
		vel.nor();
	}
	
	public float getVelocityX() {
		return vel.x;
	}
	
	public float getVelocityY() {
		return vel.y;
	}
	
}
