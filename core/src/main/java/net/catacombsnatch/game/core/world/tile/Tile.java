package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.level.Level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class Tile {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;

	protected final Level level;
	protected final Sprite sprite;

	protected Rectangle bb;

	public Tile( Level level, Sprite sprite ) {
		this.level = level;
		this.sprite = sprite;
	}

	public void init( int x, int y ) {
		this.sprite.setX( x );
		this.sprite.setY( y );

		this.bb = new Rectangle( x, y, HEIGHT, WIDTH );
	}

	public abstract void tick();

	public abstract void render( Screen screen );

	/**
	 * Returns the level the tile is placed in
	 * 
	 * @return The {@link Level}
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Returns the tile sprite.
	 * 
	 * @return The {@link Sprite}
	 */
	public Sprite getSprite() {
		return sprite;
	}

	/**
	 * Returns the {@link Rectangle} of this tile
	 * 
	 * @return The {@link Rectangle}
	 */
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
		if ( entity.hasComponent( Physics.class ) ) return entity.getComponent( Physics.class ).intersects( getBounds() );

		return false;
	}
}
