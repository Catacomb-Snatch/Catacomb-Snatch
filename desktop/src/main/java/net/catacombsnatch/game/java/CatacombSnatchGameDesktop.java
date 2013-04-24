package net.catacombsnatch.game.java;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.PlatformDependent;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.BufferUtils;

public class CatacombSnatchGameDesktop {
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;
	
	public static void main( String[] args ) {
		System.out.println("Starting game in DESKTOP mode!");
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL20 = true;
		config.title = "Catacomb-Snatch";
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;
		
		new LwjglApplication( 
			new Game(
			new PlatformDependent (){
				@Override
				public void create() {
					// Set game cursor
					try {
						int size = 16, center = (size / 2);
						IntBuffer buffer = BufferUtils.newIntBuffer(size * size);
			
						int x = 0, y = 0;
						for (int n = 0; n < buffer.limit(); n++) {
							if ((x == center || y == center) &&
								(x < center-1 || y < center-1 ||
								 x > center+1 || y > center+1)) {
								buffer = buffer.put(n, 0xFFFFFFFF);
							}
							
							x++;
							if(x == size) {
								x = 0;
								y++;
							}
						}
			
						Mouse.setNativeCursor(new Cursor(size, size, center, center, 1, buffer, null));
						
					} catch (LWJGLException e) {
						System.err.print("Error setting native cursor!\n" + e);
					}	
				}
					
				@Override
				public Object[] createPlatformObjects() {	
					throw new UnsupportedOperationException("Unimplemented");
				}
						
				@Override
				public void dispose() {	
				}
			})
		,config );
	}
}
