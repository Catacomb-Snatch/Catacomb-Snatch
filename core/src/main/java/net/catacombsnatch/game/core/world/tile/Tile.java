package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.Minimap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Tile implements Renderable, Tickable {
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
	 * Returns true if the entity can pass the tile.
	 * 
	 * @param entity The entity to check
	 * @return True if the entity can pass, otherwise false
	 */
	public abstract boolean canPass( Entity entity );
	
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
		
		int ox = region.getRegionX(), oy = region.getRegionY();
		
		int rgba = pixmap.getPixel(ox, oy);
		for (int yy = oy+1; yy < region.getRegionHeight(); yy++) {
			for (int xx = ox+1; xx < region.getRegionWidth(); xx++) {
				Color c = new Color(0f, 0f, 0f, 0f);
				Color cc = new Color(0f, 0f, 0f, 0f);
				Color.rgba8888ToColor(c, rgba);
				Color.rgba8888ToColor(cc, pixmap.getPixel(xx, yy));
				c.mul(0.5f);
				cc.mul(0.5f);
				c.add(cc);
				rgba = Color.rgba8888(c);
			}
		}
		
		pixmap.dispose();
		
		Color c = new Color();
		Color.rgba8888ToColor(c, rgba);
		return c;
	}
	
}
