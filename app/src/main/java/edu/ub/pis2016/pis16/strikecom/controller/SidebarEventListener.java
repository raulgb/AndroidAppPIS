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

	public void onClickTurret1() {
	}

	public void onClickTurret2() {
	}

	public void onClickTurret3() {
	}

	public void onClickTurret4() {
	}

	public void onClickTurret5() {
	}

	public void onClickTurret6() {
	}

	public void onClickUpgrade1() {
	}

	public void onClickUpgrade2() {
	}

	public void onClickUpgrade3() {
	}

	public void onClickMinimap() {
	}

	public void onClickInventory() {
	}
}
