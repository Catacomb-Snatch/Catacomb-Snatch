package net.catacombsnatch.game.core.entity.entities;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.input.PlayerInput;

public class Player {
	protected PlayerInput inputType;

	protected Entity entity;
	protected Health health;
	protected Physics physics;

	/**
	 * Creates a new player entity
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entiy id
	 */
	public Player( EntityManager manager, long id, int x, int y ) {
		entity = new Entity( manager, id );

		health = entity.addComponent( Health.class );
		physics = entity.addComponent( Physics.class, new Physics( x - 4, y - 4, 8, 8 ) );
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

	/**
	 * Sets the {@link PlayerInput} type for this player
	 * 
	 * @param type The {@link PlayerInput} type
	 */
	public void setInputType( PlayerInput type ) {
		this.inputType = type;
	}

	/**
	 * Returns the {@link PlayerInput} type for this player
	 * 
	 * @return The {@link PlayerInput} type
	 */
	public PlayerInput getInputType() {
		return inputType;
	}
}
