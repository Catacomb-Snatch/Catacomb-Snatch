package net.catacombsnatch.game.core.event.input.events;

import net.catacombsnatch.game.core.event.input.InputEvent;
import net.catacombsnatch.game.core.event.input.InputSource;
import net.catacombsnatch.game.core.event.input.Key;

import com.badlogic.gdx.controllers.Controller;

/** Called whenever a key is getting released (up state) */
public class KeyReleaseEvent extends InputEvent {
	protected final Key key;
	protected final Controller controller;
	
	public KeyReleaseEvent(InputSource source, Key key, Controller controller) {
		super(source);
		
		this.key = key;
		this.controller = controller;
	}

	/**
	 * The key that is getting released
	 * @return The key
	 */
	public Key getKey() {
		return key;
	}
	
	/**
	 * The {@link Controller} involved in this event, if any.
	 * If this event has been called from a keyboard this will return null.
	 * @return The involved in this event, if any.
	 */
	public Controller getController() {
		return controller;
	}
}
