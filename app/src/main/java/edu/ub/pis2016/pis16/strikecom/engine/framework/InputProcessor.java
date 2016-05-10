package edu.ub.pis2016.pis16.strikecom.engine.framework;

/**
 * Adapter class for input processors. Touch only.
 * All points are top-left corner origin.
 */
public abstract class InputProcessor {
	public boolean touchUp(float x, float y, int pointer){return false;}

	public boolean touchDown(float x, float y, int pointer){return false;}

	public boolean touchDragged(float x, float y, int pointer){return false;}
}
