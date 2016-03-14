package net.catacombsnatch.game;

import com.badlogic.gdx.utils.Disposable;

/**
 * An interface for cleaningly initalizing
 * and tearing down any platform dependent
 * runtime structures.
 * <p>
 * e.g. Cursors on desktop and no Cursors
 * on mobile.
 *
 * @author Kyle Brodie
 */
public interface PlatformDependent extends Disposable {

    /**
     * Creates all runtime static objects
     */
    void create();

    /**
     * Creates all runtime dynamic objects
     * necessary for the game runtime.
     * <p>
     * Programmer notes: See implementing
     * function's javadocs to determine
     * which objects are provided in this
     * array (and their order) for correct
     * casting.
     * <p>
     * (RESERVED FOR FUTURE USE)
     */
    Object[] createPlatformObjects();

}
