package net.catacombsnatch.game.core.event;

/** Represents a cancellable event */
public class CancellableEvent extends Event {
	private boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
