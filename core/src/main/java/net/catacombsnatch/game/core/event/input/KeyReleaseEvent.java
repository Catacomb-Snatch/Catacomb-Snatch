package net.catacombsnatch.game.core.event.input;

/** Called whenever a key is getting released (up state) */
public class KeyReleaseEvent extends InputEvent {
	protected final int key;
	
	public KeyReleaseEvent(InputSource source, int key) {
		super(source);
		
		this.key = key;
	}

	/**
	 * The key that is getting released
	 * @return The internal key number
	 */
	public int getKey() {
		return key;
	}
}
