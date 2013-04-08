package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class View implements Renderable {
	protected Level level;
	protected Rectangle viewport;
	protected int rendered;
	
	protected Sprite panel;
	
	public View(Level lvl) {
		level = lvl;
		
		panel = new Sprite(Art.skin.getAtlas().findRegion("player-panel"));
	}
	
	public void setViewport(Rectangle view) {
		viewport = view;
	}
	
	@Override
	public void render( Scene scene ) {
		if(viewport == null) return;
		
		rendered = 0; // Reset counter
		
		for(Tile tile : level.getTiles()) {
			if(!tile.getBounds().overlaps(viewport))  continue;
				
			tile.render(scene);
			rendered++;
		}
		
		panel.draw(scene.getSpriteBatch());
	}
	
	public void resize() {
		if(viewport == null) return;
		
		panel.setPosition((viewport.getWidth() - panel.getWidth()) / 2, viewport.getHeight() - panel.getHeight());
	}
	
	/** @return The number of tiles rendered during the last {@link #render(Screen)} call. */
	public int getLastRenderedTileCount() {
		return rendered;
	}

}
