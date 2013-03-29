package net.catacombsnatch.game.core.event.input.map;

import java.util.EnumMap;

import net.catacombsnatch.game.core.event.input.InputEvent.InputSource;
import net.catacombsnatch.game.core.event.input.Key;

public class KeyMap {
	protected InputSource source;
	protected EnumMap<Key, Integer> map;
	
	public KeyMap(InputSource source) {
		map = new EnumMap<Key, Integer>(Key.class);
	}
	
	public int getFor(Key key) {
		return map.get(key);
	}
	
	public void setFor(Key key, int code) {
		map.put(key, code);
	}
	
}
