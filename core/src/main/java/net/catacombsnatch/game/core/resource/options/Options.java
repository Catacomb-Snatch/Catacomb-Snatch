package net.catacombsnatch.game.core.resource.options;

import java.util.Map;

public abstract class Options extends OptionGroup {

	public Options(String name) {
		super(name);
	}

	public Options(String name, OptionGroup parent) {
		super(name, parent);
	}
	
	public Options(String name, OptionGroup parent, Map<String, Object> defaults) {
		super(name, parent, defaults);
	}


	public abstract void save();

	public abstract void reload();
	
}
