package net.catacombsnatch.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import net.catacombsnatch.game.event.EventHandler;
import net.catacombsnatch.game.event.EventManager;
import net.catacombsnatch.game.event.input.InputManager;
import net.catacombsnatch.game.event.input.Key;
import net.catacombsnatch.game.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.player.LocalPlayer;
import net.catacombsnatch.game.player.Player;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.resource.Language;
import net.catacombsnatch.game.resource.options.DefaultOptions;
import net.catacombsnatch.game.resource.options.Options;
import net.catacombsnatch.game.resource.options.PreferenceOptions;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.scene.SceneManager;
import net.catacombsnatch.game.scene.scenes.TitleScreen;
import net.catacombsnatch.game.screen.Screen;
import net.catacombsnatch.game.sound.GdxSoundPlayer;
import net.catacombsnatch.game.sound.ISoundPlayer;
import net.catacombsnatch.game.sound.NoSoundPlayer;

import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.Calendar;

public class CatacombSnatch extends ApplicationAdapter {
    public final static String TAG = "[Core]";

    protected InputManager input;
    protected Language language;
    protected SceneManager sceneManager;

    protected static Player[] localPlayers;

    public static ISoundPlayer sound;
    public static Options options;

    private Label fpsLabel;

    private final PlatformDependent platform;

    public CatacombSnatch(PlatformDependent platform) {
        this.platform = platform;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO | Application.LOG_DEBUG | Application.LOG_ERROR);

        if (!Art.loadResources()) Gdx.app.exit();

        // Load options
        options = new PreferenceOptions("catacombsnatch-options.xml");
        DefaultOptions.setDefaults();

        // Load sound system
        try {
            sound = new GdxSoundPlayer();
        } catch (Exception e) {
            Gdx.app.log(TAG, "Could not enable sound system, falling back to NoSound!", e);
            sound = new NoSoundPlayer();
        }

        // Load main managers
        sceneManager = new SceneManager();

        input = new InputManager();
        Gdx.input.setInputProcessor(input);
        platform.create();

        Controllers.addListener(input);
        EventManager.registerListener(this);

        // Dive in :)
        localPlayers = new Player[4];
        localPlayers[0] = new LocalPlayer();

        SceneManager.switchTo(TitleScreen.class);

        fpsLabel = new Label("FPS", Art.skin);
    }

    @Override
    public void resize(int width, int height) {
        Screen.resize(width, height);
        sceneManager.update(true);
    }

    @Override
    public void render() {
        Screen.clear();

        Scene last = null;

        if (SceneManager.isDrawAllEnabled()) {
            for (Scene scene : sceneManager.getScenes()) {
                if (scene.hasBeenClosed()) continue;

                scene.tick(Gdx.graphics.getDeltaTime());
                scene.getBatch().end();

                last = scene;
            }
        } else {
            last = SceneManager.getCurrent();

            if (last != null) {
                last.tick(Gdx.graphics.getDeltaTime());
                last.getBatch().end();
            }
        }

        if (last != null && (Boolean) DefaultOptions.DEBUG.get()) {
            last.getBatch().begin();

            fpsLabel.setText(Gdx.graphics.getFramesPerSecond() + " FPS");
            fpsLabel.draw(last.getBatch(), 1);

            last.getBatch().end();
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
        platform.dispose();
        options.save();
    }

    @EventHandler
    public void keyPressed(KeyPressedEvent event) {
        if (event.getKey() != Key.SCREENSHOT) return;

        String filename = "screen_" + (DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime())) + ".png";

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
                File logfile = new File(dir, filename);
                Screen.saveScreenshot(Gdx.files.absolute(logfile.getPath()));
                break;

            default:
                Screen.saveScreenshot(Gdx.files.external(filename));
        }
    }

    /**
     * Returns all local players.<br />
     * <b>The maximum number of <em>local</em> players is 4!</b>
     *
     * @return A {@link Player Player[]} containing all local players.
     */
    public static Player[] getLocalPlayers() {
        return localPlayers;
    }
}
