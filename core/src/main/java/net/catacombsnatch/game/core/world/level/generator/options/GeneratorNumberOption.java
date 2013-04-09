package net.catacombsnatch.game.core.world.level.generator.options;

import com.badlogic.gdx.Gdx;

public class GeneratorNumberOption extends GeneratorOption<Integer> {

	public GeneratorNumberOption(String name) {
		super(name);
	}

	public GeneratorNumberOption(String name, int defaultValue) {
		super(name, defaultValue);
	}
	
	@Override
	public Integer parseOption(String option) {
		try {
			return Integer.parseInt(option);
			
		} catch(Exception e) {
			Gdx.app.error("GeneratorNumberOption", "Could not parse option value:" + option, e);
			return null;
		}
	}

}
