package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Tickable;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animated extends EntityComponent implements Renderable, Tickable {
	protected Animation animation;
	protected float timeState;
	protected int x, y;

	public Animated( long id, TextureRegion[] animation ) {
		this(id, animation, 1f );
	}

	public Animated( long id, TextureRegion[] animation, float duration ) {
		this(id, animation, 1f, Animation.LOOP);
	}
		
	public Animated( long id, TextureRegion[] animation, float duration, int mode ) {
		super(id);
		
		this.animation = new Animation( duration, new Array<TextureRegion>(animation), mode );
		this.timeState = 0f;

		this.x = this.y = 0;
	}

	@Override
	public void tick(float delta) {
		timeState += delta;
	}
	
	@Override
	public void render( Scene scene ) {
		scene.getSpriteBatch().draw( animation.getKeyFrame( timeState ), x, y );
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
