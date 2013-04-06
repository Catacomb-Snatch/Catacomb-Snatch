package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TitleScreen extends Scene {
	private int index = 0;
	private final Entity charEntity;
	private final Animated charAnimation; // Reference for quick access

	public TitleScreen() {
		super();
		
		this.setBackground(Art.pyramid);

		addTextButton(Language.get("scene.title.exit"), 0, 0).addAction(new Action() {
			@Override
			public boolean act(float delta) {
				Gdx.app.exit();
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.options"), 0, 0).setDisabled(true);
		addTextButton(Language.get("scene.title.start"), 0, 0).setDisabled(true);
		
		addTextButton(Language.get("scene.title.demo"), 0, 0).addAction(new Action() {
			@Override
			public boolean act(float delta) {
				SceneManager.switchTo(InGameScene.class, true);
				return true;
			}
		});
		
		for(Actor actor : getActors()) {
			actor.setWidth(150);
		}
		
		index = getActors().size-1;
		
		charEntity = new EntityManager().createEntity();
		charAnimation = charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );

		EventManager.registerListener(this);
		update(true);
	}

	@Override
	public void enter() {
		Game.sound.startTitleMusic();
	}
	
	@Override
	public void leave() {
		Game.sound.stopTitleMusic();
	}

	@Override
	public void render() {
		super.render();
		
		getSpriteBatch().draw(Art.logo, (Screen.getWidth() - Art.logo.getWidth()) / 2, Screen.getHeight() - (int) (1.5f * Art.logo.getHeight()));
		
		charAnimation.tick();
		charAnimation.render( this );
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
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
		charAnimation.setPosition((int) (actor.getX() - (Art.lordLard[0][0].getRegionWidth() / 2)), (int) actor.getY());
	}
	
}
