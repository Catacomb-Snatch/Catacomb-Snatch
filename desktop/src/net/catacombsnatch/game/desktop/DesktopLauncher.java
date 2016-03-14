package net.catacombsnatch.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.BufferUtils;
import net.catacombsnatch.game.CatacombSnatch;
import net.catacombsnatch.game.PlatformDependent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.nio.IntBuffer;

public class DesktopLauncher {
	public static final int GAME_WIDTH = 512;
	public static final int GAME_HEIGHT = GAME_WIDTH * 3 / 4;

	public static void main (String[] arg) {
		System.out.println("Starting game in DESKTOP mode!");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.title = "Catacomb Snatch";
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;

		new LwjglApplication(
				new CatacombSnatch(
						new PlatformDependent(){
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
