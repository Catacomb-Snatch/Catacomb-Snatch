package net.catacombsnatch.game.core.event.input;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.catacombsnatch.game.core.event.EventOrder;

@Retention(RetentionPolicy.RUNTIME)
public @interface Input {
	public EventOrder priority() default EventOrder.NORMAL;
}
