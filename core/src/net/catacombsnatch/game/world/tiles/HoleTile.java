package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

/**
 * Represents a HoleTile.
 *
 * @author spaceemotion
 * @version 1.0
 */
class HoleTile extends TileBuilder {

    HoleTile() {
        super(Color.BLACK);
    }

    @Override
    public Entity createFor(Level level, int x, int y) {
        final Entity tile = Entities.createTile(level, x, y, color, Art.tiles_hole, false);

//        Tile northtile = getRelative(Direction.NORTH);
//        if (northtile != null && (northtile instanceof WallTile || northtile instanceof DestroyableWallTile)) {
//            if (tmptile == null) {
//                if (northtile instanceof WallTile) {
//                    tmptile = new FloorTile();
//                }
//                if (northtile instanceof DestroyableWallTile) {
//                    Class<? extends Tile> tileclass = ((DestroyableWallTile) northtile).destroy();
//                    if (tileclass != null) {
//                        try {
//                            tmptile = tileclass.newInstance();
//                        } catch (Exception ignore) {
//                        }
//                    }
//                }
//                if (tmptile != null) {
//                    tmptile.init(level, (int) bb.x / Tiles.SIZE, (int) bb.y / Tiles.SIZE - 1);
//                }
//            }
//            northtile = tmptile;
//        }
//
//        if (northtile != null && !(northtile instanceof HoleTile)) {
//            northtile.getBounds().y += Tiles.SIZE;
//            northtile.render(graphics, view);
//            northtile.getBounds().y-=Tiles.SIZE;
//        }
//
//        renderTile(graphics, view, region);

        return tile;
    }

}
