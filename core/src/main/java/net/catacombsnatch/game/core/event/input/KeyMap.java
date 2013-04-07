package net.catacombsnatch.game.core.event.input;

import java.util.EnumMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Input;


public class KeyMap {
	public final static KeyMap KEYBOARD, CONTROLLER;
	static {
		// Keyboard & Mouse
		KEYBOARD = new KeyMap();
		KEYBOARD.setFor(Key.SCREENSHOT, InputSource.KEYBOARD, Input.Keys.F2);
		KEYBOARD.setFor(Key.MOVE_LEFT, InputSource.KEYBOARD, Input.Keys.LEFT);
		KEYBOARD.setFor(Key.MOVE_RIGHT, InputSource.KEYBOARD, Input.Keys.RIGHT);
		KEYBOARD.setFor(Key.MOVE_UP, InputSource.KEYBOARD, Input.Keys.UP);
		KEYBOARD.setFor(Key.MOVE_DOWN, InputSource.KEYBOARD, Input.Keys.DOWN);

		KEYBOARD.setFor(Key.USE, InputSource.KEYBOARD, Input.Keys.ENTER);
		KEYBOARD.setFor(Key.FIRE, InputSource.MOUSE, Input.Buttons.LEFT);
		KEYBOARD.setFor(Key.SPRINT, InputSource.KEYBOARD, Input.Keys.SHIFT_LEFT);
		
		// Gamepad controller
		CONTROLLER = new KeyMap();
		CONTROLLER.setFor(Key.USE, InputSource.CONTROLLER, 3);
		CONTROLLER.setFor(Key.FIRE, InputSource.CONTROLLER, 2);
		CONTROLLER.setFor(Key.SPRINT, InputSource.CONTROLLER, 6);
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
