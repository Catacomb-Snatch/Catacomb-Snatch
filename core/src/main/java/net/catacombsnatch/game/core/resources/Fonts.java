package net.catacombsnatch.game.core.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Fonts {
	public static BitmapFont GOLD;

	public static void init() {
		GOLD = load( "gold" );
	}

	private static BitmapFont load( String name ) {
		return new BitmapFont( Gdx.files.internal( "fonts/" + name + ".fnt" ), Gdx.files.internal( "fonts/" + name + ".png" ), false );
	}

	public static void unload() {
		GOLD.dispose();
	}
}
