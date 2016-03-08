package edu.ub.pis2016.pis16.strikecom.engine.framework;

import android.view.View;

import java.util.List;

public interface TouchHandler extends View.OnTouchListener {
	public boolean isTouchDown(int pointer);

	public int getTouchX(int pointer);

	public int getTouchY(int pointer);

	public List<Input.TouchEvent> getTouchEvents();
}