package net.catacombsnatch.game.core.entity.entities;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;

public class Player extends Entity {

	/**
	 * Creates a new player entity
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entiy id
	 */
	public Player( EntityManager manager, long id ) {
		super( manager, id );

		addComponent( Health.class );
	}

}
