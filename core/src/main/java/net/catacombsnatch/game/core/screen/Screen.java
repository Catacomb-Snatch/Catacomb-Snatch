package net.catacombsnatch.game.core.screen;

import net.catacombsnatch.game.core.resources.Options;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;

public class Screen {
	protected static int SCALE = 1;
	static {
		setClearColor(Color.BLACK);
	}
	
	public static void resize(int width, int height) {
		if(Options.getBoolean(Options.SCALE, true)) {
			if(width > 800 && height > 600) SCALE = 2;
			else SCALE = 1;
		}
		
		Scene current = SceneManager.getCurrent();
		if(current != null) current.setViewport(width / SCALE, height / SCALE, true);
	}

	/** Clears the screen with black */
	public static void clear() {
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
	}

	/**
	 * Sets the clear color of the screen
	 * 
	 * @param color The color used to clear the screen
	 */
	public static void setClearColor( Color color ) {
		Gdx.gl.glClearColor( color.r, color.g, color.b, color.a );
	}

	/** @return The screen width */
	public static int getWidth() {
		return Gdx.graphics.getWidth() / SCALE;
	}

	/** @return The screen height */
	public static int getHeight() {
		return Gdx.graphics.getHeight() / SCALE;
	}
}
