package net.catacombsnatch.game.core;

import com.badlogic.gdx.utils.Disposable;

/**
 * An interface for cleaningly initalizing
 * and tearing down any platform dependent
 * runtime structures.
 *
 * e.g. Cursors on desktop and no Cursors
 * 		on mobile.
 *
 * @author Kyle Brodie
 */
public interface PlatformDependent extends Disposable {

	/**
	 * Creates all runtime static objects
	 */
	public void create();

	/**
	 * Creates all runtime dynamic objects
	 * necessary for the game runtime.
	 *
	 * Programmer notes: See implementing
	 * function's javadocs to determine
	 * which objects are provided in this
	 * array (and their order) for correct
	 * casting.
	 *
	 * (RESERVED FOR FUTURE USE)
	 */
	public Object[] createPlatformObjects();

}
