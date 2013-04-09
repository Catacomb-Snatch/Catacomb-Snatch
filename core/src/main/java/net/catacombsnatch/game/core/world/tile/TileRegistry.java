package net.catacombsnatch.game.core.world.tile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.catacombsnatch.game.core.world.tile.tiles.DestroyableWallTile;
import net.catacombsnatch.game.core.world.tile.tiles.FloorTile;
import net.catacombsnatch.game.core.world.tile.tiles.SandTile;
import net.catacombsnatch.game.core.world.tile.tiles.WallTile;

public class TileRegistry {
	protected final static Map<String, Class<? extends Tile>> registry;
	static {
		registry = new HashMap<String, Class<? extends Tile>>();
		
		// Register default tiles
		register(FloorTile.class, "floor");
		register(SandTile.class, "sand");
		register(WallTile.class, "wall");
		register(DestroyableWallTile.class, "destroyable");
	}
	
	
	public static <T extends Tile> void register(Class<T> tile, String as) {
		registry.put(as, tile);
	}
	
	public static Collection<String> getTypes() {
		return Collections.unmodifiableCollection(registry.keySet());
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Tile> Class<T> getByName(String name) {
		return name != null ? (Class<T>) registry.get(name) : null;
	}
	
}
