package net.catacombsnatch.game.core.entity;

import java.util.HashMap;
import java.util.Map;

public class EntityMeta {
	protected Map<String, Object> data;

	public EntityMeta() {
		data = new HashMap<String, Object>();
	}

	public boolean has( String key ) {
		return get( key ) != null;
	}

	public Object get( String key ) {
		return this.data.get( key );
	}

	public <T> void set( String key, T value ) {
		this.data.put( key, value );
	}
}
