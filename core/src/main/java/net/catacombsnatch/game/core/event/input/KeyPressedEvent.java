package net.catacombsnatch.game.core.event.input;


/** Called whenever a key is getting pressed (down state) */
public class KeyPressedEvent extends InputEvent {
	protected final Key key;
	
	public KeyPressedEvent(InputSource source, Key key) {
		super(source);
		
		this.key = key;
	}

	/**
	 * The key that is getting pressed
	 * @return The key
	 */
	public Key getKey() {
		return key;
	}
}
