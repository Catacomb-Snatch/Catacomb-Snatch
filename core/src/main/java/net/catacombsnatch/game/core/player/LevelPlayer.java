package net.catacombsnatch.game.core.player;

import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.world.level.Level;

public class LevelPlayer {
	protected final Level level;
	
	protected long entityId;
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
		
		EntityManager eMng = level.getEntityManager();
		entityId = eMng.createEntity();
		health = eMng.addComponent( entityId, Health.class );
		physics = eMng.addComponent( entityId, Physics.class, new Physics( entityId, x - 4, y - 4, 8, 8 ) );
	}

	/** @return The {@link Level} this player currently plays in. */
	public Level getLevel() {
		return level;
	}
	
	/**
	 * Returns the player entity
	 * 
	 * @return The player's entity id
	 */
	public long getEntityId() {
		return entityId;
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
