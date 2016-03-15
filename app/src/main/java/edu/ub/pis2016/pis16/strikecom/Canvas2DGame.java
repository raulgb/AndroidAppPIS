package edu.ub.pis2016.pis16.strikecom;


import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidGame;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.screens.AlexanderScreen;
import edu.ub.pis2016.pis16.strikecom.screens.Test2DScreen;

@Deprecated
public class Canvas2DGame extends AndroidGame {
	@Override
	public Screen getStartScreen() {
		return new AlexanderScreen(this);
	}
}
