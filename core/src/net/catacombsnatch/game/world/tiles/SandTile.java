package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

/**
 * Represents a SandTile.
 *
 * @author spaceemotion
 * @version 1.0
 */
class SandTile extends TileBuilder {

    SandTile() {
        super(Art.tiles_sand[0]);
    }

    @Override
    public Entity createFor(Level level, int x, int y) {
        return Entities.createTile(level, x, y, color, Art.tiles_sand[0], false);
    }

}
