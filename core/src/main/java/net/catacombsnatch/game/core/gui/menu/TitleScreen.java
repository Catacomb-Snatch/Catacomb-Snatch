package net.catacombsnatch.game.core.gui.menu;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.input.Input;
import net.catacombsnatch.game.core.resources.Fonts;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;

public class TitleScreen extends GuiMenu {
	private int selected = 0;
	private List<String> menu;

	private Entity charEntity;

	public TitleScreen() {
		menu = new ArrayList<String>();
		menu.add( "START DEMO" );
		menu.add( "START GAME" );
		menu.add( "OPTIONS" );
		menu.add( "EXIT GAME" );

		charEntity = new EntityManager().createEntity();
		charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );
		scrolled( 0 );

		Game.sound.startTitleMusic();
	}

	public void exit() {
		Game.sound.stopTitleMusic();
	}

	public void render( Screen screen ) {
		screen.getGraphics().draw( Art.titleScreen, 0, 0 );

		for ( int s = 0; s < menu.size(); s++ )
			Fonts.GOLD.drawMultiLine( screen.getGraphics(), menu.get( s ), Screen.getWidth() / 2, 128 - 24 * s, 0, HAlignment.CENTER );

		charEntity.getComponent( Animated.class ).render( screen );
	}

	public boolean scrolled( int amount ) {
		selected -= amount;

		if ( selected > menu.size() - 1 )
			selected = 0;
		else if ( selected < 0 ) selected = menu.size() - 1;

		charEntity.getComponent( Animated.class ).setPosition( (Screen.getWidth() - 200) / 2, 128 - 8 + (-selected * 24) );

		return true;
	}

	public boolean keyDown( int key ) {
		if ( key == Keys.ENTER ) {
			switch ( selected ) {
			case 0:
				Game.switchTo( TestGameMenu.class );
				break;

			case 1:
			case 2:
				break;

			case 3:
				Game.exitAll();
			}
		} else if ( key == Input.up ) {
			scrolled( 1 );
		} else if ( key == Input.down ) {
			scrolled( -1 );
		}

		return true;
	}
}
