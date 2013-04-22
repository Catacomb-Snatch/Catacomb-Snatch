package net.catacombsnatch.game.core.resource.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Options extends OptionGroup {
	protected List<String> changedList;
	
	public Options(String name) {
		this(name, null);
	}

	public Options(String name, OptionGroup parent) {
		this(name, parent, null);
	}
	
	public Options(String name, OptionGroup parent, Map<String, Object> defaults) {
		super(null, name, parent, defaults);
		
		options = this;
	}
	
	protected void addChange(String key) {
		if(changedList == null) changedList = new ArrayList<String>();
		if(!changedList.contains(key)) changedList.add(key);
	}

	public abstract void save();

	public abstract void reload();
	
}
