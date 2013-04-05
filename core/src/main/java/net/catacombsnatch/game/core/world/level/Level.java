package net.catacombsnatch.game.core.world.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Level implements Tickable {
	protected EntityManager entityManager;
	
	protected TiledMap map;
	protected List<Layer> layers;
	
	protected Random random;
	
	protected boolean debug = false;
	protected boolean finished = false;

	public Level(TiledMap tiledMap) {
		random = new Random();
		
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
	
	public static Level fromFile(String file) {
		return new Level(new TmxMapLoader().load(file));
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
	
	/**
	 * Sets whether or not the level is finished.
	 * 
	 * @param finished True if finished, false if not
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/** @return True if the level is finished, otherwise false */
	public boolean hasFinished() {
		return finished;
	}
	
	/**
	 * Sets the random for this level.
	 * 
	 * @param r The random number generator to set.
	 */
	public void setRandom(Random r) {
		random = r;
	}
	
	/** @return The random number generator for this level. */
	public Random getRandom() {
		return random;
	}
	
}
