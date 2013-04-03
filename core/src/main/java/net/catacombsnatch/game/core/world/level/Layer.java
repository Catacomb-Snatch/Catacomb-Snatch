package net.catacombsnatch.game.core.world.level;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;

public class Layer {
	protected final Level level;
	protected final List<Tile> tiles;
	
	public Layer(Level level, MapLayer layer) {
		this.level = level;
		this.tiles = new ArrayList<Tile>();
		
		for(MapObject obj : layer.getObjects()) {
			Class<? extends Tile> t = TileRegistry.getByName((String) obj.getProperties().get("type"));
			
			if(t != null) try {
				Tile tile = t.newInstance();
				tile.init(level, 0, 0);
				
				tiles.add(tile);
					
			} catch (Exception e) {}
		}
	}
	
	public List<Tile> getTiles() {
		return tiles;
	}
}
