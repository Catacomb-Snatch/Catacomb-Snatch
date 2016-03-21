package net.catacombsnatch.game.world.level.generator;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.generator.options.GeneratorStringOption;
import net.catacombsnatch.game.world.tiles.Tiles;

public class TmxLevelGenerator extends LevelGenerator {
    private TiledMap map;
    private Array<Vector2> spawns;


    public TmxLevelGenerator(String file) {
        super();

        final TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.flipY = false;

        map = new TmxMapLoader().load(file, parameters);
        spawns = new Array<>();

        addOption(new GeneratorStringOption("emptyTile"));
    }

    @Override
    public Level generate(Campaign campaign) {
        Level level = null;

        final GeneratorStringOption empty = (GeneratorStringOption) getOption("emptyTile");
        final boolean fill = empty.value != null && Tiles.isRegistered(empty.value);

        for (MapLayer layer : map.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                // Tile layer - the first one always decides the map size
                final TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                if (level == null) {
                    level = new Level(campaign, this, tileLayer.getWidth(), tileLayer.getHeight());
                }

                for (int x = 0; x < tileLayer.getWidth(); x++) {
                    for (int y = 0; y < tileLayer.getHeight(); y++) {
                        final Cell cell = tileLayer.getCell(x, y);
                        final String type = (cell == null || cell.getTile() == null) ?
                                (fill ? empty.value : null) :
                                (String) cell.getTile().getProperties().get("type");

                        if (type != null) {
                            Tiles.createAndAdd(type, level, x, y);
                        }
                    }
                }
            } else {
                // Object layer for additional entities (monsters, spawn points, triggers)
                final MapObjects objects = layer.getObjects();

                for (int i = 0; i < objects.getCount(); i++) {
                    MapObject obj = objects.get(i);
                    String type = obj.getProperties().get("type", String.class);

                    if ("spawnPoint".equalsIgnoreCase(type)) {
                        float x = obj.getProperties().get("x", Float.class);
                        float y = obj.getProperties().get("y", Float.class);

                        spawns.add(new Vector2(x, y).add(0.5f, 0.5f));
                    }
                }
            }
        }

        // Safety check
        if (level == null) {
            return null;
        }

        // Update all tiles
//        for (int x = 0; x < level.width; x++) {
//            for (int y = 0; y < level.height; y++) {
//                Entity tile = level.getTile(x, y);
//                if (tile == null && level.getTile(x, y - 1) != null && !(level.getTile(x, y - 1) instanceof HoleTile)) {
//                    HoleTile hole = new HoleTile();
//
//                    hole.init(level, x, y);
//                    level.setTile(hole, x, y);
//                }
//
//                if (tile == null) {
//                    continue;
//                }
//
//                tile.update();
//            }
//        }

        return level;
    }

    @Override
    public Array<Vector2> getSpawnLocations() {
        return spawns;
    }

}
