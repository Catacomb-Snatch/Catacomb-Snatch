package net.catacombsnatch.game.scene;

import com.badlogic.gdx.Gdx;
import net.catacombsnatch.game.screen.Screen;
import net.catacombsnatch.game.screen.Updateable;

import java.util.Stack;

public class SceneManager implements Updateable {
    public final static String TAG = "[SceneManager]";

    protected static SceneStack scenes;
    protected static boolean drawAll;

    public SceneManager() {
        scenes = new SceneStack();
        drawAll = false;
    }

    @Override
    public void update(boolean resize) {
        if (resize) {
            for (Scene scene : scenes) {
                update(scene);
            }
        }
    }

    /**
     * Returns the content of all stored scenes.
     *
     * @return A newly generated {@link Scene Scene[]} containing all scene instances.
     */
    public Scene[] getScenes() {
        return scenes.toArray(new Scene[scenes.size()]);
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
     * Adds and switches to the newly generated scene.<br />
     * This creates a new scene from the class default constructor.
     *
     * @param scene The class of the scene to switch to.
     * @return The newly generated scene instance.
     */
    public static synchronized <T extends Scene> T switchTo(Class<T> scene) {
        return switchTo(scene, false);
    }

    /**
     * Adds and switches to the newly generated scene and optionally closes all other ones.<br />
     * This creates a new scene from the class default constructor.
     *
     * @param scene    The class of the scene to switch to.
     * @param closeAll True if all previous scenes should be closed.
     * @return The newly generated scene instance.
     */
    public static synchronized <T extends Scene> T switchTo(Class<T> scene, boolean closeAll) {
        try {
            return switchTo(scene.newInstance(), closeAll);

        } catch (Exception e) {
            Gdx.app.error(TAG, "Error switching to scene '" + scene.getName() + "'", e);
        }

        return null;
    }

    /**
     * Adds and switches to a scene and optionally closes all other ones.<br />
     * <b>This takes an already existing menu instance.</b>
     *
     * @param scene    The scene instance to switch to.
     * @param closeAll True if all previous scenes should be closed.
     * @return The scene instance.
     */
    public static synchronized <T extends Scene> T switchTo(T scene, boolean closeAll) {
        if (closeAll) exitAll();

        scenes.add(scene);
        update(scene);

        return scene;
    }

    /**
     * Exits the currently shown scene (if any).
     */
    public static void exit() {
        scenes.pop();
    }

    /**
     * Exits all open scenes.
     * Please keep in mind that {@link #getCurrent()} will return null afterwards!
     */
    public static void exitAll() {
        // Clear stack until its empty
        while (!scenes.isEmpty()) {
            scenes.pop();
        }
    }

    /**
     * Sets the draw all mode.
     * If set to true this will let the game render all open scenes,
     * instead of just the current (peek) one.
     *
     * @param enable True or false to enable or disable
     */
    public static void setDrawAllEnabled(boolean enable) {
        drawAll = enable;
    }

    /**
     * @return True if the game should render all open scenes (instead of just the peek).
     */
    public static boolean isDrawAllEnabled() {
        return drawAll;
    }

    protected static void update(Scene scene) {
        scene.getViewport().update(Screen.getWidth(), Screen.getHeight(), true);
        scene.update(true);
    }

    protected class SceneStack extends Stack<Scene> {
        private static final long serialVersionUID = 1L;

        public SceneStack() {
            super();
        }

        @Override
        public boolean add(Scene scene) {
            Scene before = null;

            if (!empty()) {
                before = peek();
                if (before != null) before.leave(scene);
            }

            scene.enter(before);
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

                if (top != null) {
                    Scene peek = empty() ? null : peek();

                    top.leave(peek);
                    top.exit();

                    if (peek != null) peek.enter(top);
                }

                return top;
            } catch (Exception e) {
                Gdx.app.error(TAG, "Could not pop scene stack", e);
                return null;
            }
        }
    }

}
