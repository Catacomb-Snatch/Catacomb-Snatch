package net.catacombsnatch.game.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Art {
	public final static String TAG = "[Art]";
	
	public static Skin skin;
	
	public static Texture pyramid;
	public static Texture logo;

	public static TextureRegion[][] lordLard;
	
	public static TextureRegion[] tiles_floor, tiles_walls;

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
			pyramid = load( "art/screen/pyramid.png" );

			// Load characters
			lordLard = cut( "art/player/lord_lard.png", 23, 32 );

			// Load tiles
			tiles_floor = cut("art/tiles/floor.png", 32, 32)[0];
			tiles_walls = cut("art/tiles/walls.png", 32, 56)[0];
			
			// Load extras
			logo = load( "art/logo.png" );
			
			return true;
		} catch ( Exception e ) {
			Gdx.app.error(TAG, "Something went wrong while loading a resource: ", e);
		}

		return false;
	}

	/** Unloads all loaded resources */
	public static void unloadResources() {
		for(int i = 0; i < lordLard.length; i++) {
			dispose(lordLard[i]);
		}
		
		skin.dispose();
		pyramid.dispose();
		logo.dispose();
	}

	private static Texture load( String path ) {
		Texture tex = new Texture( Gdx.files.internal( path ) );
		tex.setFilter( TextureFilter.Nearest, TextureFilter.Nearest );

		return tex;
	}

	private static TextureRegion[][] cut( String path, int w, int h ) {
		Texture tex = load( path );
		return TextureRegion.split( tex, w, h );
	}
	
	private static void dispose(TextureRegion[] tex) {
		for(int i = 0; i < tex.length; i++) {
			tex[i].getTexture().dispose();
		}
	}
}
