package net.catacombsnatch.game.core;

import java.util.Locale;

import net.catacombsnatch.game.core.gui.MenuStack;
import net.catacombsnatch.game.core.gui.menu.GuiMenu;
import net.catacombsnatch.game.core.gui.menu.TitleScreen;
import net.catacombsnatch.game.core.input.InputManager;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.resources.Options;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;
import net.catacombsnatch.game.core.sound.NoSoundPlayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Game implements ApplicationListener {
	public static final String VERSION = "${pom.version}.{buildNumber}";

	public static Language language;

	private Screen screen;

	private static MenuStack menuStack;

	public static ISoundPlayer sound;
	public static InputManager input;

	@Override
	public void create() {
		Options.load();
		language = new Language( new Locale( "en" ) );

		if ( !Art.loadResources() ) Gdx.app.exit();

		Fonts.init();

		try {
			sound = new GdxSoundPlayer();
		} catch ( Exception e ) {
			sound = new NoSoundPlayer();
		}

		input = new InputManager();

		screen = new Screen();
		menuStack = new MenuStack();

		switchTo( TitleScreen.class );
	}

	public static void exitAll() {
		while ( !menuStack.isEmpty() )
			menuStack.pop();

		Gdx.app.exit();
	}

	public static synchronized <T extends GuiMenu> T switchTo( Class<T> menu ) {
		T instance = null;

		try {
			instance = menu.newInstance();
			menuStack.add( instance );

		} catch ( Exception e ) {
			System.err.println( "Error switching to menu '" + menu + "': " + e.getMessage() );
			e.printStackTrace();
		}

		Gdx.input.setInputProcessor( instance );
		return instance;
	}

	public static synchronized <T extends GuiMenu> T returnTo( Class<T> menu ) {
		menuStack.pop();
		return switchTo( menu );
	}

	public static synchronized void exitMenu() {
		menuStack.pop();

		if ( !menuStack.isEmpty() ) Gdx.input.setInputProcessor( menuStack.peek() );
	}

	@Override
	public void resize( int width, int height ) {}

	@Override
	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );

		if ( !menuStack.isEmpty() ) menuStack.peek().render( screen );

		if ( Options.getBoolean( Options.DRAW_FPS, true ) ) {
			Fonts.GOLD.draw( screen.getGraphics(), Integer.toString( Gdx.graphics.getFramesPerSecond() ) + " FPS", 2, Screen.getHeight() - 2 - 8 );
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
		Fonts.unload();

		input.shutdown();
		sound.shutdown();

		Options.save();
	}
}
