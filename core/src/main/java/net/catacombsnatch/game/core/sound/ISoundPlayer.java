package net.catacombsnatch.game.core.sound;

public interface ISoundPlayer {

	public static final String BACKGROUND_TRACK = "background";

	/** Starts the title music */
	public abstract void startTitleMusic();

	/** Stops the title music */
	public abstract void stopTitleMusic();

	/** Starts the end music */
	public abstract void startEndMusic();

	/** Stops the end music */
	public abstract void stopEndMusic();

	/** Starts the background playlist */
	public abstract void startBackgroundMusic();

	/** Stops the background playlist */
	public abstract void stopBackgroundMusic();

	/** Pauses the background playlist */
	public abstract void pauseBackgroundMusic();

	/** Resumes the background playlist */
	public abstract void resumeBackgroundMusic();

	/**
	 * Sets the listener position
	 * 
	 * @param x The x-position
	 * @param y The y-position
	 */
	public abstract void setListenerPosition( float x, float y );

	/**
	 * Plays a sound with a given name at a specific location
	 * 
	 * @param sourceName The sound file name
	 * @param x The x-position
	 * @param y The y-position
	 * @return True if the sound could be played, otherwise false
	 */
	public abstract boolean playSound( String sourceName, float x, float y );

	/**
	 * Plays a sound with a given name at a specific location with sound
	 * blocking
	 * 
	 * @param sourceName The sound file name
	 * @param x The x-position
	 * @param y The y-position
	 * @param blocking True if the sound should be blocked, otherwise false
	 * @return True if the sound could be played, otherwise false
	 */
	public abstract boolean playSound( String sourceName, float x, float y, boolean blocking );

	/** Shuts down the sound system */
	public abstract void shutdown();

	/**
	 * Returns true if the system is muted
	 * 
	 * @return True if the system is muted, otherwise false
	 */
	public abstract boolean isMuted();

	/**
	 * Mutes the sound system
	 * 
	 * @param muted True for muting, false for unmuting
	 */
	public abstract void setMuted( boolean muted );

}
