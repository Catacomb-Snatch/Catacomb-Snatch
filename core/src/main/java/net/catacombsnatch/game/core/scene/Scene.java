package net.catacombsnatch.game.core.scene;

import net.catacombsnatch.game.core.gui.GuiComponent;

public abstract class Scene extends GuiComponent {
	
	/**
	 * Called whenever the scene is getting closed.
	 * This function should be used to dispose resources that are
	 * no longer needed.
	 */
	public void exit() {}
	
}
