package net.catacombsnatch.game.core.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	public int priority() default 0;
	public boolean ignoreCancelled() default false;
}
