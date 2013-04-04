package net.catacombsnatch.game.core.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	public int priority() default Priority.NORMAL;
	public boolean ignoreCancelled() default false;
	
	public static class Priority {
		public final static int LOWEST = 10;
		public final static int LOW = 20;
		public final static int NORMAL = 30;
		public final static int HIGH = 40;
		public final static int HIGHEST = 50;
	}
}
