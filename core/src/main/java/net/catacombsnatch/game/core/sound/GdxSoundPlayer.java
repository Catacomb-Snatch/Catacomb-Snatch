package net.catacombsnatch.game.core.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GdxSoundPlayer implements ISoundPlayer{

	private float listenerX = 0.0f;
	private float listenerY = 0.0f;
	
	private float maxSoundDistance = 200.0f;
	
	private boolean paused = false;
	private boolean muted = false;
	private float musicVolume = 0.5f;
	private float soundVolume = 1.0f;
	
	private long timeBetweenSongs = 10; // in seconds
	private long songEnded = 0;
	
	private static List<Music> backgroundMusicList;
	
	private static Music titleMusic;
	private static boolean titleMusicIsPlaying = false;
	private static Music endMusic;
	private static boolean endMusicIsPlaying = false;
	private static Music backgroundMusic;
	private static boolean backgroundIsPlaying = false;
	private static int backgroundPlaying;
	
	private static Map<String, Sound> soundsMap;
	
	public GdxSoundPlayer(){
		//Load music
		titleMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/ThemeTitle.ogg"));
		endMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/ThemeEnd.ogg"));
		backgroundMusicList = new ArrayList<Music>();
		backgroundMusicList.add(Gdx.audio.newMusic(Gdx.files.internal("sound/Background 1.ogg")));
		backgroundMusicList.add(Gdx.audio.newMusic(Gdx.files.internal("sound/Background 2.ogg")));
		backgroundMusicList.add(Gdx.audio.newMusic(Gdx.files.internal("sound/Background 3.ogg")));
		backgroundMusicList.add(Gdx.audio.newMusic(Gdx.files.internal("sound/Background 4.ogg")));
		
		//Load sounds
		soundsMap = new HashMap<String, Sound>();
		soundsMap.put("Shot1",Gdx.audio.newSound(Gdx.files.internal("sound/Shot 1.wav")));
		soundsMap.put("Explosion",Gdx.audio.newSound(Gdx.files.internal("sound/Explosion.wav")));
		
	}

	public void startTitleMusic() {
		this.stopBackgroundMusic();
		this.stopEndMusic();
		if (musicVolume > 0.0f && !titleMusic.isPlaying() && !paused){
			titleMusic.setLooping(true);
			titleMusic.setVolume(musicVolume);
			titleMusic.play();
			titleMusicIsPlaying = true;
		}
		
	}
	
	public void stopTitleMusic(){
		if (titleMusicIsPlaying){
			if (titleMusic.isPlaying()) titleMusic.stop();
			titleMusicIsPlaying = false;
		}
	}

	public void startEndMusic() {
		this.stopBackgroundMusic();
		this.stopTitleMusic();
		if (musicVolume > 0.0f && !endMusic.isPlaying() && !paused){
			endMusic.setLooping(true);
			endMusic.setVolume(musicVolume);
			endMusic.play();
			endMusicIsPlaying = true;
		}		
	}
	
	public void stopEndMusic(){
		if (endMusicIsPlaying){
			if (endMusic.isPlaying()) endMusic.stop();
			endMusicIsPlaying = false;
		}
	}

	public void startBackgroundMusic() {
		this.stopTitleMusic();
		this.stopEndMusic();
		if (musicVolume > 0.0f && backgroundMusicList.size() > 0 && !paused){
			backgroundMusic = backgroundMusicList.get(backgroundPlaying);
			if (!backgroundMusic.isPlaying()){
				if (backgroundIsPlaying){
					if (songEnded == 0){
						songEnded = System.currentTimeMillis();
						if ((backgroundPlaying+1) >= backgroundMusicList.size()){
							backgroundPlaying = 0;
						}else{
							backgroundPlaying++;
						}
						backgroundMusic = backgroundMusicList.get(backgroundPlaying);
					}
				}	
				if (songEnded == 0 || (songEnded + (timeBetweenSongs*1000)) < System.currentTimeMillis()){
					backgroundMusic.setLooping(false);
					backgroundMusic.setVolume(musicVolume);
					backgroundMusic.play();
					backgroundIsPlaying = true;
					songEnded = 0;
				}
			}
		}
	}

	public void stopBackgroundMusic() {
		if (backgroundIsPlaying){
			for (Music m : backgroundMusicList){
				if(m.isPlaying()) m.stop();
			}
			backgroundPlaying = 0;
			backgroundIsPlaying = false;
		}
	}

	public void pauseBackgroundMusic() {
		paused = true;
		if (backgroundIsPlaying){
			backgroundMusic.pause(); 
		}
		if (titleMusicIsPlaying){
			titleMusic.pause();
		}
		if (endMusicIsPlaying){
			endMusic.pause();
		}
	}

	public void resumeBackgroundMusic() {
		paused = false;
		if (backgroundIsPlaying){
			backgroundMusic.play();
		}
		if (titleMusicIsPlaying){
			titleMusic.play();
		}
		if (endMusicIsPlaying){
			endMusic.play();
		}
	}

	public void setListenerPosition(float x, float y) {
		listenerX = x;
		listenerY = y;
	}

	public boolean playSound(String soundName, float x, float y) {
		return playSound(soundName, x, y, false);
	}

	public boolean playSound(String soundName, float x, float y,	boolean blocking) {
		
		if (soundVolume > 0.0f){
			if (soundsMap.containsKey(soundName)){
				//todo
				// angle -> pan
				double angle = Math.toDegrees(Math.atan2(x - listenerX, y - listenerY));
				if(angle < 0){
			        angle += 360;
			    }
				//System.out.println("angle "+angle);
				float pan = (float)Math.sin(Math.toRadians(angle));
				//System.out.println("pan "+pan);
				//distance
				double difx = (x - listenerX) * (x - listenerX);
				double dify = (y - listenerY) * (y - listenerY);
				float distance = (float) Math.sqrt(difx + dify);
				System.out.println("dis "+distance);
				
				float distanceVolume = 1.0f;
				distanceVolume = 1.0f-(distance/maxSoundDistance);
				if (distanceVolume > 1.0f) distanceVolume = 1.0f; 
				if (distanceVolume < 0.0f) distanceVolume = 0.0f;
				System.out.println("disVol "+distanceVolume);
				soundsMap.get(soundName).play(distanceVolume,1,pan);
				return true;
			}
		}
		
		return false;
	}

	public void shutdown() {
		// unloading Title music
		this.stopTitleMusic();
		titleMusic.dispose();
		// unloading End music
		this.stopEndMusic();
		endMusic.dispose();
		// unloading Background music
		stopBackgroundMusic();
		for (Music m : backgroundMusicList){
			if(m.isPlaying()) m.stop();
			m.dispose();
		}
		// unloading sounds
		if (soundsMap.size() > 0){
			for (Map.Entry<String,Sound> s : soundsMap.entrySet()){
				s.getValue().stop();
				s.getValue().dispose();
			}
		}
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
}
