package edu.ub.pis2016.pis16.strikecom;

import java.util.Stack;

import edu.ub.pis2016.pis16.strikecom.controller.SidebarEventListener;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.screens.LoadingScreen;


/** Game class, encapsulates Fragment and Game behavior and keeps game states. */
public class StrikeComGLGame extends GLGameFragment {

	private Stack<Screen> screenStack = new Stack<>();

	/* Global Game Variables Here */
	SidebarEventListener sidebarListener;

	public StrikeComGLGame() {
		super();
	}

	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}

	/** Set the game screen. This will delete the entire screen stack. */
	@Override
	public void setScreen(Screen screen) {
		screenStack.clear();
		screenStack.push(screen);
		super.setScreen(screen);
	}

	/** Push a screen on top of another, pausing the initial screen */
	public void pushScreen(Screen screen) {
		if (!screenStack.empty())
			screenStack.peek().pause();

		screenStack.push(screen);
		screen.resume();
		screen.resize(SCREEN_W, SCREEN_H);

		this.screen = screenStack.peek();
	}

	/** Resume the previous screen, and dispose of the current one .*/
	public void popScreen() {
		if (screenStack.size() < 2)
			throw new IllegalStateException("Can't pop the Screen stack with less than 2 screens.");

		Screen oldScreen = screenStack.pop();
		oldScreen.pause();
		oldScreen.dispose();

		Screen newScreen = screenStack.peek();
		newScreen.resume();
		screen.resize(SCREEN_W, SCREEN_H);

		this.screen = newScreen;
	}

	public void setSidebarListener(SidebarEventListener listener) {
		this.sidebarListener = listener;
	}

	public SidebarEventListener getSidebarListener() {
		return sidebarListener;
	}

	public Object getInventoryListener() {
		return null;
	}

	public Object getShopListener() {
		return null;
	}

}
