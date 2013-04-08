package net.catacombsnatch.game.core.resource.options;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/** Represents an option section */
public class OptionGroup {
	protected OptionGroup parent;
	protected String name;
	
	protected Map<String, Object> map;
	
	
	public OptionGroup(String name) {
		this(name, null);
	}
	
	public OptionGroup(String name, OptionGroup parent) {
		this(name, parent, new HashMap<String, Object>());
	}
	
	public OptionGroup(String name, OptionGroup parent, Map<String, Object> defaults) {
		this.name = name;
		this.parent = parent;
		
		map = defaults;
	}
	
	
	/* ------------ Grouping related functions ------------ */
	
	public OptionGroup getGroup(String name) {
		OptionGroup current = this, group = null;
		
		String[] split = name.split(".");
		for(String s : split) {
			group = current.getGroup(s);
			if(group == null) return null;
		}
		
		return group;
	}
	
	public OptionGroup createGroup(String name) {
		return createGroup(name, new HashMap<String, Object>());
	}
	
	public OptionGroup createGroup(String name, Map<String, Object> defaults) {
		OptionGroup group = new OptionGroup(name, this, defaults);
		group.map.put(name, group);
		
		return group;
	}
	
	public OptionGroup getParent() {
		return parent;
	}
	
	/** @return The topmost {@link OptionGroup} */
	public OptionGroup getRoot() {
		OptionGroup root = this;
		
		while(true) {
			if(root.getParent() == null) break;
			
			root = root.getParent();
		}
		
		return root;
	}
	
	/**
	 * Gets the path from this current {@link OptionGroup} up to its root.
	 * <p>
	 * The groups are separated by a <code>'.'</code> character.
	 * Example: <code>root.child.child</code>
	 * 
	 * @return A string representation of the path
	 */
	public String getCurrentPath() {
		String path = getName();
		
		OptionGroup og = this;
		if(og == getRoot()) return "";
		
		while(og != null) {
			og = og.getParent();
			
			if(og != null) path += ".";
			path += og.getName() != null ? og.getName() : "";
		}
		
		return path;
	}
	
	/**
	 * Gets the name of this individual {@link OptionGroup}.
	 * If this is a root element, this returns null.
	 * 
	 * @return The group name
	 */
	public String getName() {
		return name;
	}

	
	/* ------------ Value / Key related functions ------------ */

	public Set<String> getKeys() {
		return getKeys(true);
	}
	
	public Set<String> getKeys(boolean deep) {
		Set<String> keys = map.keySet();
		
		if(deep) for(Entry<String, Object> e: map.entrySet()) {
			if(!(e.getValue() instanceof OptionGroup)) continue;
			
			keys.addAll(((OptionGroup) e.getValue()).getKeys(true));
		}
		
		return keys;
	}
	
	public boolean isSet(String key) {
		OptionGroup group = getGroup(key);
		if(group != null) {
			return group.map.containsKey(key);
		}
		
		return false;
	}
	
	public Object get(String key) {
		return get(key, null);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type) {
		return (T) get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, T def) {
		OptionGroup group = getGroup(key);
		if(group != null) {
			Object obj = group.map.get(key);
			if(obj != null) return (T) obj; 
		}
		
		return def;
	}
	
	public boolean set(String key, Object value) {
		OptionGroup group = getGroup(key);
		if(group != null) {
			group.map.put(key, value);
			return true;
		}
		
		return false;
	}
	
	public boolean setDefault(String key, Object value) {
		OptionGroup group = getGroup(key);
		if(group != null) {
			if(!group.map.containsKey(key)) group.map.put(key, value);
			return true;
		}
		
		return false;
	}
	
}
