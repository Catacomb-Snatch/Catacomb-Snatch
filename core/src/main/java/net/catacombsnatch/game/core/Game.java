package net.catacombsnatch.game.core;

import java.io.File;
import java.net.URLDecoder;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.Key;
import net.catacombsnatch.game.core.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.core.multiplayer.sfs.SFSClient;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.resource.Language;
import net.catacombsnatch.game.core.resource.options.DefaultOptions;
import net.catacombsnatch.game.core.resource.options.Options;
import net.catacombsnatch.game.core.resource.options.PreferenceOptions;
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
	public static Options options;
	
	private Label fpsLabel;
	
	protected SFSClient sfsClient;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_INFO | Application.LOG_DEBUG | Application.LOG_ERROR);
		
		// Load static content
		Language.set("en");
		
		if ( !Art.loadResources() ) Gdx.app.exit();
		
		// Load options
		options = new PreferenceOptions("options.xml");
		DefaultOptions.setDefaults();
		
		// Load sound system
		try {
			sound = new GdxSoundPlayer();
		} catch ( Exception e ) {
			Gdx.app.log(TAG, "Could not enable sound system, falling back to NoSound!", e);
			sound = new NoSoundPlayer();
		}

		// Load main managers
		sceneManager = new SceneManager();
		
		input = new InputManager();
		Gdx.input.setInputProcessor(input);
		
		Controllers.addListener(input);
		EventManager.registerListener(this);
		
		//Set cursor when on PC
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			/* Desktop LibGDX uses LWJGL. LWJGL isn't on the core build path. 
			 * We're in core right now. There are 3 variants of making this possible:
			 *
			 * 1. Putting lwjgl into the dependencies / build path 
			 *    Cons: Core will get more Desktop dependent. 
			 * 2. Creating a method in CatacombSnatchGameDestkop for this 
			 *    Cons: Risk of cross-project messup. 
			 * 3. Using le old Classloader class finding method finding variant. 
			 *    Cons: Code less readable.
			 */
			try {
				int size = 16, center = (size / 2);
				IntBuffer buffer = BufferUtils.newIntBuffer(size * size);

				int x = 0, y = 0;
				for (int n = 0; n < buffer.limit(); n++) {
					if ((x == center || y == center) && 
							!(x >= center-1 && y >= center-1 && 
							x <= center+1 && y <= center+1)) {
						buffer = buffer.put(n, 0xFFFFFFFF);
					}
					x++;
					if(x == size) {
						x = 0;
						y++;
					}
				}
				
				Class<?> cCursor = ClassLoader.getSystemClassLoader().loadClass("org.lwjgl.input.Cursor");
				ClassLoader.getSystemClassLoader().loadClass("org.lwjgl.input.Mouse").getMethod("setNativeCursor", cCursor).invoke(null,
					cCursor.getConstructor(int.class, int.class, int.class, int.class, int.class, IntBuffer.class, IntBuffer.class)
					.newInstance(size, size, center, center, 1, buffer, null)
				);
				
			} catch (Exception e) {
				Gdx.app.log(TAG, "Could not set new cursor!", e);
			}
		}
		
		// Dive in :)
		SceneManager.switchTo(TitleScreen.class);
		
		fpsLabel = new Label("FPS", Art.skin);
		sfsClient = new SFSClient();
		//sfsClient.connect("test", "test");
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
			current.render(Gdx.graphics.getDeltaTime());
			
			if ((Boolean) DefaultOptions.DEBUG.get()) {
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
		options.save();
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		if(event.getKey() != Key.SCREENSHOT) return;
		
		switch (Gdx.app.getType()) {
			case Desktop:
				String path = "";
				
				try {
					String rawpath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
					path = URLDecoder.decode(rawpath, "UTF-8");
				} catch (Exception e) {
					Gdx.app.error(TAG, "Error getting path", e);
				}
				
				File dir = new File(path).getParentFile();
				File logfile = new File(dir, "screen_"+(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime()))+".png");
				Screen.saveScreenshot(Gdx.files.absolute(logfile.getPath()));
				break;
				
			default:
				Screen.saveScreenshot(Gdx.files.external("screen_"+(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime()))+".png"));
		}
	}
	
}
