package net.catacombsnatch.game.core.entity;

public abstract class EntityComponent {
	private final long entityId;
	
	public EntityComponent(long id) {
		this.entityId = id;
	}
	
	public long getEntityId() {
		return entityId;
	}
	
}
