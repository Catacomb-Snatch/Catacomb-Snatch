package net.catacombsnatch.game.core.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GdxSoundPlayer implements ISoundPlayer {
	public final static String TAG = "[SoundPlayer]";
	
	private float listenerX = 0.0f;
	private float listenerY = 0.0f;

	private float maxSoundDistance = 200.0f;

	private boolean paused = false;
	private boolean muted = false;
	private float musicVolume = 0.5f;
	private float soundVolume = 1.0f;

	private long timeBetweenSongs = 10; // in seconds
	private long songEnded = 0;

	private Map<String, Music> musicMap;
	private Map<String, Sound> soundsMap;

	private List<Music> backgroundMusicList;
	private int backgroundPlaying;

	public GdxSoundPlayer() {
		// Initialize all lists
		soundsMap = new HashMap<String, Sound>();
		musicMap = new HashMap<String, Music>();
		backgroundMusicList = new ArrayList<Music>();

		// Load music
		loadMusic( Sounds.TITLE_THEME );
		loadMusic( Sounds.END_THEME );
		loadMusic( Sounds.BACKGROUND_TRACK_1 );
		loadMusic( Sounds.BACKGROUND_TRACK_2 );
		loadMusic( Sounds.BACKGROUND_TRACK_3 );
		loadMusic( Sounds.BACKGROUND_TRACK_4 );

		// Load sounds
		loadSound( Sounds.SOUND_SHOT_1 );
		loadSound( Sounds.SOUND_EXPLOSION );
	}

	private void loadMusic( Sounds music ) {
		try {
			Gdx.app.log(TAG, Gdx.files.internal("music/"+music.name+".ogg").file().getAbsolutePath());
			Music file = Gdx.audio.newMusic( Gdx.files.internal( "music/" + music.name + ".ogg" ) );
			// If background track, add to playlist
			if ( music.name.toLowerCase().startsWith( "background" ) ) backgroundMusicList.add( file );

			musicMap.put( music.name, file );

		} catch (GdxRuntimeException e){
			Gdx.app.log(TAG, "Error loading musicfile: " + music.name + ": " + e.getMessage());
		}
	}

	private void loadSound( Sounds sound ) {
		try{
			soundsMap.put( sound.name, Gdx.audio.newSound( Gdx.files.internal( "sound/" + sound.name + ".wav" ) ) );
		}catch (GdxRuntimeException e){
			Gdx.app.log(TAG, "Error loading soundfile: " + sound.name);
		}
	}

	private Music getMusic( Sounds music ) {
		return musicMap.get( music.name );
	}

	@Override
	public void startTitleMusic() {
		stopBackgroundMusic();
		stopEndMusic();

		Music titleMusic = getMusic( Sounds.TITLE_THEME );
		if (titleMusic == null){
			return;
		}
		if ( musicVolume > 0.0f && !titleMusic.isPlaying() && !paused ) {
			titleMusic.setLooping( true );
			titleMusic.setVolume( musicVolume );
			titleMusic.play();
		}
	}

	@Override
	public void stopTitleMusic() {
		Music titleMusic = getMusic( Sounds.TITLE_THEME );
		if (titleMusic == null){
			return;
		}
		if ( titleMusic.isPlaying() ) titleMusic.stop();
	}

	@Override
	public void startEndMusic() {
		stopBackgroundMusic();
		stopTitleMusic();

		Music endMusic = getMusic( Sounds.END_THEME );
		if (endMusic == null){
			return;
		}
		if ( musicVolume > 0.0f && !endMusic.isPlaying() && !paused ) {
			endMusic.setLooping( true );
			endMusic.setVolume( musicVolume );
			endMusic.play();
		}
	}

	@Override
	public void stopEndMusic() {
		Music endMusic = getMusic( Sounds.END_THEME );
		if (endMusic == null) return;
		if ( endMusic.isPlaying() ) endMusic.stop();
	}

	@Override
	public void startBackgroundMusic() {
		stopTitleMusic();
		stopEndMusic();

		if ( musicVolume > 0.0f && backgroundMusicList.size() > 0 && !paused ) {
			if ( backgroundPlaying < 0 ) backgroundPlaying = 0;

			Music music = backgroundMusicList.get( backgroundPlaying );
			if (music == null) return;
			if ( !music.isPlaying() ) {
				if ( songEnded == 0 ) {
					songEnded = System.currentTimeMillis();
					backgroundPlaying = (backgroundPlaying + 1) >= backgroundMusicList.size() ? 0 : backgroundPlaying + 1;

					music = backgroundMusicList.get( backgroundPlaying );
				}

				if ( songEnded == 0 || (songEnded + (timeBetweenSongs * 1000)) < System.currentTimeMillis() ) {
					music.setLooping( false );
					music.setVolume( musicVolume );
					music.play();

					songEnded = 0;
				}
			}
		}
	}

	@Override
	public void stopBackgroundMusic() {
		if ( backgroundPlaying < 0 ) return;

		for ( Music m : backgroundMusicList ) {
			if ( m.isPlaying() ) m.stop();
		}

		backgroundPlaying = -1;
	}

	@Override
	public void pauseBackgroundMusic() {
		paused = true;
		
		if ( backgroundPlaying >= 0 ) backgroundMusicList.get( backgroundPlaying ).pause();
		Music music;
		
		music = getMusic( Sounds.TITLE_THEME );
		if (music != null){ 
			music.pause();
		}
		music = getMusic( Sounds.END_THEME );
		if (music != null){ 
			music.pause();
		}
	}

	@Override
	public void resumeBackgroundMusic() {
		paused = true;

		if ( backgroundPlaying >= 0 ) backgroundMusicList.get( backgroundPlaying ).play();

		getMusic( Sounds.TITLE_THEME ).play();
		getMusic( Sounds.END_THEME ).play();
	}

	@Override
	public void setListenerPosition( float x, float y ) {
		listenerX = x;
		listenerY = y;
	}

	@Override
	public boolean playSound( String soundName, float x, float y ) {
		return playSound( soundName, x, y, false );
	}

	@Override
	public boolean playSound( String soundName, float x, float y, boolean blocking ) {
		if ( soundVolume > 0.0f ) {
			if ( soundsMap.containsKey( soundName ) ) {
				// TODO

				// angle -> pan
				double angle = Math.toDegrees( Math.atan2( x - listenerX, y - listenerY ) );
				if ( angle < 0 ) angle += 360;

				float pan = (float) Math.sin( Math.toRadians( angle ) );

				// distance
				double difx = (x - listenerX) * (x - listenerX);
				double dify = (y - listenerY) * (y - listenerY);
				float distance = (float) Math.sqrt( difx + dify );

				float distanceVolume = soundVolume - (distance / maxSoundDistance);
				if ( distanceVolume > soundVolume ) distanceVolume = soundVolume;
				if ( distanceVolume < 0.0f ) distanceVolume = 0.0f;

				soundsMap.get( soundName ).play( distanceVolume, 1, pan );

				return true;
			}
		}

		return false;
	}

	@Override
	public void shutdown() {
		Music music;
		// unloading Title music
		stopTitleMusic();
		music = getMusic( Sounds.TITLE_THEME );
		if (music != null){
			music.dispose();
		}

		// unloading End music
		stopEndMusic();
		music = getMusic( Sounds.END_THEME );
		if (music != null){
			music.dispose();
		}
		
		// unloading Background music
		stopBackgroundMusic();

		for ( Music m : backgroundMusicList ) {
			if ( m.isPlaying() ) m.stop();
			m.dispose();
		}

		// unloading sounds
		if ( soundsMap.size() > 0 ) {
			for ( Sound s : soundsMap.values() ) {
				s.stop();
				s.dispose();
			}
		}
	}

	@Override
	public boolean isMuted() {
		return muted;
	}

	@Override
	public void setMuted( boolean muted ) {
		this.muted = muted;
	}

}
