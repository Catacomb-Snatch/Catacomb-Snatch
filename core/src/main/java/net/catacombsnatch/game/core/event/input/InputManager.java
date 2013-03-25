package net.catacombsnatch.game.core.event.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.InputEvent.InputSource;


public class InputManager extends InputAdapter implements ControllerListener {
	
	public InputManager() {
		
	}
	
	public static boolean isKeyboardEnabled() {
		return Gdx.input.isPeripheralAvailable(Peripheral.HardwareKeyboard);
	}
	
	public Key getKeyForSource(InputSource source, int keycode) {
		// TODO
		return Key.UNKNOWN;
	}
	
	
	/* -------- Keyboard and Mouse input events -------- */

	@Override
	public boolean keyDown(int keycode) {
		InputSource source = InputSource.KEYBOARD;
		KeyPressedEvent event = new KeyPressedEvent(source, getKeyForSource(source, keycode), null);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean keyUp(int keycode) {
		InputSource source = InputSource.KEYBOARD;
		KeyReleaseEvent event = new KeyReleaseEvent(source, getKeyForSource(source, keycode), null);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean keyTyped(char character) {
		KeyTypeEvent event = new KeyTypeEvent(InputSource.KEYBOARD, character);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}
	
	
	/* -------- Controller input events -------- */

	@Override
	public void connected(Controller controller) {
		EventManager.callEvent(new ControllerConnectEvent(controller));
	}

	@Override
	public void disconnected(Controller controller) {
		EventManager.callEvent(new ControllerDisconnectEvent(controller));
	}

	@Override
	public boolean buttonDown(Controller controller, int button) {
		InputSource source = InputSource.CONTROLLER;
		KeyPressedEvent event = new KeyPressedEvent(source, getKeyForSource(source, button), controller);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean buttonUp(Controller controller, int button) {
		InputSource source = InputSource.CONTROLLER;
		KeyReleaseEvent event = new KeyReleaseEvent(source, getKeyForSource(source, button), controller);
		EventManager.callEvent(event);
		
		return !event.isCancelled();
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false; // TODO
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false; // TODO
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false; // TODO
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false; // TODO
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false; // TODO
	}
	
}
