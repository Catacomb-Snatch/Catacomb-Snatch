package net.catacombsnatch.game.core.gui.menu;

import net.catacombsnatch.game.core.gui.components.GuiComponent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public abstract class GuiMenu extends GuiComponent implements InputProcessor {
	public GuiMenu() {
		Gdx.input.setInputProcessor( this );
	}

	public boolean keyDown( int key ) {
		return false;
	}

	public boolean keyTyped( char c ) {
		return false;
	}

	public boolean keyUp( int key ) {
		return false;
	}

	public boolean mouseMoved( int x, int y ) {
		return false;
	}

	public boolean scrolled( int amount ) {
		return false;
	}

	public boolean touchDown( int x, int y, int pointer, int button ) {
		return false;
	}

	public boolean touchDragged( int x, int y, int pointer ) {
		return false;
	}

	public boolean touchUp( int x, int y, int pointer, int button ) {
		return false;
	}

}
