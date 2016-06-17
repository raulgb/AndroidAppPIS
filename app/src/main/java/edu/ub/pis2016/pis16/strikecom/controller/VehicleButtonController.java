package edu.ub.pis2016.pis16.strikecom.controller;

import edu.ub.pis2016.pis16.strikecom.engine.framework.InputProcessor;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.ObjectMap;
import edu.ub.pis2016.pis16.strikecom.gameplay.Vehicle;

public class VehicleButtonController extends InputProcessor {

	private Vehicle vehicle;

	private float w, h;
	private Rectangle accelRect;
	private Rectangle brakeRect;
	private Rectangle leftRect;
	private Rectangle rightRect;

	private final int ACCEL = 0;
	private final int BRAKE = 1;
	private final int LEFT = 2;
	private final int RIGHT = 3;

	Vector2 tmp = new Vector2();

	ObjectMap<Integer, Integer> inputPointers = new ObjectMap<>();

	public VehicleButtonController(Screen screen, Vehicle vehicle) {
		GLGraphics glGraphics = screen.getGame().getGLGraphics();

		this.vehicle = vehicle;
		w = glGraphics.getWidth();
		h = glGraphics.getHeight();

		accelRect = new Rectangle(7 * w / 8f, 0, w / 8f, h / 2f);
		brakeRect = new Rectangle(6 * w / 8f, 0, w / 8f, h / 2f);
		leftRect = new Rectangle(0 * w / 8f, 0, 2 * w / 8f, h / 2f);
		rightRect = new Rectangle(2 * w / 8f, 0, 2 * w / 8f, h / 2f);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer) {
		tmp.set(x, h - y);
		if (inputPointers.containsKey(pointer))
			return false;
		if (accelRect.contains(tmp))
			inputPointers.put(pointer, ACCEL);

		if (brakeRect.contains(tmp))
			inputPointers.put(pointer, BRAKE);

		if (leftRect.contains(tmp))
			inputPointers.put(pointer, LEFT);

		if (rightRect.contains(tmp))
			inputPointers.put(pointer, RIGHT);

		return false;
	}

	@Override
	public boolean touchUp(float x, float y, int pointer) {
		inputPointers.remove(pointer);
		return false;
	}

	public void update(float delta) {
		if (!vehicle.isValid())
			return;
		for (Integer action : inputPointers.values())
			switch (action) {
				case ACCEL:
					vehicle.accelerate(0.6f);
					break;
				case BRAKE:
					vehicle.brake(1f);
					break;
				case LEFT:
					vehicle.turnLeft(0.8f);
					break;
				case RIGHT:
					vehicle.turnRight(0.8f);
					break;
			}
		if (inputPointers.size == 0) vehicle.brake(0.2f);
	}
}
