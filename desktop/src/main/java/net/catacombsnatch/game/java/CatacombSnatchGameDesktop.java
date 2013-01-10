package net.catacombsnatch.game.java;

import net.catacombsnatch.game.core.CatacombSnatchGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class CatacombSnatchGameDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		new LwjglApplication(new CatacombSnatchGame(), config);
	}
}
