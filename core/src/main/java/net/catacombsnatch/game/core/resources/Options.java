package net.catacombsnatch.game.core.resources;

import net.catacombsnatch.game.core.entity.GameCharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class Options {

	/* KEYS for common options */
	public static final String DRAW_FPS = "drawFps";
	public static final String TRAP_MOUSE = "trapMouse";
	public static final String FULLSCREEN = "fullscreen";
	public static final String MUSIC = "music";
	public static final String SOUND = "sound";
	public static final String VOLUME = "volume";

	public static final String CREATIVE = "creative";
	public static final String CHARACTER_ID = "character";

	public static final String MP_PORT = "mpPort";

	public static final String LOCALE = "locale";
	public static final String SCALE = "scale";

	public static final String DLSYSTEM = "dlsystem";
	public static final String DLBUFFERSIZE = "dlbuffer";
	public static final String ENABLE_MODS = "enableMods";

	private static final String PREFERENCES_FILENAME = "options.xml";
	private static Preferences preferences;

	public static boolean contains( String key ) {
		return preferences.contains( key );
	}

	public static boolean isSet( String key ) {
		return contains( key ) && preferences.getString( key ) != null;
	}

	public static String get( String key ) {
		return preferences.getString( key );
	}

	public static String get( String key, String defaultValue ) {
		return preferences.getString( key, defaultValue );
	}

	public static boolean getBoolean( String key ) {
		return preferences.getBoolean( key );
	}

	public static boolean getBoolean( String key, boolean defaultValue ) {
		return preferences.getBoolean( key, defaultValue );
	}

	public static float getFloat( String key ) {
		return preferences.getFloat( key );
	}

	public static float getFloat( String key, float defaultValue ) {
		return preferences.getFloat( key, defaultValue );
	}

	public static int getInteger( String key ) {
		return preferences.getInteger( key );
	}

	public static int getInteger( String key, int defaultValue ) {
		return preferences.getInteger( key, defaultValue );
	}

	public static long getLong( String key ) {
		return preferences.getLong( key );
	}

	public static long getLong( String key, long defaultValue ) {
		return preferences.getLong( key, defaultValue );
	}

	public static int getCharacterID() {
		if ( !isSet( CHARACTER_ID ) ) { return GameCharacter.LordLard.ordinal(); }

		int id = preferences.getInteger( CHARACTER_ID );

		if ( id < 0 || id >= GameCharacter.values().length - 1 ) { return GameCharacter.LordLard.ordinal(); }

		return id;
	}

	public static void set( String key, String value ) {
		preferences.putString( key, value );
	}

	public static void set( String key, boolean value ) {
		preferences.putBoolean( key, value );
	}

	public static void set( String key, float value ) {
		preferences.putFloat( key, value );
	}

	public static void set( String key, int value ) {
		preferences.putInteger( key, value );
	}

	public static void set( String key, long value ) {
		preferences.putLong( key, value );
	}

	public static void save() {
		preferences.flush();
	}

	public static void load() {
		preferences = Gdx.app.getPreferences( PREFERENCES_FILENAME );
	}
}
