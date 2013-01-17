package net.catacombsnatch.game.core.input;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class InputManager implements ControllerListener {
	public static final String TAG = "[Input]";
	public static boolean debug = true;

	private List<Controller> controllers;

	public InputManager() {
		controllers = new ArrayList<Controller>();
		Controllers.addListener( this );
	}

	public void shutdown() {
		Controllers.removeListener( this );
	}

	public boolean accelerometerMoved( Controller controller, int buttonIndex, Vector3 value ) {
		return false;
	}

	public boolean axisMoved( Controller controller, int buttonIndex, float value ) {
		return false;
	}

	public boolean buttonDown( Controller controller, int buttonIndex ) {
		return false;
	}

	public boolean buttonUp( Controller controller, int buttonIndex ) {
		return false;
	}

	public void connected( Controller controller ) {
		if ( controllers.add( controller ) ) {
			debug( "Connected: #" + controllers.indexOf( controller ) + " (" + controller.getName() + ")" );
		} else {
			debug( "Could not add controller: " + controller.getName() );
		}
	}

	public void disconnected( Controller controller ) {
		controllers.remove( controllers.indexOf( controller ) );
		debug( "Disconnected: " + controller.getName() );
	}

	public boolean povMoved( Controller controller, int buttonIndex, PovDirection value ) {
		return false;
	}

	public boolean xSliderMoved( Controller controller, int buttonIndex, boolean value ) {
		return false;
	}

	public boolean ySliderMoved( Controller controller, int buttonIndex, boolean value ) {
		return false;
	}

	public int indexOf( Controller controller ) {
		return Controllers.getControllers().indexOf( controller, true );
	}

	private void debug( String log ) {
		if ( debug ) Gdx.app.log( TAG, log );
	}
}
