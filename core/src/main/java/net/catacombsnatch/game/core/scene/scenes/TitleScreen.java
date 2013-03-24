package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

public class TitleScreen extends Scene {
	private Entity charEntity;

	public TitleScreen() {
		super();
		
		this.setBackground(Art.titleScreen);
		
		addTextButton(Language.get("scene.title.demo"), Screen.getWidth() / 2, 250);
		addTextButton(Language.get("scene.title.start"), Screen.getWidth() / 2, 200);
		addTextButton(Language.get("scene.title.options"), Screen.getWidth() / 2, 150);
		addTextButton(Language.get("scene.title.exit"), Screen.getWidth() / 2, 100);
		
		charEntity = new EntityManager().createEntity();
		charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );

		Game.sound.startTitleMusic();
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
}
