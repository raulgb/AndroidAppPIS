package edu.ub.pis2016.pis16.strikecom.engine.framework;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidAudio;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;

/** Methods to allow for a gameFrag life-cycle */
public interface Game {
	public Input getInput();

	public FileIO getFileIO();

	public Graphics getGraphics();

	public GLGraphics getGLGraphics();

	public AndroidAudio getAudio();

	public int getValueMusic();

	public void setScreen(Screen screen);

	public Screen getCurrentScreen();

	public Screen getStartScreen();
}