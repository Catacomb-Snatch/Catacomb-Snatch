package net.catacombsnatch.game.event.input;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.catacombsnatch.game.event.EventManager;
import net.catacombsnatch.game.event.input.events.ControllerConnectEvent;
import net.catacombsnatch.game.event.input.events.ControllerDisconnectEvent;
import net.catacombsnatch.game.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.event.input.events.KeyTypeEvent;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.scene.SceneManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;


public class InputManager implements InputProcessor, ControllerListener {
	public final static String TAG = "[InputManager]";
	
	public static final float OUYA_STICK_DEADZONE = 0.25F;
	
	protected static KeyMap keyboard;
	protected static Map<Controller, KeyMap> controllers;
	protected static Map<Key, Boolean> pressed;
	private static Key lastKey = Key.UNKNOWN;
	static {
		keyboard = KeyMap.KEYBOARD;
		controllers = new HashMap<Controller, KeyMap>();
		pressed = new EnumMap<Key, Boolean>(Key.class);
	}
	
	public static boolean isKeyboardEnabled() {
		return Gdx.input.isPeripheralAvailable(Peripheral.HardwareKeyboard);
	}
	
	public static boolean isPressed(Key key) {
		return pressed.containsKey(key) ? pressed.get(key) : false;
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
	
	public Key getKeyForSource(InputSource source, int keycode, Controller controller) {
		Key k = null;
		
		switch(source) {
			case KEYBOARD:
			case MOUSE:
				k = keyboard.getKeyFor(source, keycode);
				break;
				
			case CONTROLLER:
				if(controller == null) return Key.UNKNOWN;
				
				KeyMap map = controllers.get(controller);
					
				if(map != null) k = map.getKeyFor(source, keycode);
				else k = KeyMap.CONTROLLER.getKeyFor(source, keycode);
		}
		
		return k != null ? k : Key.UNKNOWN;
	}
	
	
	/* -------- Keyboard and Mouse input events -------- */

	@Override
	public boolean keyDown(int keycode) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.keyDown(keycode);
		
		InputSource source = InputSource.KEYBOARD;
		
		Key key = getKeyForSource(source, keycode, null);
		pressed.put(key, true);
		
		KeyPressedEvent event = new KeyPressedEvent(source, key, null);
		EventManager.callEvent(event);
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.keyUp(keycode);
		
		InputSource source = InputSource.KEYBOARD;
		
		Key key = getKeyForSource(source, keycode, null);
		pressed.put(key, false);
		
		KeyReleaseEvent event = new KeyReleaseEvent(source, key, null);
		EventManager.callEvent(event);
		
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.keyTyped(character);
		
		KeyTypeEvent event = new KeyTypeEvent(InputSource.KEYBOARD, character);
		EventManager.callEvent(event);
		
		return true;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.touchDown(x, y, pointer, button);
		
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.touchUp(x, y, pointer, button);
		
		return true;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.touchDragged(x, y, pointer);
		
		return true;
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.mouseMoved(x, y);
		
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		Scene scene = SceneManager.getCurrent();
		if(scene != null) scene.scrolled(amount);
		
		return true;
	}
	
	
	/* -------- Controller input events -------- */

	@Override
	public void connected(Controller controller) {
		Gdx.app.log(TAG, "Controller "+controller.getName()+" connected.");
		EventManager.callEvent(new ControllerConnectEvent(controller));
	}

	@Override
	public void disconnected(Controller controller) {
		Gdx.app.log(TAG, "Controller "+controller.getName()+" disconnected.");
		EventManager.callEvent(new ControllerDisconnectEvent(controller));
	}

	@Override
	public boolean buttonDown(Controller controller, int button) {
		Gdx.app.log(TAG, "Controller: "+controller+" Button: "+button);
		
		InputSource source = InputSource.CONTROLLER;
		
		Key key = getKeyForSource(source, button, controller);
		
		pressed.put(key, true);
		
		KeyPressedEvent event = new KeyPressedEvent(source, key, controller);
		EventManager.callEvent(event);
		
		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int button) {
		InputSource source = InputSource.CONTROLLER;
		
		Key key = getKeyForSource(source, button, controller);
		pressed.put(key, false);
		
		KeyReleaseEvent event = new KeyReleaseEvent(source, key, controller);
		EventManager.callEvent(event);
		
		return true;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		//Gdx.app.log(TAG, "Controller: "+controller.getName()+" Axis: "+axisCode+" Value: "+value);
		if(controller.getName().equals(Ouya.ID)) {
		   // we know it's an Ouya controller, so we can use the Ouya codes
			/*switch(axisCode){
			case Ouya.AXIS_LEFT_X:
				
			break;
			}*/
		   float LS_X = controller.getAxis(Ouya.AXIS_LEFT_X);
		   float LS_Y = controller.getAxis(Ouya.AXIS_LEFT_Y);
		   float RS_X = controller.getAxis(Ouya.AXIS_RIGHT_X);
		   float RS_Y = controller.getAxis(Ouya.AXIS_RIGHT_Y);
		   float L2 = controller.getAxis(Ouya.AXIS_LEFT_TRIGGER);
		   float R2 = controller.getAxis(Ouya.AXIS_RIGHT_TRIGGER);
		   
		   float axisX = LS_X;
		   float axisY = LS_Y;
		   if (axisX * axisX + axisY * axisY < OUYA_STICK_DEADZONE * OUYA_STICK_DEADZONE) {
			   axisX = axisY = 0.0f;
		   }
		   Gdx.app.log(TAG, "Controller: "+controller.getName()+" X: "+axisX+" Y: "+axisY);
		}
		return false; // TODO
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		Gdx.app.log(TAG, "Controller: "+controller.getName()+" povCode: "+povCode+" Value: "+value);
		InputSource source = InputSource.CONTROLLER;
		
		this.axisMoved(controller, 5, controller.getAxis(5));
		this.axisMoved(controller, 6, controller.getAxis(6));
		Gdx.app.log(TAG, "Controller: "+controller.getName()+" Axis5 Value: "+controller.getAxis(5));
		Gdx.app.log(TAG, "Controller: "+controller.getName()+" Axis6 Value: "+controller.getAxis(6));
		
		if (value == PovDirection.south){
			Key key = Key.MOVE_DOWN;
			pressed.put(key, true);
			KeyPressedEvent event = new KeyPressedEvent(source, key, controller);
			EventManager.callEvent(event);
			lastKey = key;
		}
		else if (value == PovDirection.north){
			Key key = Key.MOVE_UP;
			pressed.put(key, true);
			KeyPressedEvent event = new KeyPressedEvent(source, key, controller);
			EventManager.callEvent(event);
			lastKey = key;
		}
		else if (value == PovDirection.west){
			Key key = Key.MOVE_LEFT;
			pressed.put(key, true);
			KeyPressedEvent event = new KeyPressedEvent(source, key, controller);
			EventManager.callEvent(event);
			lastKey = key;
		}
		else if (value == PovDirection.east){
			Key key = Key.MOVE_RIGHT;
			pressed.put(key, true);
			KeyPressedEvent event = new KeyPressedEvent(source, key, controller);
			EventManager.callEvent(event);
			lastKey = key;
		}
		else{
			if (lastKey != Key.UNKNOWN){
				pressed.put(lastKey, false);			
				KeyReleaseEvent event = new KeyReleaseEvent(source, lastKey, controller);
				EventManager.callEvent(event);
			}
		}
		
		return true; // TODO
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		Gdx.app.log(TAG, "Controller: "+controller.getName()+" sliderX: "+sliderCode+" Value: "+value);
		return false; // TODO
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		Gdx.app.log(TAG, "Controller: "+controller.getName()+" sliderY: "+sliderCode+" Value: "+value);
		return false; // TODO
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false; // TODO
	}
	
}
