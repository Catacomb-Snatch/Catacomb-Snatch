package net.catacombsnatch.game.core.event.input;

/** Called whenever a key is getting released (up state) */
public class KeyReleaseEvent extends InputEvent {
	protected final Key key;
	
	public KeyReleaseEvent(InputSource source, Key key) {
		super(source);
		
		this.key = key;
	}

	/**
	 * The key that is getting released
	 * @return The key
	 */
	public Key getKey() {
		return key;
	}
}
