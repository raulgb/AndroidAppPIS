package edu.ub.pis2016.pis16.strikecom.controller;

import edu.ub.pis2016.pis16.strikecom.engine.framework.InputProcessor;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;

public class VehicleTouchController extends InputProcessor {

	private Vehicle vehicle;
	private GLGraphics glGraphics;
	private OrthoCamera camera;

	private GameObject moveIcon;

	private int pointer = -1;
	private Vector2 screenPos = new Vector2();
	private Vector2 targetPos = new Vector2();

	public VehicleTouchController(Screen screen, Vehicle vehicle) {
		this.glGraphics = screen.getGame().getGLGraphics();
		this.camera = screen.getGameObject("OrthoCamera", OrthoCamera.class);
		this.vehicle = vehicle;

		// Create move icon
		moveIcon = new GameObject();
		moveIcon.setLayer(Screen.LAYER_BUILDING_BOTTOM);
		moveIcon.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("cursor_move")));
		moveIcon.getComponent(GraphicsComponent.class).getSprite().setScale(0.4f);
		moveIcon.putComponent(new PhysicsComponent());
		moveIcon.setTag("cursor_move");
		screen.addGameObject("CursorMove", moveIcon);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		if (this.pointer == -1) {
			this.pointer = pointer;
			screenPos.set(x, y);
		}
		return false;
	}

	@Override
	public boolean touchDragged(float x, float y, int pointer) {
		if (this.pointer == pointer)
			screenPos.set(x, y);
		return false;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {
		if (this.pointer == pointer)
			this.pointer = -1;
		return false;
	}


	@Override
	public void update(float delta) {
		if (pointer != -1) {
			targetPos.x = screenPos.x;
			targetPos.y = screenPos.y;
			camera.unproject(targetPos);

			moveIcon.setPosition(targetPos);
			if (vehicle != null)
				vehicle.getComponent(VehicleFollowBehavior.class).setTarget(targetPos);
		}
	}
}
