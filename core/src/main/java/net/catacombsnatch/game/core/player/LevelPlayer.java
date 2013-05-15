package net.catacombsnatch.game.core.player;

import net.catacombsnatch.game.core.entity.EntityFactory;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

public class LevelPlayer {
	protected final Level level;
	
	protected Entity entity;
	protected Health health;

	/**
	 * Creates a new in-game (in level) player
	 * 
	 * @param level The {@link Level} this player plays in
	 * @param x The x-spawn coordinate
	 * @param y The y-spawn coordinate
	 */
	public LevelPlayer( Level level ) {
		this.level = level;
		
		entity = EntityFactory.createPlayerEntity(level);
		
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
