package net.catacombsnatch.game.core.resource.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferenceOptions extends Options {
	protected String name;
	protected Preferences preferences;
	

	public PreferenceOptions(String name) {
		super(null);
		
		this.name = name;
		
		reload();
	}
	
	@Override
	public void save() {
		preferences.flush();
	}


	@Override
	public void reload() {
		preferences = Gdx.app.getPreferences(name);
	}

}
