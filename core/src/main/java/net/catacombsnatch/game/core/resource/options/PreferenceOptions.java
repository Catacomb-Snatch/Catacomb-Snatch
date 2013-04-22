package net.catacombsnatch.game.core.resource.options;

import java.util.Map;
import java.util.Map.Entry;

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
		if(changedList != null) for (String change : changedList) {
			Object value = get(change);
			
			if(value instanceof Boolean) preferences.putBoolean(change, (Boolean) value);
			else if(value instanceof Float) preferences.putFloat(change, (Float) value);
			else if(value instanceof Integer) preferences.putInteger(change, (Integer) value);
			else if(value instanceof Long) preferences.putLong(change, (Long) value);
			else preferences.putString(change, value.toString());
		}
		
		preferences.flush();
	}

	@Override
	public void reload() {
		preferences = Gdx.app.getPreferences(name);
		
		Map<String, ?> values = preferences.get();
		for(Entry<String, ?> entry : values.entrySet()) {
			// set(entry.getKey(), entry.getValue()); TODO: check for object types
		}
	}

}
