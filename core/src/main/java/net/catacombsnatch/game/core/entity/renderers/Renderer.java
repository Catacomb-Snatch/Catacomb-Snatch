package net.catacombsnatch.game.core.entity.renderers;

import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Renderer {
	protected Level level;
	protected Entity entity;
	
	public Renderer(Level level, Entity entity) {
		this.level = level;
		this.entity = entity;
	}
	
	public abstract void initialize();
	
	public abstract void render(SpriteBatch graphics);
	
}
