package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.generator.options.GeneratorStringOption;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TmxLevelGenerator extends LevelGenerator {
	protected TiledMap map;
	
	
	public TmxLevelGenerator(String file) {
		super();
		
		map = new TmxMapLoader().load(file);
		options.add(new GeneratorStringOption("emptyTile"));
	}
	
	@Override
	public Level generate() {
		Level level = null;
		
		GeneratorStringOption empty = (GeneratorStringOption) getOption("emptyTile");
		boolean fill = empty.getValue() != null && TileRegistry.getByName(empty.getValue()) != null;
		
		for(MapLayer layer : map.getLayers()) {
			if(!(layer instanceof TiledMapTileLayer)) continue;
		
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
		}
		
		// Update all tiles
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				Tile tile = level.getTile(x, y);
				
				if(tile != null) tile.update();
			}
		}
		
		return level;
	}

}
