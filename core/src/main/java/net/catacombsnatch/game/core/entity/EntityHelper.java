package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;

public class EntityHelper {

	public static void addToGroup(Level level, Entity entity, String group) {
		level.getManager(GroupManager.class).add(entity, group.toUpperCase());
	}
	
}
