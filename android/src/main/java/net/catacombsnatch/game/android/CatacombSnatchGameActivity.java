package net.catacombsnatch.game.android;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.GamePlatformHelper;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CatacombSnatchGameActivity extends AndroidApplication implements GamePlatformHelper {
	
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
       config.useCompass = false;
       config.useAccelerometer = false;
       config.useGL20 = true;
       initialize(new Game(), config);
   }
	
	@Override
	public void initCursor() {
	}
	
}
