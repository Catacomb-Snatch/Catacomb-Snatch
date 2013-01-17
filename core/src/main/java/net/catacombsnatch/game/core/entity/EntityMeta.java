package net.catacombsnatch.game.core.entity;

import java.util.HashMap;
import java.util.Map;

public class EntityMeta {
	protected Map<String, Object> data;

	public EntityMeta() {
		data = new HashMap<String, Object>();
	}

	/**
	 * Returns true if an entry with the given key was found
	 * 
	 * @param key The key to check
	 * @return True if found, otherwise false
	 */
	public synchronized boolean has( String key ) {
		return get( key ) != null;
	}

	/**
	 * Returns the associated object stored for the given key
	 * 
	 * @param key The key
	 * @return The stored object
	 */
	public synchronized Object get( String key ) {
		return this.data.get( key );
	}

	/**
	 * Sets a meta data
	 * 
	 * @param key The key
	 * @param value The value
	 */
	public synchronized <T> void set( String key, T value ) {
		this.data.put( key, value );
	}
}
