package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.entity.EntityManager;

import com.badlogic.gdx.math.Rectangle;

public class Physics extends EntityComponent {
	protected Rectangle bounds;

	public Physics( EntityManager manager, long id, int x, int y, int w, int h ) {
		super(manager, id);
		
		bounds = new Rectangle( x, y, w, h );
	}

	public boolean intersects( Rectangle bb ) {
		return bb.overlaps( bounds );
	}
	
	public boolean isWithin( Rectangle bb ) {
		return bb.contains(bounds);
	}

}
