package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

/**
 * Represents a FloorTile.
 *
 * @author spaceemotion
 * @version 1.0
 */
class FloorTile extends TileBuilder {

    FloorTile() {
        super(Art.tiles_walls[0]);
    }

    @Override
    public Entity createFor(Level level, int x, int y) {
        final Entity tile = Entities.createTile(level, x, y, color, Art.tiles_floor, false);
//                tile.add(level.createComponent(Updatable.class).add(new Updatable.Callback() {
//                    @Override
//                    public void onUpdate(Level level, Entity entity) {
//                        byte mask = 0x0;
//
//                        for (Direction side : Direction.values()) {
//                            if (side.isEdge()) continue;
//
//                            Tile tile = getRelative(side);
//                            if (tile == null) continue;
//
//                            if (tile instanceof SandTile || tile instanceof WallTile || tile instanceof DestroyableWallTile) {
//                                mask += side.getMask();
//                            }
//                        }
//
//                        overlay = mask > 0 ? Art.tiles_shadows[mask - 1] : null;
//                    }
//                }));
        return tile;
    }
}
