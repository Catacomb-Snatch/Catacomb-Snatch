package net.catacombsnatch.game.core.event.input;

import java.util.HashMap;
import java.util.Map;

import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.InputEvent.InputSource;
import net.catacombsnatch.game.core.event.input.events.ControllerConnectEvent;
import net.catacombsnatch.game.core.event.input.events.ControllerDisconnectEvent;
import net.catacombsnatch.game.core.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.core.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.core.event.input.events.KeyTypeEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class InputManager extends InputAdapter implements ControllerListener {
	protected static KeyMap keyboard;
	protected static Map<Controller, KeyMap> controllers;
	static {
		keyboard = KeyMap.defaultMapping;
		controllers = new HashMap<Controller, KeyMap>();
	}
	
	public static boolean isKeyboardEnabled() {
		return Gdx.input.isPeripheralAvailable(Peripheral.HardwareKeyboard);
	}
	
	public static void registerMapping(InputSource source, KeyMap map, Controller controller) {
		if(source.equals(InputSource.KEYBOARD)) {
			keyboard = map;
			
		} else if(source.equals(InputSource.CONTROLLER)) {
			if(controller == null) throw new GdxRuntimeException("Controller should not be null");
			
			controllers.put(controller, map);
		}
		
		throw new GdxRuntimeException("Only KEYBOARD and CONTROLLER allowed as sources!");
	}
	
	public Key getKeyForSource(InputSource source, int keycode) {
		Key k = null;
		
		if(source.equals(InputSource.KEYBOARD)) k = keyboard.getKeyFor(source, keycode);
		
		// TODO
		
		return k != null ? k : Key.UNKNOWN;
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
