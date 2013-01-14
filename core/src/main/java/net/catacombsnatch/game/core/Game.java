package net.catacombsnatch.game.core;

import java.util.Locale;

import net.catacombsnatch.game.core.gui.MenuStack;
import net.catacombsnatch.game.core.gui.menu.GuiMenu;
import net.catacombsnatch.game.core.gui.menu.TitleScreen;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;
import net.catacombsnatch.game.core.sound.NoSoundPlayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Game implements ApplicationListener {
	public static Language language;

	private Screen screen;

	private static MenuStack menuStack;

	public static ISoundPlayer soundPlayer;

	public void create() {
		language = new Language( new Locale( "en" ) );
		if ( !Art.loadResources() ) Gdx.app.exit();
		Fonts.init();
		try {
			soundPlayer = new GdxSoundPlayer();
		}catch (Exception e){
			soundPlayer = new NoSoundPlayer();
		}
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

	public void resize( int width, int height ) {}

	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );

		if ( !menuStack.isEmpty() ) menuStack.peek().render( screen );

		screen.getGraphics().end();
	}

	public void pause() {
		soundPlayer.pauseBackgroundMusic();
	}

	public void resume() {
		soundPlayer.resumeBackgroundMusic();
	}

	public void dispose() {
		Art.unloadResources();
		Fonts.unload();
		soundPlayer.shutdown();
	}
}
