package net.catacombsnatch.game.player;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;

public class LocalPlayer implements Player {

    @Override
    public Entity createEntity(Level level) {
        return Entities.createPlayer(level, Art.lordLard);
    }

}
