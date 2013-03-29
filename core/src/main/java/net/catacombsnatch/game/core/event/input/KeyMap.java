package net.catacombsnatch.game.core.event.input;

import java.util.EnumMap;
import java.util.Map.Entry;

import net.catacombsnatch.game.core.event.input.InputEvent.InputSource;

import com.badlogic.gdx.Input;


public class KeyMap {
	public final static KeyMap defaultMapping;
	static {
		defaultMapping = new KeyMap();
		defaultMapping.setFor(Key.MOVE_LEFT, InputSource.KEYBOARD, Input.Keys.LEFT);
		defaultMapping.setFor(Key.MOVE_RIGHT, InputSource.KEYBOARD, Input.Keys.RIGHT);
		defaultMapping.setFor(Key.MOVE_UP, InputSource.KEYBOARD, Input.Keys.UP);
		defaultMapping.setFor(Key.MOVE_DOWN, InputSource.KEYBOARD, Input.Keys.DOWN);

		defaultMapping.setFor(Key.USE, InputSource.KEYBOARD, Input.Keys.ENTER);
		defaultMapping.setFor(Key.FIRE, InputSource.MOUSE, Input.Buttons.LEFT);
		defaultMapping.setFor(Key.SPRINT, InputSource.KEYBOARD, Input.Keys.SHIFT_LEFT);
	}
	
	protected EnumMap<Key, KeyMapEntry> entries;
	
	public KeyMap() {
		entries = new EnumMap<Key, KeyMapEntry>(Key.class);
	}
	
	public Key getKeyFor(InputSource source, int code) {
		for(Entry<Key, KeyMapEntry> entry : entries.entrySet()) {
			KeyMapEntry e = entry.getValue();
			if(e.source.equals(source) && e.code == code) return entry.getKey(); 
		}
		
		return null;
	}
	
	public void setFor(Key key, InputSource source, int code) {
		entries.put(key, new KeyMapEntry(source, code));
	}
	
	protected class KeyMapEntry {
		public final InputSource source;
		public final int code;
		
		public KeyMapEntry(InputSource source, int code) {
			this.source = source;
			this.code = code;
		}
	}
	
}
