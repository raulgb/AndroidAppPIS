package edu.ub.pis2016.pis16.strikecom.controller;

import android.app.Activity;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.physics.ContactListener;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.CameraBehavior;

public class GameContactListener extends ContactListener {

	private Screen screen;
	private OrthoCamera camera;
	private FragmentedGameActivity activity;

	public GameContactListener(Screen screen) {
		this.screen = screen;
		this.camera = screen.getGameObject("OrthoCamera", OrthoCamera.class);
		this.activity = (FragmentedGameActivity) ((GLGameFragment) screen.getGame()).getActivity();
	}

	@Override
	public void beginContact(Contact contact) {
		GameObject goA = (GameObject) contact.a.userData;
		GameObject goB = (GameObject) contact.b.userData;
		Physics2D.Filter filterA = goA.getPhysics().body.filter;
		Physics2D.Filter filterB = goB.getPhysics().body.filter;

		if (Physics2D.Filter.isProjectile(filterA))
			handleProjectileContact(goA, goB);
		else if (Physics2D.Filter.isProjectile(filterB))
			handleProjectileContact(goB, goA);

		else if (filterA == Physics2D.Filter.PLAYER && filterB == Physics2D.Filter.ENEMY)
			handlePlayerContact(goA, goB);
		else if (filterA == Physics2D.Filter.ENEMY && filterB == Physics2D.Filter.PLAYER)
			handlePlayerContact(goB, goA);

		else if (filterA == Physics2D.Filter.SHOP)
			handleShopContact(goA, goB);
		else if (filterB == Physics2D.Filter.SHOP)
			handleShopContact(goB, goA);
	}

	@Override
	public void endContact(Contact contact) {
	}

	private void handlePlayerContact(GameObject player, GameObject other) {
		if (other.killable) {
			player.takeHit( other.hitpoints / 2f );
			camera.getComponent(CameraBehavior.class).cameraShake(2);
			other.destroy();
		}
	}

	private void handleShopContact(GameObject shop, GameObject other) {
		if (!(other.getPhysics().body.filter == Physics2D.Filter.PLAYER))
			return;
		activity.showShopDialog(shop.getTag());
	}

	private void handleProjectileContact(GameObject projectile, GameObject other) {
		if (!other.killable)
			return;

		other.takeHit(projectile.hitpoints);

		// TODO Disabled
//		if (other == strikeBase)
//			camera.getComponent(CameraBehavior.class).cameraShake(1.5f);
		projectile.destroy();
	}
}
