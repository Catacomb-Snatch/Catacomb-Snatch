package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.KeyPressedEvent;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class TitleScreen extends Scene {
	private int index = 0;
	private final Entity charEntity;
	private final Animated charAnimation; // Reference for quick access

	public TitleScreen() {
		super();
		
		this.setBackground(Art.pyramid);
		
		addTextButton(Language.get("scene.title.exit"), 0, 0).setWidth(150);
		addTextButton(Language.get("scene.title.options"), 0, 0).setWidth(150);
		addTextButton(Language.get("scene.title.start"), 0, 0).setWidth(150);
		addTextButton(Language.get("scene.title.demo"), 0, 0).setWidth(150);
		
		charEntity = new EntityManager().createEntity();
		charAnimation = charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );

		EventManager.registerListener(this);
		update(true);
		
		Game.sound.startTitleMusic();
	}

	@Override
	public void exit() {
		Game.sound.stopTitleMusic();
	}

	@Override
	public void render( Screen screen ) {
		super.render(screen);
		
		screen.getGraphics().draw(Art.logo, (Screen.getWidth() - Art.logo.getWidth()) / 2, Screen.getHeight() - (int) (1.5f * Art.logo.getHeight()));
		
		charAnimation.render( screen );
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		index--;
		if(index < 0) index = actors.size - 1;
		
		update(false);
	}
	
	@Override
	public void update(boolean resize) {
		if(resize) {
			int x = (Screen.getWidth() - 150) / 2, p = 40;
			for(int i = 0; i < actors.size; i++) {
				actors.get(i).setPosition(x, p + (p * i));
			}
		}
		
		Actor actor = actors.get(index);
		charAnimation.setPosition((int) (actor.getX() - (Art.lordLard[0][0].getRegionWidth() / 2)), (int) actor.getY());
	}
	
}
