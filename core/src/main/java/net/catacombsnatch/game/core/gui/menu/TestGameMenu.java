package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Input.Keys;

public class TestGameMenu extends GuiMenu {

	public void render( Screen screen ) {
		// TODO Auto-generated method stub

	}

	public boolean keyDown( int k ) {
		if ( k == Keys.ESCAPE ) {
			Game.exitMenu();
			return true;
		}

		return false;
	}

}
