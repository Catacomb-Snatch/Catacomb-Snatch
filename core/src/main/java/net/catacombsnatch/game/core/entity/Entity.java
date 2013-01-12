package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.entity.components.EntityComponent;

public class Entity {
	protected final EntityManager manager;
	protected final long id;

	public Entity( EntityManager manager, long id ) {
		this.manager = manager;
		this.id = id;
	}

	public EntityManager getManager() {
		return this.manager;
	}

	public long getEntityId() {
		return this.id;
	}

	public <T extends EntityComponent> T addComponent( Class<T> component ) {
		return getManager() != null ? getManager().addComponent( this, component ) : null;
	}

	public <T extends EntityComponent> T addComponent( Class<T> component, T instance ) {
		return getManager() != null ? getManager().addComponent( this, component, instance ) : null;
	}

	public <T extends EntityComponent> T getComponent( Class<T> component ) {
		return getManager() != null ? getManager().getComponent( this, component ) : null;
	}

	public <T extends EntityComponent> boolean hasComponent( Class<T> component ) {
		return getManager() != null ? getManager().hasComponent( this, component ) : false;
	}
}
