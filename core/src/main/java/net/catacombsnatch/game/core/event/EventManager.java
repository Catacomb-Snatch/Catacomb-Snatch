package net.catacombsnatch.game.core.event;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;


/** Manages event handling. */
public class EventManager {
	protected final static Map<Class<? extends Event>, EventRegistry> registry;
	
	static {
		registry = new HashMap<Class<? extends Event>, EventRegistry>();
	}
	
	
	@SuppressWarnings("unchecked")
	public static void registerListener(Object listener) {
		for(Method method : listener.getClass().getMethods()) {
			EventHandler handler = method.getAnnotation(EventHandler.class);
			if(handler == null) continue;
			
			Type returnType = method.getReturnType();
			if(returnType != Void.TYPE) {
				throw new GdxRuntimeException("Unsupported return type: " + returnType.toString());
			}
			
			if(method.getParameterTypes().length != 1) {
				throw new GdxRuntimeException("Method " + method.getName() + " needs exactly 1 parameter!");
			}
			
			Class<?> eventParam = method.getParameterTypes()[0];
			if(Event.class.isAssignableFrom(eventParam)) {
				EventRegistry entry = registry.get(eventParam);
				
				if(entry == null) {
					entry = new EventRegistry();
					registry.put((Class<? extends Event>) eventParam, entry);
				}
				
				entry.addEntry(new Listener(handler.priority(), handler.ignoreCancelled(), method, listener));
				
			} else {
				throw new GdxRuntimeException("Method " + method.getName() + " does not have a proper event parameter!");
			}
		}
	}
	
	public static void unregisterListener(Object listener) {
		for(Method method : listener.getClass().getMethods()) {
			EventHandler handler = method.getAnnotation(EventHandler.class);
			if(handler == null) continue;
			
			Class<?> eventParam = method.getParameterTypes()[0];
			if(Event.class.isAssignableFrom(eventParam)) {
				EventRegistry entry = registry.get(eventParam);
				
				if(entry != null) entry.remove(listener);
			}
		}
	}
	
	public static void callEvent(Event event) {
		EventRegistry entry = registry.get(event.getClass());
		if(entry == null) return;
		
		if (event instanceof CancellableEvent) {
			CancellableEvent cancellableEvent = (CancellableEvent) event;
			
			for(Listener listener : entry.getListeners()) {
				if(cancellableEvent.isCancelled() && !listener.ignoresCancelledEvents()) continue;
				
				listener.listen(cancellableEvent);
			}
			
		} else {
			//for(Listener listener : entry.getListeners()) {
			for (int i = 0; i < entry.getListeners().size(); i++) {
				Listener listener = entry.getListeners().get(i);
				listener.listen(event);
			}
		}
	}
	
	protected static class EventRegistry {
		protected List<Listener> listeners;
		
		public EventRegistry() {
			listeners = new ArrayList<Listener>();
		}
		
		public void addEntry(Listener entry) {
			listeners.add(entry);
			
			Collections.sort(listeners, new Comparator<Listener>() {
				@Override
			    public int compare(Listener l1, Listener l2) {
			        return l2.getPriority() - l1.getPriority();
			    }
			});
		}
		
		public void remove(Object listener) {
			Iterator<Listener> it = listeners.iterator();
			while(it.hasNext()) {
				if(it.next().equals(listener)) it.remove();
			}
		}
		
		public List<Listener> getListeners() {
			return listeners;
		}
	}
	
	protected static class Listener {
		protected final int priority;
		protected final boolean ignores;
		protected final Method method;
		protected final Object instance;
		
		public Listener(int priority, boolean ignores, Method method, Object clazz) {
			this.priority = priority;
			this.ignores = ignores;
			this.method = method;
			this.instance = clazz;
		}
		
		public int getPriority() {
			return priority;
		}

		public boolean ignoresCancelledEvents() {
			return ignores;
		}
		
		public void listen(Event event) {
			try {
				method.invoke(instance, event);
			} catch (Exception ex) {
				Gdx.app.error("Event", "Error listening to event " + event + " in listener " + instance, ex);
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			return instance.equals(obj);
		}
	}
	
}
