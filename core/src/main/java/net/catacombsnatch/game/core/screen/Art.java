package net.catacombsnatch.game.core.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Art {
	public final static String TAG = "[Art]";
	
	public static Skin skin;
	
	public static Texture background;
	public static Texture titleScreen;

	public static TextureRegion[][] lordLard;

	/**
	 * Loads all the artwork
	 * 
	 * @return True on success, otherwise false
	 */
	public static boolean loadResources() {
		try {
			// Load interface
			skin = new Skin(Gdx.files.internal("art/interface.skin"), new TextureAtlas("art/interface.atlas"));
			
			// Load backgrounds
			background = load( "art/screen/background.png" );
			titleScreen = load( "art/screen/title.png" );

			// Load characters
			lordLard = cut( "art/player/lord_lard.png", 23, 32 );

			return true;
		} catch ( Exception e ) {
			Gdx.app.error(TAG, "Something went wrong while loading a resource: ", e);
		}

		return false;
	}

	/** Unloads all loaded resources */
	public static void unloadResources() {
		background.dispose();
		titleScreen.dispose();
	}

	private static Texture load( String path ) {
		Texture tex = new Texture( Gdx.files.internal( path ) );
		tex.setFilter( TextureFilter.Linear, TextureFilter.Linear );

		return tex;
	}

	private static TextureRegion[][] cut( String path, int w, int h ) {
		Texture tex = load( path );
		return TextureRegion.split( tex, w, h );
	}
}
