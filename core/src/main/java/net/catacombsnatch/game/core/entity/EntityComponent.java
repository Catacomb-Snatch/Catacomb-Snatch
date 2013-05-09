package net.catacombsnatch.game.core.entity;

public abstract class EntityComponent {
	private final EntityManager manager;
	private final long entityId;
	
	public EntityComponent(EntityManager manager, long id) {
		this.manager = manager;
		this.entityId = id;
	}
	
	public EntityManager getManager() {
		return manager;
	}
	
	public long getEntityId() {
		return entityId;
	}
	
}
