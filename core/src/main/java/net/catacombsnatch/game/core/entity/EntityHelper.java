package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

public class EntityHelper {

	public static Entity createPlayerEntity(Level level) {
		Entity player = level.createEntity();
		player.setGroup("PLAYERS");
		
		player.addComponent(new Health());
		
		return player;
	}
	
}
