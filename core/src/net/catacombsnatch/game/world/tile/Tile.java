package net.catacombsnatch.game.world.tile;

import net.catacombsnatch.game.screen.Tickable;
import net.catacombsnatch.game.world.Direction;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.Minimap;
import net.catacombsnatch.game.world.level.View;
import net.catacombsnatch.game.world.tile.tiles.DestroyableWallTile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Tile implements Tickable {
	public static final int HEIGHT = 32;
	public static final int WIDTH = 32;

	protected Level level;
	protected int minimapColor;

	protected Rectangle bb;
	protected Vector2 position;
	
	protected Tile(int color) {
		minimapColor = color;
	}

	/** @return The {@link Level} this tile is placed in */
	public Level getLevel() {
		return level;
	}
	
	/** @return The color shown on the {@link Minimap} (in rgba8888 format). */
	public int getMinimapColor() {
		return minimapColor;
	}

	/**
	 * Called whenever a tile gets removed from the level.
	 * Example: {@link DestroyableWallTile}
	 * 
	 * @return The class of the tile getting placed next, null this tile should not be replaced.
	 */
	public Class<? extends Tile> destroy() {
		return null;
	}
	
	public abstract void init(Level level, int x, int y);

	/**
	 * Used to update the tile.
	 * Called whenever a tile gets {@link #destroy()}ed;
	 */
	public abstract void update();
	
	/**
	 * Renders the tile at a given offset on the view.
	 * 
	 * @param graphics The {@link SpriteBatch} to use
	 * @param view The view to render on
	 * @param xOffset The x offset
	 * @param yOffset The y offset
	 */
	public abstract void render(SpriteBatch graphics, View view);
	
	/** @return A {@link Rectangle} containing the tile boundaries. */
	public abstract Rectangle getBounds();

	/**
	 * Returns true if the entity can pass the tile.
	 * 
	 * @param entity The entity to check
	 * @return True if the entity can pass, otherwise false
	 */
	public abstract boolean canPass( long entity );
	
	public abstract boolean shouldRender(View view);
	
	/**
	 * Gets an attached tile by its {@link Direction}.
	 * 
	 * @param dir The direction the tile is attached to
	 * @return The tile, if found, otherwise null.
	 */
	public Tile getRelative(Direction dir) {
		Vector2 vec = dir.getFor(position.x, position.y);
		return level.getTile((int) vec.x, (int) vec.y);
	}
	
	public Tile getRelative(int x, int y) {
		return level.getTile((int) position.x + x, (int) position.y + y);
	}
	
	/** @return The vector of the current tile position. */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 * Calculates the average color of a texture region.
	 * Used for the {@link Minimap} colors.
	 * 
	 * @param region The texture region
	 * @return The average color
	 */
	protected static int getColor(TextureRegion region) {
		region.getTexture().getTextureData().prepare();
		Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();
		
		float t = region.getRegionHeight() * region.getRegionWidth();
		float r = 0, g = 0, b = 0;
		
		Color c = new Color();
		for (int y = region.getRegionY(); y < region.getRegionHeight(); y++) {
			for (int x = region.getRegionX(); x < region.getRegionWidth(); x++) {
				Color.rgba8888ToColor(c, pixmap.getPixel(x, y));
				
				r += c.r;
				g += c.g;
				b += c.b;
			}
		}
		
		pixmap.dispose();
		return ((int)((r / t) * 255) << 24) | ((int)((g / t) * 255) << 16) | ((int)((b / t) * 255) << 8) | 255;
	}
	
}
