package net.catacombsnatch.game.core;

import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.resources.Options;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.scene.scenes.TitleScreen;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;
import net.catacombsnatch.game.core.sound.NoSoundPlayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Game implements ApplicationListener {
	public final static String TAG = "[Core]";

	protected Language language;
	protected Screen screen;
	protected SceneManager sceneManager;

	public static ISoundPlayer sound;

	@Override
	public void create() {
		// Load static content
		Gdx.app.setLogLevel(Application.LOG_INFO | Application.LOG_DEBUG | Application.LOG_ERROR);
		
		Options.load();
		Language.set("en");

		if ( !Art.loadResources() ) Gdx.app.exit();
		
		try {
			sound = new GdxSoundPlayer();
		} catch ( Exception e ) {
			Gdx.app.log(TAG, "Could not enable sound system, falling back to NoSound!", e);
			sound = new NoSoundPlayer();
		}

		// Load main managers
		sceneManager = new SceneManager();
		screen = new Screen();
		
		// Dive in :)
		SceneManager.switchTo(TitleScreen.class);
	}

	@Override
	public void resize( int width, int height ) {}

	@Override
	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );

		Scene current = SceneManager.getCurrent();
		if (current != null) {
			current.render( screen );
		}

		if ( Options.getBoolean( Options.DRAW_FPS, true ) ) {
			// Fonts.GOLD.draw( screen.getGraphics(), Integer.toString( Gdx.graphics.getFramesPerSecond() ) + " FPS", 2, Screen.getHeight() - 2 - 8 );
		}
		
		screen.getGraphics().end();
	}

	@Override
	public void pause() {
		sound.pauseBackgroundMusic();
	}

	@Override
	public void resume() {
		sound.resumeBackgroundMusic();
	}

	@Override
	public void dispose() {
		Art.unloadResources();

		sound.shutdown();

		Options.save();
	}
}
