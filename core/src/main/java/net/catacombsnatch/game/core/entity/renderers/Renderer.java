package net.catacombsnatch.game.core.entity.renderers;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Renderer {
	protected final Entity entity;
	
	public Renderer(Entity entity) {
		this.entity = entity;
	}
	
	public abstract void render(SpriteBatch graphics);
	
}
