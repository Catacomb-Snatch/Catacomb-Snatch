package net.catacombsnatch.game.core.sound;

public class NoSoundPlayer implements ISoundPlayer {

	// Dummy method; no implementation needed
	public void startTitleMusic() {}

	// Dummy method; no implementation needed
	public void stopTitleMusic() {}

	// Dummy method; no implementation needed
	public void startEndMusic() {}

	// Dummy method; no implementation needed
	public void stopEndMusic() {}

	// Dummy method; no implementation needed
	public void startBackgroundMusic() {}

	// Dummy method; no implementation needed
	public void stopBackgroundMusic() {}

	// Dummy method; no implementation needed
	public void setListenerPosition( float x, float y ) {}

	// Dummy method; no implementation needed
	public boolean playSound( String sourceName, float x, float y ) {
		return false;
	}

	// Dummy method; no implementation needed
	public boolean playSound( String sourceName, float x, float y, boolean blocking ) {
		return false;
	}

	// Dummy method; no implementation needed
	public void shutdown() {}

	// Dummy method; no implementation needed
	public boolean isMuted() {
		return false;
	}

	// Dummy method; no implementation needed
	public void setMuted( boolean muted ) {}

	// Dummy method; no implementation needed
	public void pauseBackgroundMusic() {}

	// Dummy method; no implementation needed
	public void resumeBackgroundMusic() {}

}
