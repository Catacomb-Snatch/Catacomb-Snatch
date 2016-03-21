package net.catacombsnatch.game.player;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.world.level.Level;

public interface Player {

    Entity createEntity(Level level);

}
