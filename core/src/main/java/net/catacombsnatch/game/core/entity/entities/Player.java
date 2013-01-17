package net.catacombsnatch.game.core.entity.entities;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.input.PlayerInput;

public class Player {
	protected PlayerInput inputType;

	protected Entity entity;

	/**
	 * Creates a new player entity
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entiy id
	 */
	public Player( EntityManager manager, long id, int x, int y ) {
		entity = new Entity( manager, id );

		getEntity().addComponent( Health.class );
		getEntity().addComponent( Physics.class, new Physics( x - 4, y - 4, 8, 8 ) );
	}

	/**
	 * Returns the player entity
	 * 
	 * @return The player {@link Entity}
	 */
	public Entity getEntity() {
		return entity;
	}

	public void setInputType( PlayerInput type ) {
		this.inputType = type;
	}

	public PlayerInput getInputType() {
		return inputType;
	}
}
