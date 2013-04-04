package net.catacombsnatch.game.core.screen;

import net.catacombsnatch.game.core.scene.Scene;

public interface Renderable {
	
	/**
	 * Renders content to the scene.
	 * 
	 * @param screen The {@link Scene} to draw on.
	 */
	public abstract void render( Scene scene );
	
}
