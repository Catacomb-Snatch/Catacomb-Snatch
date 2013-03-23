package net.catacombsnatch.game.core.scene;

import java.util.Stack;

import com.badlogic.gdx.Gdx;

public class SceneManager {
	public final static String TAG = "[SceneManager]";
	
	protected static SceneStack scenes;
	
	
	public SceneManager() {
		scenes = new SceneStack();
	}
	
	
	/**
	 * Returns current open scene.
	 * If no scenes have been opened this will return null.
	 * 
	 * @return The current Scene, if any.
	 */
	public static synchronized Scene getCurrent() {
		return scenes.isEmpty() ? null : scenes.peek();
	}
	
	
	/**
	 * Adds and switches to the newly generated scene.
	 * This creates a new scene from the class default constructor to switch to a scene using the
	 * direct instance use ###.
	 * 
	 * @param menu The class of the menu to switch to.
	 * @return The newly generated scene instance.
	 */
	public static synchronized <T extends Scene> T switchTo( Class<T> menu ) {
		T instance = null;

		try {
			// Create new menu instance from class
			instance = menu.newInstance();
			scenes.add(instance);

		} catch ( Exception e ) {
			Gdx.app.log(TAG, "Error switching to menu '" + menu + "': " + e.getMessage());
		}

		return instance;
	}
	
	
	/**
	 * Exits the currently showing scene.
	 */
	public static void exit() {
		scenes.pop();
	}
	
	
	/**
	 * Exits all open scenes.
	 * Please keep in mind that {@link #getCurrent() } will return null!
	 */
	public static void exitAll() {
		// Clear stack until its empty
		while ( !scenes.isEmpty() ) {
			scenes.pop();
		}
	}
	
	protected class SceneStack extends Stack<Scene> {
		private static final long serialVersionUID = 1L;

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

	
}
