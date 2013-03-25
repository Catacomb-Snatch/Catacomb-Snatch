package net.catacombsnatch.game.core.event.input;

import net.catacombsnatch.game.core.event.Event;


public class InputEvent extends Event {
	public enum InputSource {
		KEYBOARD, MOUSE, CONTROLLER;
	}
	
	protected final InputSource source;
	
	public InputEvent(InputSource source) {
		this.source = source;
	}

	public InputSource getSource() {
		return source;
	}

}
