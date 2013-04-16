package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;

import com.badlogic.gdx.math.Rectangle;

public class Physics implements EntityComponent {
	protected Rectangle bounds;

	public Physics( int x, int y, int w, int h ) {
		bounds = new Rectangle( x, y, w, h );
	}

	public boolean intersects( Rectangle bb ) {
		return bb.overlaps( bounds );
	}
	
	public boolean isWithin( Rectangle bb ) {
		return bb.contains(bounds);
	}

}
