package net.catacombsnatch.game.core;

import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class CatacombSnatchGame implements ApplicationListener {
	private Screen screen;

	public void create() {
		if ( !Art.loadResources() ) Gdx.app.exit();

		screen = new Screen();
	}

	public void resize( int width, int height ) {
	}

	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );
		screen.getGraphics().end();
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}
}
