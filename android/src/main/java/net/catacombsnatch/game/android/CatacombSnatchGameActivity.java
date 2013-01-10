package net.catacombsnatch.game.android;

import net.catacombsnatch.game.core.CatacombSnatchGame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CatacombSnatchGameActivity extends AndroidApplication {
	
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
       config.useGL20 = true;
       initialize(new CatacombSnatchGame(), config);
   }
}
