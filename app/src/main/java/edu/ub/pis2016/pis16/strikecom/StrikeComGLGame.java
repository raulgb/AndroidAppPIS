package edu.ub.pis2016.pis16.strikecom;

import edu.ub.pis2016.pis16.strikecom.controller.SidebarEventListener;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.screens.DummyGLScreen;
import edu.ub.pis2016.pis16.strikecom.screens.LoadingScreen;


/** Game class, encapsulates Fragment and Game behavior and keeps game states. */
public class StrikeComGLGame extends GLGameFragment {

	/* Global Game Variables Here */
	SidebarEventListener sidebarListener;

	public StrikeComGLGame(){
		super();
	}

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

	public void setSidebarListener(SidebarEventListener listener){
		this.sidebarListener = listener;
	}

	public SidebarEventListener getSidebarListener(){
		return sidebarListener;
	}

}
