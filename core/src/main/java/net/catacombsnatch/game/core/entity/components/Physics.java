package net.catacombsnatch.game.core.entity.components;

import com.badlogic.gdx.math.Rectangle;

public class Physics implements EntityComponent {
	private static final long serialVersionUID = 1L;

	protected Rectangle bounds;

	public Physics( int x, int y, int w, int h ) {
		bounds = new Rectangle( x, y, w, h );
	}

	public boolean intersects( Rectangle bb ) {
		return bb.overlaps( bounds );
	}
}
