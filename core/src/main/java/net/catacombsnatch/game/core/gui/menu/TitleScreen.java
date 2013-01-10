package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.graphics.Color;

public class TitleScreen extends GuiMenu {
	private int selected = 0;

	public void render( Screen screen ) {
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );

		screen.fill( 10, 10 + selected * 30, 100, 20, Color.RED );
	}

	public boolean scrolled( int amount ) {
		selected += amount;
		return true;
	}
}
