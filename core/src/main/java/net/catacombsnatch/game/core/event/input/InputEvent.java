package net.catacombsnatch.game.core.event.input;

import net.catacombsnatch.game.core.event.Event;


public abstract class InputEvent extends Event {
	protected final InputSource source;
	
	public InputEvent(InputSource source) {
		this.source = source;
	}

	public InputSource getSource() {
		return source;
	}

}
