package net.catacombsnatch.game.core.event;

public abstract class Event {
	private boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled( boolean cancel ) {
		cancelled = cancel;
	}
}
