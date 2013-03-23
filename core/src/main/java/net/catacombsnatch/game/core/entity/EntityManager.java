package net.catacombsnatch.game.core.entity;

import com.badlogic.gdx.Gdx;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.catacombsnatch.game.core.entity.components.EntityComponent;

public class EntityManager {
	public final static String TAG = "[EntityManager]";
	
	protected List<Entity> entities;
	protected Map<Class<? extends EntityComponent>, ComponentStorage<? extends EntityComponent>> components;
	protected Map<Entity, EntityMeta> metadata;

	protected long nextId;

	public EntityManager() {
		entities = new LinkedList<Entity>();
		components = new HashMap<Class<? extends EntityComponent>, ComponentStorage<? extends EntityComponent>>();
	}

	/**
	 * Returns an <em>unmodifiable</em> collection of all stored entities.
	 * 
	 * @return The entity collection
	 */
	public Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection( entities );
	}

	/**
	 * Returns a {@link Set} of entities that have a specific component
	 * 
	 * @param component The component
	 * @return The {@link Set} of entities
	 */
	public Set<Entity> getAllWithComponent( Class<EntityComponent> component ) {
		return (Set<Entity>) components.get( component ).keySet();
	}

	/**
	 * Creates a new entity and returns it on success. If the entity could not
	 * be created, null is being returned. This function is synchronized to
	 * prevent thread safety issues.
	 * 
	 * @return The entity
	 */
	public synchronized Entity createEntity() {
		Entity entity = new Entity( this, nextId++ );

		if ( entities.add( entity ) ) return entity;
		return null;
	}

	/**
	 * Kills (removes) an entity from the system. This function is synchronized
	 * to prevent thread safety issues.
	 * 
	 * @param entity The entity to remove
	 * @return True if entity could be deleted
	 */
	public synchronized boolean killEntity( Entity entity ) {
		if ( !entities.remove( entity ) ) return false;

		for ( ComponentStorage<? extends EntityComponent> storage : components.values() ) {
			if ( storage.containsKey( entity ) ) storage.remove( entity );
		}

		return true;
	}

	/**
	 * Adds a component to an entity. An instance of the component will be
	 * automatically created. This function is synchronized to prevent thread
	 * safety issues.
	 * 
	 * @param entity The entity to add the component to
	 * @param component The component to add
	 * @return The component instance
	 */
	public synchronized <T extends EntityComponent> T addComponent( Entity entity, Class<T> component ) {
		T instance = null;

		try {
			instance = component.newInstance();
			this.addComponent( entity, component, instance );

		} catch ( Exception e ) {
			Gdx.app.error(TAG, "Error adding component '" + component + "' to entity with id " + entity.getEntityId(), e);
		}

		return instance;
	}

	/**
	 * Adds a component to an entity. This function is synchronized to prevent
	 * thread safety issues.
	 * 
	 * @param entity The entity to add the component to
	 * @param component The component to add
	 * @param instance The component instance
	 * @return The component instance
	 */
	public synchronized <T extends EntityComponent> T addComponent( Entity entity, Class<T> component, T instance ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored == null ) {
			stored = new ComponentStorage<T>();
			components.put( component, stored );
		}

		stored.put( entity, instance );

		return instance;
	}

	/**
	 * Returns true if the given entity has a certain component
	 * 
	 * @param entity The entity to check
	 * @param component The component to check
	 * @return True on success, otherwise false
	 */
	public synchronized <T extends EntityComponent> boolean hasComponent( Entity entity, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) {
			for ( Entity e : stored.keySet() ) {
				if ( e.equals( entity ) ) return true;
			}
		}

		return false;
	}

	/**
	 * Returns the instance of a component. If no instance could be found null
	 * is returned. This function is synchronized to prevent thread safety
	 * issues.
	 * 
	 * @param entity The entity
	 * @param component The component
	 * @return The component instance or null
	 */
	@SuppressWarnings( "unchecked" )
	public synchronized <T extends EntityComponent> T getComponent( Entity entity, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) {
			for ( Entry<Entity, ? extends EntityComponent> entry : stored.entrySet() ) {
				if ( entry.getKey() == entity ) {
					// Return the stored instance
					return (T) entry.getValue();
				}
			}
		}

		return null;
	}

	/**
	 * Removes a component from an entity. This function is synchronized to
	 * prevent thread safety issues.
	 * 
	 * @param entity The entity
	 * @param component The component
	 * @return True on success, otherwise false
	 */
	public synchronized <T extends EntityComponent> boolean removeComponent( Entity entity, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) return stored.remove( entity ) != null;

		return false;
	}

	/**
	 * Returns the meta data for an entity.
	 * 
	 * @param entity The id of the entity to get the data from
	 * @return The {@link EntityMeta} entry
	 */
	public EntityMeta getMetaData( Entity entity ) {
		EntityMeta meta = metadata.get( entity );

		if ( meta != null ) return meta;

		EntityMeta m = new EntityMeta();
		metadata.put( entity, m );

		return m;
	}

	protected class ComponentStorage<T extends EntityComponent> extends AbstractMap<Entity, T> {
		private final Map<Entity, T> content = new HashMap<Entity, T>();

		@Override
		public Set<Entry<Entity, T>> entrySet() {
			return content.entrySet();
		}

		@Override
		public Set<Entity> keySet() {
			return content.keySet();
		}

		@Override
		public Collection<T> values() {
			return content.values();
		}

		@SuppressWarnings("unchecked")
		@Override
		public T put( final Entity id, final EntityComponent instance ) {
			return content.put( id, (T) instance );
		}
	}
}
