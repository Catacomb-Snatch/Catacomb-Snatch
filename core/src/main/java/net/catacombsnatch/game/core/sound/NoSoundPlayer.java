package net.catacombsnatch.game.core.sound;

public class NoSoundPlayer implements ISoundPlayer {

	// Dummy method; no implementation needed
	@Override
	public void startTitleMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void stopTitleMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void startEndMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void stopEndMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void startBackgroundMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void stopBackgroundMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void setListenerPosition( float x, float y ) {}

	// Dummy method; no implementation needed
	@Override
	public boolean playSound( String sourceName, float x, float y ) {
		return false;
	}

	// Dummy method; no implementation needed
	@Override
	public boolean playSound( String sourceName, float x, float y, boolean blocking ) {
		return false;
	}

	// Dummy method; no implementation needed
	@Override
	public void shutdown() {}

	// Dummy method; no implementation needed
	@Override
	public boolean isMuted() {
		return false;
	}

	// Dummy method; no implementation needed
	@Override
	public void setMuted( boolean muted ) {}

	// Dummy method; no implementation needed
	@Override
	public void pauseBackgroundMusic() {}

	// Dummy method; no implementation needed
	@Override
	public void resumeBackgroundMusic() {}

}
