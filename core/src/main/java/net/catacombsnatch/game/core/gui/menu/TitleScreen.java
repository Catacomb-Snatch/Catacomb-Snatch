package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TitleScreen extends GuiMenu {
	private int selected = 0;
	private Entity charEntity;

	public TitleScreen() {
		charEntity = new EntityManager().createEntity();
		Animated comp = charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );
		scrolled( 0 );

		Game.soundPlayer.startTitleMusic();
	}

	public void exit() {
		Game.soundPlayer.stopTitleMusic();
	}

	public void render( Screen screen ) {
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );

		Fonts.GOLD.drawMultiLine( screen.getGraphics(), "START GAME", Screen.getWidth() / 2, 82, 0, HAlignment.CENTER );
		Fonts.GOLD.drawMultiLine( screen.getGraphics(), "OPTIONS", Screen.getWidth() / 2, 66, 0, HAlignment.CENTER );
		Fonts.GOLD.drawMultiLine( screen.getGraphics(), "EXIT GAME", Screen.getWidth() / 2, 50, 0, HAlignment.CENTER );

		Fonts.GOLD.draw( screen.getGraphics(), Integer.toString( Gdx.graphics.getFramesPerSecond() ) + " FPS", 2, 2 );

		charEntity.getComponent( Animated.class ).render( screen );
	}

	public boolean scrolled( int amount ) {
		selected -= amount;

		if ( selected > 2 )
			selected = 0;
		else if ( selected < 0 ) selected = 2;

		charEntity.getComponent( Animated.class ).setPosition( (Screen.getWidth() - 200) / 2, 82 - 8 + (-selected * 16) );

		return true;
	}
}
