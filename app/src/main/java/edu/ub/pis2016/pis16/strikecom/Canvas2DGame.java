package edu.ub.pis2016.pis16.strikecom;


import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;

public class Canvas2DGame extends AndroidGame {
	@Override
	public Screen getStartScreen() {
		return new CanvasSpriteScreen(this);
	}
}
