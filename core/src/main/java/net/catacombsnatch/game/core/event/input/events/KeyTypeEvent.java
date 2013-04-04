package net.catacombsnatch.game.core.event.input.events;

import net.catacombsnatch.game.core.event.input.InputEvent;
import net.catacombsnatch.game.core.event.input.InputSource;


public class KeyTypeEvent extends InputEvent {
	protected final char letter;
	
	public KeyTypeEvent(InputSource source, char letter) {
		super(source);
		
		this.letter = letter;
	}

	/**
	 * Returns the typed in letter
	 * @return The typed letter
	 */
	public char getLetter() {
		return letter;
	}
}
