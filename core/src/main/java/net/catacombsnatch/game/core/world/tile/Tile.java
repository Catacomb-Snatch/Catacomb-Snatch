package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.world.level.Level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class Tile implements Renderable {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;

	protected Level level;
	protected Sprite sprite;

	protected Rectangle bb;

	
	public void init(Level level, int x, int y) {
		this.level = level;
		this.bb = new Rectangle( x, y, HEIGHT, WIDTH );
	}

	public abstract void tick();

	/** @return The {@link Level} this tile is placed in */
	public Level getLevel() {
		return level;
	}

	/** @return The {@link Rectangle} of the tile boundaries. */
	public Rectangle getBounds() {
		return bb;
	}

	/**
	 * Returns true if the entity can pass the tile.
	 * 
	 * @param entity The entity to check
	 * @return True if the entity can pass, otherwise false
	 */
	public boolean canPass( Entity entity ) {
		Physics p = entity.getComponent(Physics.class);
		return p != null ? p.intersects( getBounds() ) : true;
	}
}
