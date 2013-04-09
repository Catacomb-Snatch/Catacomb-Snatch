package net.catacombsnatch.game.core.world.level.generator.options;

import com.badlogic.gdx.Gdx;

public class GeneratorToggleOption extends GeneratorOption<Boolean> {

	public GeneratorToggleOption(String name) {
		super(name);
	}

	public GeneratorToggleOption(String name, boolean defaultValue) {
		super(name, defaultValue);
	}
	
	@Override
	public Boolean parseOption(String option) {
		try {
			return Boolean.parseBoolean(option);
			
		} catch(Exception e) {
			Gdx.app.error("GeneratorToggleOption", "Could not parse option value:" + option, e);
			return null;
		}
	}

}
