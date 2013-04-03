package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animated implements EntityComponent, Renderable {
	private static final long serialVersionUID = 1L;

	protected Animation animation;
	protected float timeState;
	protected int x, y;

	public Animated( TextureRegion[] animation ) {
		this( animation, 1f );
	}

	public Animated( TextureRegion[] animation, float duration ) {
		this.animation = new Animation( duration, animation );
		this.timeState = 0f;

		this.x = this.y = 0;
	}

	@Override
	public void render( Screen screen ) {
		timeState += Gdx.graphics.getDeltaTime();
		screen.getGraphics().draw( animation.getKeyFrame( timeState, true ), x, y );
	}

	public void setPosition( int x, int y ) {
		this.x = x;
		this.y = y;
	}

	public void move( int x, int y ) {
		this.x += x;
		this.y += y;
	}
}
