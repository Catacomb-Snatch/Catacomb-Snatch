package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TitleScreen extends GuiMenu {
	private int selected = 0;

	public TitleScreen() {
		Game.soundPlayer.startTitleMusic();
	}

	public void exit() {
		Game.soundPlayer.stopTitleMusic();
	}

	public void render( Screen screen ) {
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );
		screen.fill( Screen.getWidth() / 2 - 50, Screen.getHeight() / 2 - selected * 30, 100, 20, Color.BLACK );

		Fonts.GOLD.drawMultiLine( screen.getGraphics(), "EXIT GAME", Screen.getWidth() / 2, 50, 0, HAlignment.CENTER );
		Fonts.GOLD.draw( screen.getGraphics(), Integer.toString( Gdx.graphics.getFramesPerSecond() ) + " FPS", 2, 2 );
	}

	public boolean scrolled( int amount ) {
		selected += amount;
		return true;
	}
}
