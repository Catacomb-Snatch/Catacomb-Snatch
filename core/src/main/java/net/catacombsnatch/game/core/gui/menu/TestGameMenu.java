package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.Difficulty;
import net.catacombsnatch.game.core.world.World;

import com.badlogic.gdx.Input.Keys;

public class TestGameMenu extends GuiMenu {
	protected World world;

	public TestGameMenu() {
		this.world = new World( Difficulty.EASY );
	}

	public void render( Screen screen ) {
	}

	public boolean keyDown( int k ) {
		if ( k == Keys.ESCAPE ) {
			Game.exitMenu();
			return true;
		}

		return false;
	}

}
