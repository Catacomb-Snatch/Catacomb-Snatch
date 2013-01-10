package net.catacombsnatch.game.core.gui;

import java.util.Stack;

import net.catacombsnatch.game.core.gui.menu.GuiMenu;

public class MenuStack extends Stack<GuiMenu> {
	private static final long serialVersionUID = 1L;

	public MenuStack() {
		super();
	}

	/**
	 * Attempts to pop the top menu off the stack
	 * 
	 * @return The menu that was popped or null otherwise
	 */
	public GuiMenu pop() {
		try {
			GuiMenu menu = super.pop();
			menu.exit();

			return menu;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}
