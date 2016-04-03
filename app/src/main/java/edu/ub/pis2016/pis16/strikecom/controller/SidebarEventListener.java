package edu.ub.pis2016.pis16.strikecom.controller;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;

/**
 * A listener adapter with empty methods, Override as many methods as necessary to add functionality.
 *
 * @author German Dempere
 */
public abstract class SidebarEventListener {

	protected StrikeComGLGame game;

	public SidebarEventListener(StrikeComGLGame game) {
		this.game = game;
	}

	public void onClickTurret(int index) {
	}

	public void onClickUpgrade(int index) {
	}

	public void onClickMinimap() {
	}

	public void onClickInventory() {
	}
}
