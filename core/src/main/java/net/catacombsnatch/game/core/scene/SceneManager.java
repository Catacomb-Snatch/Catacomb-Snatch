package net.catacombsnatch.game.core.scene;

import java.util.Stack;

import com.badlogic.gdx.Gdx;

public class SceneManager {
	public final static String TAG = "[SceneManager]";
	
	protected static SceneStack scenes;
	
	
	public SceneManager() {
		scenes = new SceneStack();
	}
	
	public void resize() {
		Scene current = getCurrent();
		if(current != null) current.update(true);
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
	 * This creates a new scene from the class default constructor.
	 * 
	 * @param menu The class of the menu to switch to.
	 * @return The newly generated scene instance.
	 */
	public static synchronized <T extends Scene> T switchTo( Class<T> menu ) {
		return switchTo(menu, false);
	}
	
	/**
	 * Adds and switches to the newly generated scene and optionally closes all other ones.
	 * This creates a new scene from the class default constructor.
	 * 
	 * @param menu The class of the menu to switch to.
	 * @param closeAll True if all previous scenes should be closed.
	 * @return The newly generated scene instance.
	 */
	public static synchronized <T extends Scene> T switchTo( Class<T> menu, boolean closeAll ) {
		if(closeAll) exitAll();
		
		T instance = null;

		try {
			// Create new menu instance from class
			instance = menu.newInstance();
			scenes.add(instance);

		} catch ( Exception e ) {
			Gdx.app.error(TAG, "Error switching to menu '" + menu + "'", e);
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

		@Override
		public boolean add(Scene scene) {
			if(!empty()) {
				Scene peek = super.peek();
				if(peek != null) peek.leave();
			}
			
			scene.enter();
			return super.add(scene);
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
				top.leave();
				top.exit();

				return top;
			} catch ( Exception e ) {
				e.printStackTrace();
				return null;
			}
		}
	}

	
}
