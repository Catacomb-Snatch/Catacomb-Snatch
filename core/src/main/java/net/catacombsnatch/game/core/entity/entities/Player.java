package net.catacombsnatch.game.core.entity.entities;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Physics;

public class Player extends Entity {

	/**
	 * Creates a new player entity
	 * 
	 * @param manager The {@link EntityManager}
	 * @param id The entiy id
	 */
	public Player( EntityManager manager, long id, int x, int y ) {
		super( manager, id );

		addComponent( Health.class );

		// TODO Properly set up physics
		addComponent( Physics.class, new Physics( x - 4, y - 4, 8, 8 ) );
	}

}
