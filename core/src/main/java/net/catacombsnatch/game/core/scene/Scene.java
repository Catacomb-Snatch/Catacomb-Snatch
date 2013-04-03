package net.catacombsnatch.game.core.scene;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

public class Scene implements Renderable {
	/** The background image */
	protected Texture background;
	
	/** Holds all drawable objects (actors) */
	protected Array<Actor> actors;
	
	
	public Scene() {
		actors = new Array<Actor>();
	}
	
	/**
	 * Called whenever the scene is getting closed.
	 * This function should be used to dispose resources that are
	 * no longer needed.
	 */
	public void exit() {}
	
	/**
	 * Called whenever the screen should get updated
	 * 
	 * @param update If update has been called through a resize
	 */
	public void update(boolean resize) {}
	
	@Override
	public void render(Screen screen) {
		// Draw background
		if(background != null) {
			screen.getGraphics().draw(background, 0, 0, Screen.getWidth(), Screen.getHeight());
		}
		
		// Draw actors
		for(Actor actor : actors) {
			actor.draw(screen.getGraphics(), 1);
		}
	}
	
	/**
	 * Sets the scene background
	 * 
	 * @param background The background texture to set
	 */
	public void setBackground(Texture background) {
		this.background = background;
	}
	
	/**
	 * Adds an actor to the scene
	 * 
	 * @param actor The actor to add
	 */
	public Actor addActor(Actor actor) {
		actors.add(actor);
		return actor;
	}
	
	/**
	 * Adds a new text button
	 * 
	 * @param text The text it contains
	 * @param x The x-position
	 * @param y The y-position
	 * @return The created {@link TextButton}
	 */
	public TextButton addTextButton(String text, int x, int y) {
		TextButton button = new TextButton(text, Art.skin);
		button.setPosition(x, y);
		return (TextButton) addActor(button);
	}
	
}
