package net.catacombsnatch.game.core.player;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.world.level.Level;

public class LevelPlayer {
	protected final Level level;
	
	protected Entity entity;
	protected Health health;
	protected Physics physics;

	/**
	 * Creates a new in-game (in level) player
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entity id
	 */
	public LevelPlayer( Level level, int x, int y ) {
		this.level = level;
		
		entity = level.getEntityManager().createEntity();
		health = entity.addComponent( Health.class );
		physics = entity.addComponent( Physics.class, new Physics( x - 4, y - 4, 8, 8 ) );
	}

	/** @return The {@link Level} this player currently plays in. */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Returns the player entity
	 * 
	 * @return The player {@link Entity}
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

	/**
	 * Returns the {@link Physics} component for this player
	 * 
	 * @return The {@link Physics} component
	 */
	public Physics getPhysics() {
		return physics;
	}
	
}
