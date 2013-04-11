package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.Minimap;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.tiles.DestroyableWallTile;

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
	protected Color minimapColor;

	protected Rectangle bb;

	
	protected Tile(Color color) {
		minimapColor = color;
	}
	
	public void init(Level level, int x, int y) {
		this.level = level;
		this.bb = new Rectangle( x, y, HEIGHT, WIDTH );
	}

	/** @return The {@link Level} this tile is placed in */
	public Level getLevel() {
		return level;
	}

	/** @return The {@link Rectangle} of the tile boundaries. */
	public Rectangle getBounds() {
		return bb;
	}
	
	/** @return The {@link Color} shown on the minimap. */
	public Color getMinimapColor() {
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
	
	/**
	 * Returns true if the entity can pass the tile.
	 * 
	 * @param entity The entity to check
	 * @return True if the entity can pass, otherwise false
	 */
	public abstract boolean canPass( Entity entity );
	
	/**
	 * Gets an attached tile by its {@link Side}.
	 * 
	 * @param side The side the tile should be attached to
	 * @return The tile, if found, otherwise null.
	 */
	public Tile getRelative(Side side) {
		try {
			Vector2 vec = side.getFor(bb.x, bb.y);
			return level.getTiles()[(int) (vec.x + vec.y * level.getWidth())];
			
		} catch(Exception e) {
			// Thrown when out of bounds, etc.
			return null;
		}
	}
	
	/**
	 * Calculates the average color of a texture region.
	 * Used for the {@link Minimap} colors.
	 * 
	 * @param region The texture region
	 * @return The average color
	 */
	protected static Color getColor(TextureRegion region) {
		region.getTexture().getTextureData().prepare();
		Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();
		
		float t = region.getRegionHeight() * region.getRegionWidth();
		float r = 0, g = 0, b = 0;
		int ox = region.getRegionX(), oy = region.getRegionY();
		
		Color c = new Color();
		for (int y = oy; y < region.getRegionHeight(); y++) {
			for (int x = ox; x < region.getRegionWidth(); x++) {
				Color.rgba8888ToColor(c, pixmap.getPixel(x, y));
				
				r += c.r;
				g += c.g;
				b += c.b;
			}
		}
		
		pixmap.dispose();
		return new Color(r / t, g / t, b / t, 1);
	}
	
	/** Represents a side (and its edges) of a {@link Tile}. */
	public enum Side {
		// Linear sides
		NORTH(0, 1, 0x1), EAST(1, 0, 0x2), SOUTH(0, -1, 0x4), WEST(-1, 0, 0x8),
		
		// Edges
		NORTH_EAST(1, 1, 0x9), EAST_SOUTH(1, -1, 0x10), SOUTH_WEST(-1, -1, 0x12), WEST_NORTH(-1, 1, 0xF);
		
		
		private Vector2 vector;
		private byte weight;
		
		private Side(float x, float y, int b) {
			vector = new Vector2(x, y);
			weight = (byte) b;
		}
		
		public Vector2 getFor(float x, float y) {
			return new Vector2(x, y).add(vector);
		}
		
		public byte getMask() {
			return isEdge() ? (byte) (weight - 0x8) : weight;
		}
		
		public boolean isEdge() {
			return weight > 0x8;
		}
	}
	
}
