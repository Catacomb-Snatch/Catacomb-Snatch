package net.catacombsnatch.game.core;

import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.resources.Options;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.scene.scenes.TitleScreen;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;
import net.catacombsnatch.game.core.sound.NoSoundPlayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Game implements ApplicationListener {
	public final static String TAG = "[Core]";

	protected InputManager input;
	protected Language language;
	protected SceneManager sceneManager;

	public static ISoundPlayer sound;
	
	private Label fpsLabel;

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
		input = new InputManager();
		Gdx.input.setInputProcessor(input);
		Controllers.addListener(input);
		
		sceneManager = new SceneManager();
		
		// Dive in :)
		SceneManager.switchTo(TitleScreen.class);
		
		fpsLabel = new Label("FPS", Art.skin);
	}

	@Override
	public void resize( int width, int height ) {
		Screen.resize(width, height);
		sceneManager.resize();
	}

	@Override
	public void render() {
		Screen.clear();

		Scene current = SceneManager.getCurrent();
		if (current != null) {
			current.render();
			
			if ( Options.getBoolean( Options.DRAW_FPS, true ) ) {
				fpsLabel.setText(Gdx.graphics.getFramesPerSecond() + " FPS");
				fpsLabel.draw(current.getSpriteBatch(), 1);
			}
			
			current.getSpriteBatch().end();
		}
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
