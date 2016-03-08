package edu.ub.pis2016.pis16.strikecom;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGame;


public class OpenGLGame extends GLGame {

	@Override
	public Screen getStartScreen() {
		return new OpenGLTestScreen(this);
	}
}
