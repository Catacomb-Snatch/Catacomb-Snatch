package net.catacombsnatch.game.core.scene;

import java.util.Stack;

public class SceneStack extends Stack<Scene> {
	public SceneStack() {
		super();
	}

	/**
	 * Attempts to pop the top menu off the stack
	 * 
	 * @return The menu that was popped or null otherwise
	 */
	@Override
	public Scene pop() {
		try {
			Scene top = super.pop();
			top.exit();

			return top;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}
