package net.catacombsnatch.game.world.level.generator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.tiles.Tiles;

public class DebugLevelGenerator extends LevelGenerator {

    @Override
    public Level generate(Campaign campaign) {
        final int registered = Tiles.getRegistered().size();
        final Level level = new Level(campaign, this, registered, 1);

        int i = 0;
        for (String type : Tiles.getRegistered()) {
            Tiles.createAndAdd(type, level, i++, 0);
        }

        return level;
    }

    @Override
    public Array<Vector2> getSpawnLocations() {
        return new Array<>(new Vector2[]{new Vector2(0, 0)});
    }

}
