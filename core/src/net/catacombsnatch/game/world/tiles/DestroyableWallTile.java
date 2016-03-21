package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Destroyable;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

/**
 * Represents a DestroyableWallTile.
 *
 * @author spaceemotion
 * @version 1.0
 */
class DestroyableWallTile extends TileBuilder {

    DestroyableWallTile() {
        super(Art.tiles_walls[0]);
    }

    @Override
    public Entity createFor(Level level, final int x, final int y) {
        final Entity tile = Entities.createTile(level, x, y, color, Art.tiles_walls[0], true);
        tile.add(new Destroyable().add(new Destroyable.Callback() {
            @Override
            public void onEntityDestroyed(Level level, Entity entity) {
                Tiles.createAndAdd(Tiles.FLOOR, level, x, y);
            }
        }));

        return tile;
    }
}
