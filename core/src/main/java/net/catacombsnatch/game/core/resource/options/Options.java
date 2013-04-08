package net.catacombsnatch.game.core.resource.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class Options extends OptionGroup {
	protected String fileName;
	protected Preferences preferences;
	
	// Default keys
	public final static String DEBUG = "debugMode";
	
	public Options(String fileName) {
		super(null);
		
		this.fileName = fileName;
		load();
		
		// Set defaults
		setDefault(DEBUG, true);
	}
	
	public void save() {
		preferences.flush();
	}

	public void load() {
		preferences = Gdx.app.getPreferences( fileName );
	}
	
}
