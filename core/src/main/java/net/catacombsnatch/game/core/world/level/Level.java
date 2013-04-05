package net.catacombsnatch.game.core.world.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Level implements Tickable {
	protected EntityManager entityManager;
	
	protected TiledMap map;
	protected List<Layer> layers;
	
	protected boolean debug = false;

	public Level(TiledMap tiledMap) {
		entityManager = new EntityManager();
		
		map = tiledMap;
		layers = new ArrayList<Layer>(map.getLayers().getCount());
		
		// Load layers
		Iterator<MapLayer> it = map.getLayers().iterator();
		while(it.hasNext()) {
			MapLayer layer = it.next();
			layers.add(new Layer(this, layer));
		}
	}

	@Override
	public void tick() {
		for(Layer layer : layers) {
			for(Tile tile : layer.getTiles()) {
				tile.tick();
			}
		}
	}

	/** @param debug True if debug information should be shown. */
	public void showDebug(boolean debug) {
		this.debug = debug;
	}
	
	/** @return A list of all stored level layers. */
	public List<Layer> getLayers() {
		return layers;
	}
	
}
