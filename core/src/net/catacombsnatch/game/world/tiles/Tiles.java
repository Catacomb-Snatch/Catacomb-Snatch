package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import gnu.trove.map.hash.THashMap;
import net.catacombsnatch.game.world.level.Level;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class Tiles {
    private final static Map<String, TileBuilder> registry;

    public static final String DESTROYABLE = "destroyable";
    public static final String FLOOR = "floor";
    public static final String HOLE = "hole";
    public static final String SAND = "sand";
    public static final String WALL = "wall";

    public static final int SIZE = 32;

    static {
        registry = new THashMap<>();

        // Register default tiles (alphabetically ordered)
        register(DESTROYABLE, new DestroyableWallTile());
        register(FLOOR, new FloorTile());
        register(HOLE, new HoleTile());
        register(SAND, new SandTile());
        register(WALL, new WallTile());
    }

    public static void register(String identifier, TileBuilder constructor) {
        registry.put(identifier, constructor);
    }

    public static boolean isRegistered(String name) {
        return registry.containsKey(name);
    }

    public static Collection<String> getRegistered() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    public static Entity create(String name, Level level, int x, int y) {
        TileBuilder constructor = registry.get(name);
        if (constructor == null) {
            Gdx.app.log(Tiles.class.getName(), "Tile type not registered: " + name);
            return null;
        }

        return constructor.createFor(level, x, y);
    }

    public static Entity createAndAdd(String name, Level level, int x, int y) {
        final Entity tile = create(name, level, x, y);
        level.setTile(tile, x, y);
        return tile;
    }

}
