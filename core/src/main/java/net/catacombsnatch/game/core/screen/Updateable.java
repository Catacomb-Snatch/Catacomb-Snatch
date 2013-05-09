package net.catacombsnatch.game.core.screen;

/**
 * Interface for classes that respond to screen resizes and updates.
 */
public interface Updateable {

	/**
	 * Called whenever the screen gets updated (by a resize).
	 * 
	 * @param resize True if caused by a screen resize
	 */
	public void update( boolean resize );
	
}
