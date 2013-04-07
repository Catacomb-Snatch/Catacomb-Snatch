package net.catacombsnatch.game.core.resource.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class Options extends OptionGroup {
	private static final String PREFERENCES_FILENAME = "options.xml";
	private static Preferences preferences;
	private static Options instance;
	
	// Default keys
	public final static String DEBUG = "debugMode";
	
	public Options() {
		super(null);
		
		// Set defaults
		set(DEBUG, true);
		
		if (instance == null) {
			instance = this;
		}
	}
	
	public static Options getOptions() {
		return instance;
	}
	
	public static void save() {
		preferences.flush();
	}

	public static void load() {
		if(instance == null) {
			instance = new Options();
		}
		
		preferences = Gdx.app.getPreferences( PREFERENCES_FILENAME );
	}
	
}
