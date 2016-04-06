package edu.ub.pis2016.pis16.strikecom.engine.framework;

/**
 * Template for implementing input processors for a Game.
 */
public interface InputProcessor {
	public boolean touchUp(float x, float y, int pointer);

	public boolean touchDown(float x, float y, int pointer);

	public boolean touchDragged(float x, float y, int pointer);
}
