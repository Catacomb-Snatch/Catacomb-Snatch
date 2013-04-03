package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.math.Rectangle;

public class View implements Renderable {
	protected Level level;
	protected Rectangle viewport;
	protected int rendered;
	
	public View(Level lvl) {
		level = lvl;
	}
	
	public void setViewport(Rectangle view) {
		viewport = view;
	}
	
	@Override
	public void render( Screen screen ) {
		if(viewport == null) return;
		
		for(Layer layer : level.getLayers()) {
			for(Tile tile : layer.getTiles()) {
				if(!tile.getBounds().overlaps(viewport))  continue;
				
				tile.render(screen);
				rendered++;
			}
		}
	}
	
	/** @return The number of tiles rendered during the last {@link #render(Screen)} call. */
	public int getLastRenderedTileCount() {
		return rendered;
	}

}
