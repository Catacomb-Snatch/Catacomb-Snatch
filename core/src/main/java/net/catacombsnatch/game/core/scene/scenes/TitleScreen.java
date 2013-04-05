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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TitleScreen extends Scene {
	private int index = 0;
	private final Entity charEntity;
	private final Animated charAnimation; // Reference for quick access

	public TitleScreen() {
		super();
		
		this.setBackground(Art.pyramid);
		
		String[] buttons = new String[] { "exit", "options", "start", "demo" };
		for(String s : buttons) {
			TextButton tb = addTextButton(Language.get("scene.title." + s), 0, 0);
			tb.setDisabled(true);
			tb.setWidth(150);
		}
		
		((TextButton) getActors().get(0)).setDisabled(false); // Exit
		((TextButton) getActors().get(3)).setDisabled(false); // Demo
		
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
				// TODO
				SceneManager.switchTo(InGameScene.class, true);
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
