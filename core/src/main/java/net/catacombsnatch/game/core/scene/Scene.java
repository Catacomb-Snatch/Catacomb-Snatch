package net.catacombsnatch.game.core.scene;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Scene extends Stage {
	/** The background image */
	protected Texture background;

	/**
	 * Called whenever this scene is getting created (or opened again).
	 * For creation only actions use the constructor.
	 */
	public void enter() {
		// Nothing to do in here
	}
	
	/**
	 * Called when the game switches to a new scene while the old one
	 * still remains "open".
	 */
	public void leave() {
		// Nothing to do in here
	}
	
	/**
	 * Called whenever the scene is getting closed.
	 * This function should be used to dispose resources that are
	 * no longer needed.
	 */
	public void exit() {
		this.dispose();
	}
	
	/**
	 * Called whenever the screen should get updated
	 * 
	 * @param update If update has been called through a resize
	 */
	public void update(boolean resize) {}
	
	public void render() {
		// Draw background
		if(background != null) {
			getSpriteBatch().begin();
			getSpriteBatch().draw(background, 0, 0, Screen.getWidth(), Screen.getHeight());
			getSpriteBatch().end();
		}
		
		super.draw();
		getSpriteBatch().begin();
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
	 * Adds a new text button
	 * 
	 * @param text The text it contains
	 * @param x The x-position
	 * @param y The y-position
	 * @return The created {@link TextButton}
	 */
	public TextButton addTextButton(String text, int x, int y) {
		final TextButton button = new TextButton(text, Art.skin);
		button.setPosition(x, y);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				button.act(Gdx.graphics.getDeltaTime());
			}
		});
		addActor(button);
		
		return button;
	}
	
	public TextButton getTextButton(int index) {
		int size = getActors().size;
		for(int i = 0; i < size; i++) {
			Actor actor = getActors().get(i);
			if(actor instanceof TextButton && i == index) return (TextButton) actor;
		}
		
		return null;
	}
	
}
