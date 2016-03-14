package net.catacombsnatch.game.screen;

import net.catacombsnatch.game.scene.Scene;

public interface Renderable {

    /**
     * Renders content to the scene.
     *
     * @param scene The {@link Scene} to draw on.
     */
    void render(Scene scene);

}
