package net.catacombsnatch.game.android;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.PlatformDependent;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class CatacombSnatchGameActivity extends AndroidApplication {
	
	@Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
       config.useCompass = false;
       config.useAccelerometer = false;
       config.useGL20 = true;
       initialize(new Game(new PlatformDependent() {
		
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public Object[] createPlatformObjects() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void create() {
			// TODO Auto-generated method stub
			
		}
	}), config);
   }
	
}
