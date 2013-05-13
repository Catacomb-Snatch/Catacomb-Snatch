package net.catacombsnatch.game.core.screen;

import java.nio.ByteBuffer;

import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

public class Screen {
	protected static int SCALE = 1;
	static {
		setClearColor(Color.BLACK);
	}
	
	public static void resize(int width, int height) {
		if(width > 800 && height > 600) SCALE = 2;
		else SCALE = 1;
		
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
	
	/**
	 * Saves the current screen into a file.
	 * @param file File where the screen will be saved to.
	 */
	public static void saveScreenshot(FileHandle file) {
		Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		PixmapIO.writePNG(file, pixmap);
        pixmap.dispose();
	}
	
	/**
	 * Get a part of the screen as {@link Pixmap}.
	 * @param x section X
	 * @param y section Y
	 * @param w section W
	 * @param h section H
	 * @param flipY true (recommended) if segment should be returned flipped on Y axis
	 * @return A {@link Pixmap} containing the pixel data of the segment
	 */
	public static Pixmap getScreenshot(int x, int y, int w, int h, boolean flipY) {
		Gdx.gl.glPixelStorei(GL10.GL_PACK_ALIGNMENT, 1);
		
		final Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
		byte[] lines = new byte[w * h * 4];
		
		ByteBuffer pixels = pixmap.getPixels();
		Gdx.gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, pixels);
		
		if (flipY) {
			final int numBytesPerLine = w * 4;
			
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			
			pixels.clear();
			pixels.put(lines);
			
    	} else {
    		pixels.clear();
    		pixels.get(lines);
    	}
		
		return pixmap;
	}
	
	/**
	 * Creates a new raw 1x1 texture for background images.
	 * 
	 * @param color The color used to fill the one and only pixel
	 * @return The newly generated Texture
	 */
	public static Texture createBlank(Color color) {
		Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
		
		pm.setColor(color.r, color.g, color.b, color.a);
		pm.drawPixel(0, 0);
		
		Texture tex = new Texture(pm);
		pm.dispose();
		
		return tex;
	}
	
}
