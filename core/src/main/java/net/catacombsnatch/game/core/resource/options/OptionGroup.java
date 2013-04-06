package net.catacombsnatch.game.core.resource.options;


/** Represents an option section */
public class OptionGroup {
	protected OptionGroup parent;
	protected String name;
	
	
	public OptionGroup(String name) {
		this(name, null);
	}
	
	public OptionGroup(String name, OptionGroup parent) {
		this.name = name;
		this.parent = parent;
	}
	
	
	/* ------------ Grouping related functions ------------ */
	
	public OptionGroup getGroup(String name) {
		return null; // TODO
	}
	
	public OptionGroup createGroup(String name) {
		OptionGroup group = new OptionGroup(name, this);
		
		// TODO
		
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
	
	public boolean isSet() {
		return true; // TODO
	}
	
	public Object get(String key) {
		return null; // TODO
	}
	
	public <T> T get(String key, Class<T> type) {
		return null; // TODO
	}
	
	public <T> T get(String key, T def) {
		return null; // TODO
	}
	
	public <T> T get(String key, T def, Class<T> type) {
		return null; // TODO
	}
	
	public void set(String key, Object value) {
		// TODO
	}
	
}
