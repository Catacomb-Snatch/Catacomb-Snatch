package net.catacombsnatch.game.core;

import java.util.Locale;

import net.catacombsnatch.game.core.gui.MenuStack;
import net.catacombsnatch.game.core.gui.menu.TitleScreen;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.sound.GdxSoundPlayer;
import net.catacombsnatch.game.core.sound.ISoundPlayer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Game implements ApplicationListener {
	public static Language language;

	private Screen screen;
	private MenuStack menuStack;
	public static ISoundPlayer soundPlayer;

	public void create() {
		language = new Language( new Locale( "en" ) );
		soundPlayer = new GdxSoundPlayer();
		if ( !Art.loadResources() ) Gdx.app.exit();

		screen = new Screen();
		menuStack = new MenuStack();

		menuStack.add( new TitleScreen() );
		//soundPlayer.startTitleMusic();
	}

	public void resize( int width, int height ) {
	}

	public void render() {
		screen.getGraphics().begin();
		screen.clear( Color.BLACK );

		if ( !menuStack.isEmpty() ) menuStack.peek().render( screen );

		screen.getGraphics().end();
		//soundPlayer.startBackgroundMusic();
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
