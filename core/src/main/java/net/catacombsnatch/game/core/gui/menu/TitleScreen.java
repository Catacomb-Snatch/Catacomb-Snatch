package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TitleScreen extends GuiMenu {
	private BitmapFont font;
	private int selected = 0;

	public TitleScreen() {
		font = new BitmapFont();
	}

	public void exit() {
		font.dispose();
	}

	public void render( Screen screen ) {
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );
		screen.fill( Screen.getWidth() / 2 - 50, Screen.getHeight() / 2 - selected * 30, 100, 20, Color.BLACK );

		font.drawMultiLine( screen.getGraphics(), "Exit game", Screen.getWidth() / 2, 50, 0, HAlignment.CENTER );
		font.draw( screen.getGraphics(), Integer.toString( Gdx.graphics.getFramesPerSecond() ) + " FPS", 2, Screen.getHeight() - 2 );
	}

	public boolean scrolled( int amount ) {
		selected += amount;
		return true;
	}
}
