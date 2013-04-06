package net.catacombsnatch.game.core.resource.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class Options extends OptionGroup {
	private static final String PREFERENCES_FILENAME = "options.xml";
	private static Preferences preferences;

	
	public Options() {
		super(null);
	}
	
	public static void save() {
		preferences.flush();
	}

	public static void load() {
		preferences = Gdx.app.getPreferences( PREFERENCES_FILENAME );
	}
	
}
