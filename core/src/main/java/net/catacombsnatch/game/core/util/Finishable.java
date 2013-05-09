package net.catacombsnatch.game.core.util;

public interface Finishable {
	
	/**
	 * Sets whether or not the object has finished.
	 * 
	 * @param finished True if finished, false if not
	 */
	public void setFinished(boolean finished);
	
	/**
	 * @return True if finished, otherwise false
	 */
	public boolean hasFinished();
	
}
