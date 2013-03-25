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

public class TitleScreen extends Scene {
	private Entity charEntity;

	public TitleScreen() {
		super();
		
		this.setBackground(Art.titleScreen);
		
		int w = 150, x = (Screen.getWidth() - w) / 2, i = 40;
		addTextButton(Language.get("scene.title.demo"), x, i * 4).setWidth(w);
		addTextButton(Language.get("scene.title.start"), x, i * 3).setWidth(w);
		addTextButton(Language.get("scene.title.options"), x, i * 2).setWidth(w);
		addTextButton(Language.get("scene.title.exit"), x, i).setWidth(w);
		
		charEntity = new EntityManager().createEntity();
		charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );

		Game.sound.startTitleMusic();
		
		EventManager.registerListener(this);
	}

	@Override
	public void exit() {
		Game.sound.stopTitleMusic();
	}

	@Override
	public void render( Screen screen ) {
		super.render(screen);
		
		charEntity.getComponent( Animated.class ).render( screen );
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		System.out.println(event.getKey().name());
	}
	
}
