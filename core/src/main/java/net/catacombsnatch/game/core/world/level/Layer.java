package net.catacombsnatch.game.core.world.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Layer {
	protected final Level level;
	protected final List<Tile> tiles;
	
	public Layer(Level level, MapLayer layer) {
		this.level = level;
		this.tiles = new ArrayList<Tile>();
		
		if(layer instanceof TiledMapTileLayer) {
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
		
		Collections.reverse(tiles);
	}
	
	public List<Tile> getTiles() {
		return tiles;
	}
}
