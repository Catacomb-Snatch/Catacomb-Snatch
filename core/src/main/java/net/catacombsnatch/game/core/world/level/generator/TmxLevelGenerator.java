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
		
		for(MapLayer layer : map.getLayers()) {
			if(!(layer instanceof TiledMapTileLayer)) continue;
		
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			if(level == null) {
				level = new Level(this, tileLayer.getWidth(), tileLayer.getHeight());
			}
			
			for(int x = 0; x < tileLayer.getWidth(); x++) {
				for(int y = 0; y < tileLayer.getHeight(); y++) {
					Cell cell = tileLayer.getCell(x, y);
					if(cell == null ||  cell.getTile() == null) continue;
					
					String type = (String) cell.getTile().getProperties().get("type");
					Class<? extends Tile> t = TileRegistry.getByName(type);
					
					if(t != null) try {
						Tile tile = t.newInstance();
						tile.init(level, x, y);
						
						level.getTiles()[x + y * tileLayer.getWidth()] = tile;
						
					} catch (Exception e) {
						Gdx.app.error("TmxLevelGenerator", "Could not add tile to layer", e);
					} else {
						Gdx.app.log("TmxLevelGenerator", "Tile type not registered: " + type);
					}
				}
			}
		}
		
		GeneratorStringOption emptyTile = (GeneratorStringOption) getOption("emptyTile");
		if(emptyTile.getValue() != null) {
			Class<? extends Tile> t = TileRegistry.getByName(emptyTile.getValue());
			
			if(t != null) {
				for(int x = 0; x < level.getWidth(); x++) {
					for(int y = 0; y < level.getHeight(); y++) {
						Tile tile = level.getTiles()[x + y * level.getWidth()];
						
						if(tile == null) try {
							tile = t.newInstance();
							tile.init(level, x, y);
							
						} catch (Exception e) {
							Gdx.app.error("TmxLevelGenerator", "Could not add tile to layer", e);
						}
					}
				}
				
			} else {
				Gdx.app.log("MapLayer", "Tile type not registered: " + emptyTile.getValue());
			}
		}
		
		return level;
	}

}
