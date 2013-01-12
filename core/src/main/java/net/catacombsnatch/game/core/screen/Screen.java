package net.catacombsnatch.game.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Screen implements Disposable {
	protected SpriteBatch batch;

	protected int xOffset, yOffset;

	protected Texture rect_tex;

	public Screen() {
		this.batch = new SpriteBatch();

		// Custom texture for rectangles (used by fill())
		Pixmap pmap = new Pixmap( 1, 1, Pixmap.Format.RGBA8888 );
		pmap.setColor( Color.WHITE );
		pmap.fillRectangle( 0, 0, 1, 1 );

		rect_tex = new Texture( pmap, true );

		pmap.dispose();
	}

	/** Clears the screen with black */
	public void clear() {
		clear( Color.BLACK );
	}

	/**
	 * Clears the screen with a given color
	 * 
	 * @param color
	 */
	public void clear( Color color ) {
		Gdx.gl.glClearColor( color.r, color.g, color.b, color.a );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
	}

	/**
	 * Sets the offset for the screen camera
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void setOffset( int xOffset, int yOffset ) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	/**
	 * Fills a region on the screen with a given color
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public void fill( int x, int y, int width, int height, Color color ) {
		Color old = getGraphics().getColor();

		getGraphics().setColor( color );
		getGraphics().draw( rect_tex, x, y, width / 2, height / 2, width, height, 1, 1, 0, 0, 0, 1, 1, false, false );
		getGraphics().setColor( old );
	}

	/**
	 * Draws a sprite to the screen
	 * 
	 * @param sprite
	 */
	public void blit( Sprite sprite ) {
		sprite.draw( batch );
	}

	/**
	 * Draws a sprite to the screen
	 * 
	 * @param sprite
	 * @param x
	 * @param y
	 */
	public void blit( Sprite sprite, int x, int y ) {
		this.blit( sprite, x, y, (int) sprite.getWidth(), (int) sprite.getHeight() );
	}

	/**
	 * Draws a sprite to the screen
	 * 
	 * @param sprite
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void blit( Sprite sprite, int x, int y, int width, int height ) {
		batch.draw( sprite.getTexture(), x, y, width, height );
	}

	/**
	 * Returns the {@link SpriteBatch} used in this screen
	 * 
	 * @return
	 */
	public SpriteBatch getGraphics() {
		return batch;
	}

	/**
	 * Releases all resources of this screen
	 * 
	 */
	public void dispose() {
		batch.dispose();
	}

	/**
	 * Returns the screen width
	 * 
	 * @return
	 */
	public static int getWidth() {
		return Gdx.graphics.getWidth();
	}

	/**
	 * Returns the screen height
	 * 
	 * @return
	 */
	public static int getHeight() {
		return Gdx.graphics.getHeight();
	}
}
