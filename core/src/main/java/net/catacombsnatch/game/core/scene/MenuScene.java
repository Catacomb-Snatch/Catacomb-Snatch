package net.catacombsnatch.game.core.scene;

import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class MenuScene extends Scene {
	private final Animation ani;
	private float tick;
	private int aniX, aniY;
	
	protected int index = 0;
	
	
	public MenuScene(Texture bg) {
		super();
		
		// Set background texture
		setBackground(bg);
		
		// Add animated character cursor
		ani = new Animation(0.05f, Art.lordLard[0]);
	}
	
	protected void init() {
		// Set actor width
		for(Actor actor : getActors()) {
			actor.setWidth(150);
		}
		
		// Set index to the topmost actor
		index = getActors().size-1;
		
		// Properly place actors
		update(true);
	}
	
	protected void drawCharacter() {
		tick += Gdx.graphics.getDeltaTime();
		getSpriteBatch().draw( ani.getKeyFrame( tick, true ), aniX, aniY );
	}
	

	@EventHandler
	public void key(KeyReleaseEvent event) {
		switch(event.getKey()) {
			case MOVE_DOWN:
				index--;
				if(index < 0) index = getActors().size - 1;
				break;
				
			case MOVE_UP:
				index++;
				if(index >= getActors().size) index = 0;
				break;
			
			case USE:
				Actor actor = getActors().get(index);
				if(actor != null) actor.act(Gdx.graphics.getDeltaTime());
				break;
				
			default:
				// Nothing to do here
		}
		
		update(false);
	}
	
	@Override
	public void update(boolean resize) {
		if(resize) {
			int x = (Screen.getWidth() - 150) / 2, p = 40;
			for(int i = 0; i < getActors().size; i++) {
				getActors().get(i).setPosition(x, p + (p * i));
			}
		}
		
		Actor actor = getActors().get(index);
		aniX = (int) (actor.getX() - (Art.lordLard[0][0].getRegionWidth() / 2));
		aniY = (int) (actor.getY());
	}
	
}
