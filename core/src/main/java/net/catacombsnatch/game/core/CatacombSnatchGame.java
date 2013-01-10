package net.catacombsnatch.game.core;

import net.catacombsnatch.game.core.gui.MenuStack;
import net.catacombsnatch.game.core.gui.menu.TitleScreen;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class CatacombSnatchGame implements ApplicationListener {
	private Screen screen;
	private MenuStack menuStack;

	public void create() {
		if ( !Art.loadResources() ) Gdx.app.exit();

		screen = new Screen();
		menuStack = new MenuStack();

		menuStack.add( new TitleScreen() );
	}

	public void resize( int width, int height ) {
	}

	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );

		if ( !menuStack.isEmpty() ) menuStack.peek().render( screen );

		screen.getGraphics().end();
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		Art.unloadResources();
	}
}
