package net.catacombsnatch.game.core.world.level.generator.options;

public class GeneratorStringOption extends GeneratorOption<String> {

	public GeneratorStringOption(String name) {
		super(name);
	}
	
	public GeneratorStringOption(String name, String defaultValue) {
		super(name, defaultValue);
	}

	@Override
	public String parseOption(String option) {
		return option;
	}

}
