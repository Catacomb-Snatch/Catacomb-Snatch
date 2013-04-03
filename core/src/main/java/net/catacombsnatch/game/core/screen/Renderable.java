package net.catacombsnatch.game.core.screen;

public interface Renderable {
	
	/**
	 * Renders content to the screen.
	 * 
	 * @param screen The {@link Screen} to draw on.
	 */
	public abstract void render( Screen screen );
	
}
