package net.catacombsnatch.game.scene;

import net.catacombsnatch.game.event.EventManager;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.screen.Screen;
import net.catacombsnatch.game.screen.Tickable;
import net.catacombsnatch.game.screen.Updateable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Scene extends Stage implements Updateable, Tickable {
	/** The background image */
	protected Texture background;
	
	private boolean exit = false;
	private boolean drawBackground = true;
	private final Rectangle currentActorRect = new Rectangle();
	private Vector2 mousePos = new Vector2();
	
	/**
	 * Called whenever this scene is getting created (or opened again).
	 * For creation only actions use the constructor.
	 * 
	 * @param scene The {@link Scene} that was previously open.
	 */
	public void enter(Scene scene) {
		EventManager.registerListener(this);
	}
	
	/**
	 * Called when the game switches to a new scene while the old one
	 * still remains "open".
	 * 
	 * @param scene The {@link Scene} being switched to.
	 */
	public void leave(Scene scene) {
		EventManager.unregisterListener(this);
	}
	
	/**
	 * Called whenever the scene is getting closed.
	 * This function should be used to dispose resources that are
	 * no longer needed.
	 */
	public void exit() {
		this.dispose();
		exit = true;
	}
	
	@Override
	public void update(boolean resize) {}
	
	@Override
	public void tick(float delta) {
		// Draw background
		if(drawBackground && background != null) {
			getBatch().begin();
			getBatch().draw(background, 0, 0, Screen.getWidth(), Screen.getHeight());
			getBatch().end();
		}
		
		// Similar to Google's Android "Project Butter":
		// Updating mouse dependent stuff in render() 
		if (!Gdx.input.isTouched()) {
			mousePos = screenToStageCoordinates(mousePos.set(Gdx.input.getX(), Gdx.input.getY()));
			
			for (Actor a : getActors()) {
				currentActorRect.set(a.getX(), a.getY(), a.getWidth(), a.getHeight());
				
				for (EventListener el : a.getListeners()) {
					if (!(el instanceof InputListener)) continue;
					
					InputListener il = (InputListener) el;
					if (currentActorRect.contains(mousePos.x, mousePos.y)) {
						il.enter(null, mousePos.x, mousePos.y, -1, null);
					} else {
						il.exit(null, mousePos.x, mousePos.y, -1, null);
					}
				}
			}
		}
		
		super.draw();
		getBatch().begin();
	}
	
	/**
	 * Sets the scene background
	 * 
	 * @param background The background texture to set
	 */
	public void setBackground(Texture background) {
		this.background = background;
	}
	
	public boolean shouldDrawBackground() {
		return drawBackground;
	}
	
	public void setDrawBackground(boolean draw) {
		this.drawBackground = draw;
	}
	
	public boolean hasBeenClosed() {
		return exit;
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
