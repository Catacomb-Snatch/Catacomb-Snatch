package net.catacombsnatch.game.core.entity;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;

public final class EntityHelper {

	public static void addToGroup(Entity entity, String group) {
		entity.getWorld().getManager(GroupManager.class).add(entity, group.toUpperCase());
	}
	
}
