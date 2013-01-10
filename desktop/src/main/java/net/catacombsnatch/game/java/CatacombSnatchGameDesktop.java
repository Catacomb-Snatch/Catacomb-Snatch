package net.catacombsnatch.game.java;

import net.catacombsnatch.game.core.CatacombSnatchGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CatacombSnatchGameDesktop {
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;

	public static void main( String[] args ) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.title = "Catacomb-Snatch";
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;

		new LwjglApplication( new CatacombSnatchGame(), config );
	}
}
