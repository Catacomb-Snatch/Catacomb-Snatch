package net.catacombsnatch.game.core;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.core.resource.options.Options;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.scene.scenes.TitleScreen;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;
import net.catacombsnatch.game.core.sound.NoSoundPlayer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.BufferUtils;

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
		EventManager.registerListener(this);
		
		sceneManager = new SceneManager();
		
		//Set cursor when on PC
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			//Desktop LibGDX uses LWJGL. LWJGL isn't on the core build path. 
			//We're in core right now. There are 3 variants of making this possible: 
			//1. Putting lwjgl into the dependencies / build path 
			//Cons: Core will get more Desktop dependent. 
			//2. Creating a method in CatacombSnatchGameDestkop for this 
			//Cons: Risk of cross-project messup. 
			//3. Using le old Classloader class finding method finding variant. 
			//Cons: Code less readable. 
			try {
				Class cMouse = ClassLoader.getSystemClassLoader().loadClass("org.lwjgl.input.Mouse");
				Class cCursor = ClassLoader.getSystemClassLoader().loadClass("org.lwjgl.input.Cursor");
				Object cursor = null;
				
				IntBuffer buffer = BufferUtils.newIntBuffer(32*32);
				int i = 0;
				for (int y = 0; y < 32; y++) {
					for (int x = 0; x < 32; x++) {
						if (x >= 15 && x <= 16) {
							buffer = buffer.put(i, 0xffffffff);
						}
						if (y >= 15 && y <= 16) {
							buffer = buffer.put(i, 0xffffffff);
						}
						if ((x >= 12 && x <= 19) && (y >= 12 && y <= 19)) {
							buffer = buffer.put(i, 0);
						}
						i++;
					}
				}
				
				cursor = cCursor.getConstructor(int.class, int.class, int.class, int.class, int.class, IntBuffer.class, IntBuffer.class)
				.newInstance(32, 32, 16, 16, 1, buffer, null);
				
				Method mSetNativeCursor = cMouse.getMethod("setNativeCursor", cCursor);
				mSetNativeCursor.invoke(null, cursor);
			} catch (Exception e) {
				Gdx.app.log(TAG, "Could not set new cursor!", e);
			}
		}
		
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
			
			if (Options.getOptions().get(Options.DEBUG, true)) {
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
		SceneManager.exitAll();
		
		Art.unloadResources();
		sound.shutdown();
		Options.save();
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		switch(event.getKey()) {
		case SCREENSHOT:
			String path = "";
			try {
				String rawpath = Game.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				path = URLDecoder.decode(rawpath, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			File dir = new File(path).getParentFile();
			File logfile = new File(dir, "screen_"+(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime()))+".png");
			Screen.saveScreenshot(Gdx.files.absolute(logfile.getPath()));
			break;
			
		default:
			// Nothing to do here
		}
	}
}
