package net.catacombsnatch.game.core.event;

/**
 * Represents a callable event.
 * 
 * <b>Important: </b> All events can be cancelled, but sometimes this will be ignored.
 * Allowing all events to be cancellable is just faster than having several interfaces.
 */
public abstract class Event {
	private boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled( boolean cancel ) {
		cancelled = cancel;
	}
}
