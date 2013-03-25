package net.catacombsnatch.game.core.event.input;


/** Called whenever a key is getting pressed (down state) */
public class KeyPressedEvent extends InputEvent {
	protected final int key;
	
	public KeyPressedEvent(InputSource source, int key) {
		super(source);
		
		this.key = key;
	}

	/**
	 * The key that is getting pressed
	 * @return The internal key number
	 */
	public int getKey() {
		return key;
	}
}
