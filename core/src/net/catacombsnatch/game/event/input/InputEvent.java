package net.catacombsnatch.game.event.input;

import net.catacombsnatch.game.event.Event;


public abstract class InputEvent extends Event {
	protected final InputSource source;
	
	public InputEvent(InputSource source) {
		this.source = source;
	}

	public InputSource getSource() {
		return source;
	}

}
