package net.catacombsnatch.game.core.event.input.events;

import com.badlogic.gdx.controllers.Controller;
import net.catacombsnatch.game.core.event.Event;


/**
 * Called whenever a controller is getting disconnected.
 * This event might not getting called all the times (Depends on the platform)!
 * <b>This event ignores the cancelled result</b>
 */
public class ControllerDisconnectEvent extends Event {
	protected final Controller controller;
	
	public ControllerDisconnectEvent(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}
	
}
