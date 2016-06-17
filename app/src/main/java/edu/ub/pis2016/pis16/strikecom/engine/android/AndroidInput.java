package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.content.Context;
import android.view.View;

import java.util.List;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.TouchHandler;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

public class AndroidInput implements Input {
	AccelerometerHandler accelHandler;
	//KeyboardHandler keyHandler;
	TouchHandler touchHandler;

	public AndroidInput(Context context, View view, float scaleX, float scaleY) {
		accelHandler = new AccelerometerHandler(context);
		//keyHandler = new KeyboardHandler(view);
		touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
	}

	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	public float getAccelX() {
		return accelHandler.getAccelX();
	}

	public float getAccelY() {
		return accelHandler.getAccelY();
	}

	public float getAccelZ() {
		return accelHandler.getAccelZ();
	}

	public Array<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

	/** KEYBOARD METHODS NOT IMPLEMENTED */
	public boolean isKeyPressed(int keyCode) {
		//return keyHandler.isKeyPressed(keyCode);
		return false;
	}

	/** KEYBOARD METHODS NOT IMPLEMENTER */
	public List<KeyEvent> getKeyEvents() {
		//return keyHandler.getKeyEvents();
		return null;
	}
}