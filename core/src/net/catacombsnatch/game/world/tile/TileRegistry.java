package net.catacombsnatch.game.world.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.tile.tiles.DestroyableWallTile;
import net.catacombsnatch.game.world.tile.tiles.FloorTile;
import net.catacombsnatch.game.world.tile.tiles.SandTile;
import net.catacombsnatch.game.world.tile.tiles.WallTile;

public class TileRegistry {
    protected final static ObjectMap<String, Class<? extends Tile>> registry;

    static {
        registry = new ObjectMap<String, Class<? extends Tile>>();

        // Register default tiles
        register(FloorTile.class, "floor");
        register(SandTile.class, "sand");
        register(WallTile.class, "wall");
        register(DestroyableWallTile.class, "destroyable");
    }


    public static <T extends Tile> void register(Class<T> tile, String as) {
        registry.put(as, tile);
    }

    public static Array<String> getTypes() {
        Array<String> types = new Array<String>();
        for (String type : registry.keys()) {
            types.add(type);
        }
        return types;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tile> Class<T> getByName(String name) {
        return name != null ? (Class<T>) registry.get(name) : null;
    }

    public static Tile createFor(String name, Level level, int x, int y) {
        Class<? extends Tile> type = getByName(name);

        if (type != null) try {
            Tile tile = type.newInstance();
            tile.init(level, x, y);

            return tile;

        } catch (Exception e) {
            Gdx.app.error("TileRegistry", "Could not create and add tile to layer", e);
        }
        else {
            Gdx.app.log("TileRegistry", "Tile type not registered: " + name);
        }

        return null;
    }

}
