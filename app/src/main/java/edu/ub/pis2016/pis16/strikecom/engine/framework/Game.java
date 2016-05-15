package edu.ub.pis2016.pis16.strikecom.engine.framework;

import android.app.Activity;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidAudio;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;

/** Methods to allow for a gameFrag life-cycle */
public interface Game {
	Input getInput();

	FileIO getFileIO();

	Graphics getGraphics();

	GLGraphics getGLGraphics();

	AndroidAudio getAudio();

	int getValueMusic();

	void setScreen(Screen screen);

	Screen getCurrentScreen();

	Screen getStartScreen();
}