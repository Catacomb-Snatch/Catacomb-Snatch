package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.LevelGenerator;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class TmxLevelGenerator extends LevelGenerator {
	protected TiledMap map;
	
	
	public TmxLevelGenerator(String file) {
		super();
		
		map = new TmxMapLoader().load(file);
	}
	
	@Override
	public Level generate() {
		Level level = new Level(this);
		Array<Tile> tiles = level.getTiles();
		
		for(MapLayer layer : map.getLayers()) {
			if(!(layer instanceof TiledMapTileLayer)) continue;
		
			TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
			
			for(int x = 0; x < tileLayer.getWidth(); x++) {
				for(int y = 0; y < tileLayer.getHeight(); y++) {
					Cell cell = tileLayer.getCell(x, y);
					if(cell == null ||  cell.getTile() == null) continue;
					
					String type = (String) cell.getTile().getProperties().get("type");
					Class<? extends Tile> t = TileRegistry.getByName(type);
					
					if(t != null) try {
						Tile tile = t.newInstance();
						tile.init(level, x, y);
						
						tiles.add(tile);
							
					} catch (Exception e) {
						Gdx.app.error("MapLayer", "Could not add tile to layer", e);
					} else {
						Gdx.app.log("MapLayer", "Tile type not registered: " + type);
					}
				}
			}
		}
		
		return level;
	}

}
