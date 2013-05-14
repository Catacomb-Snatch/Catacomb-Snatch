package net.catacombsnatch.game.core.player;

import net.catacombsnatch.game.core.entity.EntityHelper;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;
import com.artemis.EntityManager;

public class LevelPlayer {
	protected final Level level;
	
	protected Entity entity;
	protected Health health;

	/**
	 * Creates a new in-game (in level) player
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entity id
	 */
	public LevelPlayer( Level level, int x, int y ) {
		this.level = level;
		
		entity = EntityHelper.createPlayerEntity(level);
		health = entity.getComponent(Health.class);
	}

	/** @return The {@link Level} this player currently plays in. */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Returns the player entity
	 * 
	 * @return The player's entity
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Returns the {@link Health} component for this player
	 * 
	 * @return The {@link Health} component
	 */
	public Health getHealth() {
		return health;
	}
	
}
