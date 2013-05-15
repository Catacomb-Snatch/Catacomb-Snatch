package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.generator.options.GeneratorStringOption;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;
import net.catacombsnatch.game.core.world.tile.tiles.HoleTile;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TmxLevelGenerator extends LevelGenerator {
	protected TiledMap map;
	protected Array<Vector2> spawns;
	
	public TmxLevelGenerator(String file) {
		super();
		
		TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
		parameters.yUp = false;
		
		map = new TmxMapLoader().load(file, parameters);
		
		spawns = new Array<Vector2>();
		
		options.add(new GeneratorStringOption("emptyTile"));
	}
	
	@Override
	public Level generate() {
		Level level = null;
		
		GeneratorStringOption empty = (GeneratorStringOption) getOption("emptyTile");
		boolean fill = empty.getValue() != null && TileRegistry.getByName(empty.getValue()) != null;
		
		for(MapLayer layer : map.getLayers()) {
			if(layer instanceof TiledMapTileLayer) {
				// Tile layer
				
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				if(level == null) {
					level = new Level(this, tileLayer.getWidth(), tileLayer.getHeight());
				}
				
				for(int x = 0; x < tileLayer.getWidth(); x++) {
					for(int y = 0; y < tileLayer.getHeight(); y++) {
						Cell cell = tileLayer.getCell(x, y);
						String type = (cell == null || cell.getTile() == null) ?
								(fill ? empty.getValue() : null) :
								(String) cell.getTile().getProperties().get("type");
						
						if(type != null) level.setTile(TileRegistry.createFor(type, level, x, y), x, y);
					}
				}
			} else {
				// Object layer
				MapObjects objects = layer.getObjects();
				
				for(int i = 0; i < objects.getCount(); i++) {
					MapObject obj = objects.get(i); 
					String type = obj.getProperties().get("type", String.class);
					
					if("spawnPoint".equalsIgnoreCase(type)) {
						float x = obj.getProperties().get("x", Integer.class);
						float y = obj.getProperties().get("y", Integer.class);

						spawns.add(new Vector2(x, y).add(0.5f, 0.5f));  
					}
				}
			}
		}
		
		// Update all tiles
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				Tile tile = level.getTile(x, y);
				if (tile == null && level.getTile(x, y-1) != null && !(level.getTile(x, y-1) instanceof HoleTile)) {
					HoleTile hole = new HoleTile();
					
					hole.init(level, x, y);
					level.setTile(hole, x, y);
				}
				
				if (tile == null) {
					continue;
				}
				
				tile.update();
			}
		}
		
		return level;
	}

	@Override
	public Array<Vector2> getSpawnLocations() {
		return spawns;
	}

}
