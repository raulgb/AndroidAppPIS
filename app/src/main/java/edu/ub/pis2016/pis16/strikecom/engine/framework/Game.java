package edu.ub.pis2016.pis16.strikecom.engine.framework;

import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;

/** Methods to allow for a game life-cycle */
public interface Game {
	public Input getInput();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public GLGraphics getGLGraphics();

	public Audio getAudio();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();
}