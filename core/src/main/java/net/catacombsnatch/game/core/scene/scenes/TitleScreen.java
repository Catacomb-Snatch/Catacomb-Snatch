package net.catacombsnatch.game.core.scene.scenes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

public class TitleScreen extends Scene {
	private List<String> menu;

	private Entity charEntity;

	public TitleScreen() {
		menu = new ArrayList<String>();
		menu.add(Language.get("scene.title.demo"));
		menu.add(Language.get("scene.title.start"));
		menu.add(Language.get("scene.title.options"));
		menu.add(Language.get("scene.title.exit"));

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

		for ( int s = 0; s < menu.size(); s++ ) {
			Fonts.GOLD.drawMultiLine( screen.getGraphics(), menu.get( s ), Screen.getWidth() / 2, 128 - 24 * s, 0, BitmapFont.HAlignment.CENTER );
		}

		charEntity.getComponent( Animated.class ).render( screen );
	}
}
