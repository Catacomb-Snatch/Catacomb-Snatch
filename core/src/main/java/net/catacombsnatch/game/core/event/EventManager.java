package net.catacombsnatch.game.core.event;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class EventManager {
	protected final static Map<Class<? extends Event>, Array<EventRegisterEntry>> registry;
	
	static {
		registry = new HashMap<Class<? extends Event>, Array<EventRegisterEntry>>();
	}
	
	
	public void register(Object listener) {
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
			if(eventParam.isAssignableFrom(Event.class)) {
				Array<EventRegisterEntry> array = registry.get((Class<? extends Event>) eventParam);
				
				if(array == null) {
					array = new Array<EventRegisterEntry>();
					registry.put((Class<? extends Event>) eventParam, array);
				}
				
				array.add(new EventRegisterEntry(handler, method, listener));
				
			} else {
				throw new GdxRuntimeException("Method " + method.getName() + " does not have a proper event parameter!");
			}
		}
	}
	
	public static void callEvent(Event event) {
		Array<EventRegisterEntry> entries = registry.get(event.getClass());
		
		if(entries != null) for(EventRegisterEntry  entry : entries) {
			// TODO
		}
	}
	
	protected class EventRegisterEntry {
		public final EventHandler handler;
		public final Method method;
		public final Object instance;
		
		public EventRegisterEntry(EventHandler h, Method m, Object o) {
			handler = h;
			method = m;
			instance = o;
		}
	}
}
