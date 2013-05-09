package net.catacombsnatch.game.core.entity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;


public class EntityManager {
	public final static String TAG = "[EntityManager]";
	
	protected List<Long> entities;
	protected Map<Class<? extends EntityComponent>, ComponentStorage<? extends EntityComponent>> components;
	protected Map<Long, EntityMeta> metadata;

	protected long nextId;

	public EntityManager() {
		entities = new ArrayList<Long>();
		components = new HashMap<Class<? extends EntityComponent>, ComponentStorage<? extends EntityComponent>>();
	}

	/**
	 * Returns an <em>unmodifiable</em> collection of all stored entity ids.
	 * 
	 * @return The entity collection
	 */
	public Collection<Long> getEntities() {
		return Collections.unmodifiableCollection( entities );
	}

	/**
	 * Returns a {@link Set} of entity ids that have a specific component
	 * 
	 * @param component The component
	 * @return The {@link Set} of entities
	 */
	public Set<Long> getAllWithComponent( Class<EntityComponent> component ) {
		return components.get( component ).keySet();
	}

	/**
	 * Creates a new entity id and returns it on success. If the entity could not
	 * be created, -1 is being returned. This function is synchronized to
	 * prevent thread safety issues.
	 * 
	 * @return The entity id, -1 if creation fails
	 */
	public synchronized long createEntity() {
		if ( entities.add( nextId++ ) ) return nextId;
		return -1;
	}

	/**
	 * Kills (removes) an entity from the system. This function is synchronized
	 * to prevent thread safety issues.
	 * 
	 * @param entityId The entity to remove
	 * @return True if entity could be deleted
	 */
	public synchronized boolean killEntity( long entityId ) {
		if ( !entities.remove( entityId ) ) return false;

		for ( ComponentStorage<? extends EntityComponent> storage : components.values() ) {
			if ( storage.containsKey( entityId ) ) storage.remove( entityId );
		}

		return true;
	}

	/**
	 * Adds a component to an entity. An instance of the component will be
	 * automatically created. This function is synchronized to prevent thread
	 * safety issues.
	 * 
	 * @param entityId The entity id to add the component to
	 * @param component The component to add
	 * @return The component instance
	 */
	public synchronized <T extends EntityComponent> T addComponent( long entityId, Class<T> component ) {
		T instance = null;

		try {
			instance = component.getConstructor(getClass(), Long.class).newInstance(this, entityId);
			this.addComponent( entityId, component, instance );

		} catch ( Exception e ) {
			Gdx.app.error(TAG, "Error adding component '" + component + "' to entity with id " + entityId, e);
		}

		return instance;
	}

	/**
	 * Adds a component to an entity. This function is synchronized to prevent
	 * thread safety issues.
	 * 
	 * @param entityId The entity to add the component to
	 * @param component The component to add
	 * @param instance The component instance
	 * @return The component instance
	 */
	public synchronized <T extends EntityComponent> T addComponent( long entityId, Class<T> component, T instance ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored == null ) {
			stored = new ComponentStorage<T>();
			components.put( component, stored );
		}

		stored.put( entityId, instance );

		return instance;
	}

	/**
	 * Returns true if the given entity has a certain component
	 * 
	 * @param entityId The entity to check
	 * @param component The component to check
	 * @return True on success, otherwise false
	 */
	public synchronized <T extends EntityComponent> boolean hasComponent( long entityId, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) {
			for ( long id : stored.keySet() ) {
				if ( id == entityId ) return true;
			}
		}

		return false;
	}

	/**
	 * Returns the instance of a component. If no instance could be found null
	 * is returned. This function is synchronized to prevent thread safety
	 * issues.
	 * 
	 * @param entityId The entity id
	 * @param component The component
	 * @return The component instance or null
	 */
	@SuppressWarnings( "unchecked" )
	public synchronized <T extends EntityComponent> T getComponent( long entityId, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) {
			for ( Entry<Long, ? extends EntityComponent> entry : stored.entrySet() ) {
				if ( entry.getKey() == entityId ) {
					// Return the stored instance
					return (T) entry.getValue();
				}
			}
		}

		return null;
	}
	
	/**
	 * Returns all stored component instances for all entities.
	 * 
	 * @return A {@link List} of <b>all</b> {@link EntityComponent} instances. 
	 */
	@SuppressWarnings("unchecked")
	public synchronized List<EntityComponent> getComponents() {
		List<EntityComponent> list = new ArrayList<EntityComponent>();

		for(@SuppressWarnings("rawtypes") ComponentStorage storage : components.values()) {
			list.addAll(storage.values());
		}
		
		return list;
	}
	
	/**
	 * Returns all stored component instances for an entity.
	 * 
	 * @param entityId The entity id
	 * @return A {@link List} of <b>all</b> {@link EntityComponent} instances. 
	 */
	public synchronized List<EntityComponent> getComponents( long entityId ) {
		List<EntityComponent> list = new ArrayList<EntityComponent>();
		
		for(@SuppressWarnings("rawtypes") ComponentStorage storage : components.values()) {
			EntityComponent component = (EntityComponent) storage.get(entityId);
			if(component != null) list.add(component);
		}
		
		return list;
	}

	/**
	 * Removes a component from an entity. This function is synchronized to
	 * prevent thread safety issues.
	 * 
	 * @param entityId The entity id
	 * @param component The component
	 * @return True on success, otherwise false
	 */
	public synchronized <T extends EntityComponent> boolean removeComponent( long entityId, Class<T> component ) {
		ComponentStorage<? extends EntityComponent> stored = components.get( component );

		if ( stored != null ) return stored.remove( entityId ) != null;

		return false;
	}

	/**
	 * Returns the meta data for an entity.
	 * 
	 * @param entityId The id of the entity to get the data from
	 * @return The {@link EntityMeta} entry
	 */
	public EntityMeta getMetaData( long entityId ) {
		EntityMeta meta = metadata.get( entityId );

		if ( meta != null ) return meta;

		EntityMeta m = new EntityMeta();
		metadata.put( entityId, m );

		return m;
	}

	protected class ComponentStorage<T extends EntityComponent> extends AbstractMap<Long, T> {
		private final Map<Long, T> content = new HashMap<Long, T>();

		@Override
		public Set<Entry<Long, T>> entrySet() {
			return content.entrySet();
		}

		@Override
		public Set<Long> keySet() {
			return content.keySet();
		}

		@Override
		public Collection<T> values() {
			return content.values();
		}

		@SuppressWarnings("unchecked")
		@Override
		public T put( final Long id, final EntityComponent instance ) {
			return content.put( id, (T) instance );
		}
	}
}
