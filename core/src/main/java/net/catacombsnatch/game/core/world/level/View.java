package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class View implements Renderable {
	protected Level level;
	protected Rectangle viewport;
	protected int rendered;
	protected Vector2 offset;
	
	protected Sprite panel;
	
	protected Minimap minimap;
	
	public View(Level lvl) {
		level = lvl;
		
		offset = new Vector2();
		panel = new Sprite(Art.skin.getAtlas().findRegion("player-panel"));
		
		minimap = new Minimap(level, this);
	}
	
	@Override
	public void render( Scene scene ) {
		if(viewport == null) return;
		
		rendered = 0; // Reset counter
		
		for(float y = (viewport.height + offset.y) / Tile.HEIGHT + 2; y >= (viewport.y + offset.y) / Tile.HEIGHT - 2; y--) {
			for(float x = (viewport.x + offset.x) / Tile.WIDTH - 2; x < (viewport.width + offset.x) / Tile.WIDTH + 2; x++) {
				Tile tile = level.getTile((int) x, (int) y);
				
				if(tile != null && tile.getBounds().overlaps(viewport)) {
					tile.render(scene.getSpriteBatch(), this);
					rendered++;
				}
			}
		}
		
		panel.draw(scene.getSpriteBatch());
		
		minimap.render(scene);
	}
	
	/**
	 * Moves the view offset.
	 * 
	 * @param x Amount of pixels to move along the x-coordinate
	 * @param y Amount of pixels to move along the y-coordinate
	 */
	public void move(float x, float y) {
		offset.add(x, y);
	}
	
	public void resize() {
		if(viewport == null) return;
		
		panel.setPosition((viewport.getWidth() - panel.getWidth()) / 2, viewport.getHeight() - panel.getHeight());
		
		minimap.update(true);
	}
	
	public void setViewport(Rectangle view) {
		viewport = view;
	}
	
	/** @return The number of tiles rendered during the last {@link #render(Screen)} call. */
	public int getLastRenderedTileCount() {
		return rendered;
	}
	
	/** @return The current view offset. */
	public Vector2 getOffset() {
		return offset;
	}

}
