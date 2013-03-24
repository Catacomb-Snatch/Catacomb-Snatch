package net.catacombsnatch.game.core.event;

public class CancellableEvent implements Event {
	private boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCanceleld( boolean cancel ) {
		cancelled = cancel;
	}
}
