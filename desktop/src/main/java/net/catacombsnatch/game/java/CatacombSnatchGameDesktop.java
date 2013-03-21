package net.catacombsnatch.game.java;

import net.catacombsnatch.game.core.Game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CatacombSnatchGameDesktop {
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;
	
	protected static LwjglApplication game;

	public static void main( String[] args ) {
		System.out.println("Starting game in DESKTOP mode!");
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.title = "Catacomb-Snatch";
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;

		game = new LwjglApplication( new Game(), config );
	}
}
