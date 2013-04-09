package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.generator.options.GeneratorStringOption;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.Gdx;
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
		
		GeneratorStringOption emptyTile = (GeneratorStringOption) getOption("emptyTile");
		Class<? extends Tile> fillTile = TileRegistry.getByName(emptyTile.getValue());
		boolean fillEmpty = emptyTile.getValue() != null && fillTile != null;
		
		for(MapLayer layer : map.getLayers()) {
			if(!(layer instanceof TiledMapTileLayer)) continue;
		
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			if(level == null) {
				level = new Level(this, tileLayer.getWidth(), tileLayer.getHeight());
			}
			
			for(int x = 0; x < tileLayer.getWidth(); x++) {
				for(int y = 0; y < tileLayer.getHeight(); y++) {
					Cell cell = tileLayer.getCell(x, y);
					Class<? extends Tile> t = null;
					
					if(cell == null || cell.getTile() == null) {
						if(fillEmpty) t = fillTile;
						
					} else {
						String type = (String) cell.getTile().getProperties().get("type");
						t = TileRegistry.getByName(type);
						
						if(t == null) {
							Gdx.app.log("TmxLevelGenerator", "Tile type not registered: " + type);
							continue;
						}
					}
					
					if(t != null) try {
						Tile tile = t.newInstance();
						tile.init(level, x, y);
						
						level.getTiles()[x + y * tileLayer.getWidth()] = tile;
						
					} catch (Exception e) {
						Gdx.app.error("TmxLevelGenerator", "Could not add tile to layer", e);
					}
				}
			}
		}
		
		// Update all tiles
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				Tile tile = level.getTiles()[x + y * level.getWidth()];
				
				if(tile != null) tile.update();
			}
		}
		
		return level;
	}

}
