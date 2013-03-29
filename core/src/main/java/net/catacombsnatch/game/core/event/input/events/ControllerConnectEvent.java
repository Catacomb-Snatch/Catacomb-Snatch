package net.catacombsnatch.game.core.event.input.events;

import com.badlogic.gdx.controllers.Controller;
import net.catacombsnatch.game.core.event.Event;

/**
 * Called whenever a controller is getting connected.
 * <b>This event ignores the cancelled result</b>
 */
public class ControllerConnectEvent extends Event {
	protected final Controller controller;
	
	public ControllerConnectEvent(Controller controller) {
		this.controller = controller;
	}

	public Controller getController() {
		return controller;
	}
	
}
