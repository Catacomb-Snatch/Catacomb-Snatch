package net.catacombsnatch.game.world.tiles;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.catacombsnatch.game.entity.components.MiniMapObject;
import net.catacombsnatch.game.world.level.Level;

/**
 * Represents a builder / constructor for tile entities.
 *
 * @author spaceemotion
 * @version 1.0
 */
public abstract class TileBuilder {
    public final Color color;


    protected TileBuilder(TextureRegion region) {
        this(MiniMapObject.getAverageColorFor(region));
    }

    protected TileBuilder(Color color) {
        this.color = color;
    }

    public abstract Entity createFor(Level level, int x, int y);

}
