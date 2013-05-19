package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite extends Component {
	
	public static enum Layer {
		FLOOR,
		ACTORS,
		LIGHTS,
		WALLS,
		PARTICLES;

		public int getLayerId() {
			return ordinal();
		}
	}
	
	public Layer layer = Layer.ACTORS;
	public final TextureRegion t;
	
	public Sprite(TextureRegion t) {
		this.t = t;
	}
	
	public Sprite(Texture t) {
		this.t = new TextureRegion(t);
	}
}