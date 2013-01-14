package net.catacombsnatch.game.core.sound;

public interface ISoundPlayer {

	/** Starts the title music */
	public void startTitleMusic();

	/** Stops the title music */
	public void stopTitleMusic();

	/** Starts the end music */
	public void startEndMusic();

	/** Stops the end music */
	public void stopEndMusic();

	/** Starts the background playlist */
	public void startBackgroundMusic();

	/** Stops the background playlist */
	public void stopBackgroundMusic();

	/** Pauses the background playlist */
	public void pauseBackgroundMusic();

	/** Resumes the background playlist */
	public void resumeBackgroundMusic();

	/**
	 * Sets the listener position
	 * 
	 * @param x The x-position
	 * @param y The y-position
	 */
	public void setListenerPosition( float x, float y );

	/**
	 * Plays a sound with a given name at a specific location
	 * 
	 * @param sourceName The sound file name
	 * @param x The x-position
	 * @param y The y-position
	 * @return True if the sound could be played, otherwise false
	 */
	public boolean playSound( String sourceName, float x, float y );

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
	public boolean playSound( String sourceName, float x, float y, boolean blocking );

	/** Shuts down the sound system */
	public void shutdown();

	/**
	 * Returns true if the system is muted
	 * 
	 * @return True if the system is muted, otherwise false
	 */
	public boolean isMuted();

	/**
	 * Mutes the sound system
	 * 
	 * @param muted True for muting, false for unmuting
	 */
	public void setMuted( boolean muted );

}
