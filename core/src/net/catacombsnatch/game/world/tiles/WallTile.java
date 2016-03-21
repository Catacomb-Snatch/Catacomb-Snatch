package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.BoundingBox;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

import java.util.Arrays;

/**
 * Represents a WallTile.
 *
 * @author spaceemotion
 * @version 1.0
 */
class WallTile extends TileBuilder {
    private static final TextureRegion[] textures = Arrays.copyOfRange(Art.tiles_walls, 1, Art.tiles_walls.length);

    WallTile() {
        super(textures[0]);
    }

    @Override
    public Entity createFor(Level level, int x, int y) {
        final Entity tile = Entities.createTile(level, x, y, color, textures, true);
        tile.add(new BoundingBox().set(x, y, Tiles.SIZE));
        return tile;
    }

}
