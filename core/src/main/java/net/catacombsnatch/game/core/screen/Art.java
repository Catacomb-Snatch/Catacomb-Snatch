package net.catacombsnatch.game.core.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {
	public static Texture background;
	public static Texture titleScreen;

	/**
	 * Loads all the artwork
	 * 
	 * @return True on success, otherwise false
	 */
	public static boolean loadResources() {
		try {
			background = load( "art/screen/background.png" );
			titleScreen = load( "art/screen/title.png" );

			return true;
		} catch ( Exception e ) {
			System.err.println( "Something went wrong while loading a resource: " + e.getMessage() );
		}

		return false;
	}

	private static Texture load( String path ) {
		Texture tex = new Texture( path );
		tex.setFilter( TextureFilter.Linear, TextureFilter.Linear );

		return tex;
	}

	private static TextureRegion load( String path, int x, int y, int w, int h ) {
		return new TextureRegion( load( path ), x, y, w, h );
	}
}
