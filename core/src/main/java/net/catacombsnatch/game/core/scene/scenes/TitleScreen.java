package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TitleScreen extends Scene {
	private List<TextButton> menu;

	private Entity charEntity;

	public TitleScreen() {
		menu = new ArrayList<TextButton>();
		menu.add(new TextButton(Language.get("scene.title.demo"), Art.skin));
		menu.add(new TextButton(Language.get("scene.title.start"), Art.skin));
		menu.add(new TextButton(Language.get("scene.title.options"), Art.skin));
		menu.add(new TextButton(Language.get("scene.title.exit"), Art.skin));

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
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );

		for(TextButton button : menu) {
			button.draw(screen.getGraphics(), 1);
		}

		charEntity.getComponent( Animated.class ).render( screen );
	}
}
