package net.catacombsnatch.game;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new CatacombSnatch(new PlatformDependent() {
			@Override
			public void create() {

			}

			@Override
			public Object[] createPlatformObjects() {
				return new Object[0];
			}

			@Override
			public void dispose() {

			}
		}), config);
	}
}
