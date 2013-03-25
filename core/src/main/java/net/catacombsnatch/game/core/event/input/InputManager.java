package net.catacombsnatch.game.core.event.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.InputEvent.InputSource;


public class InputManager extends InputAdapter {
	
	public InputManager() {
		
	}
	
	public static boolean isKeyboardEnabled() {
		return Gdx.input.isPeripheralAvailable(Peripheral.HardwareKeyboard);
	}
	
	
	/* -------- LibGDX Input events -------- */

	@Override
	public boolean keyDown(int keycode) {
		KeyPressedEvent event = new KeyPressedEvent(InputSource.KEYBOARD, keycode);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean keyUp(int keycode) {
		KeyReleaseEvent event = new KeyReleaseEvent(InputSource.KEYBOARD, keycode);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean keyTyped(char character) {
		KeyTypeEvent event = new KeyTypeEvent(InputSource.KEYBOARD, character);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}
	
}
