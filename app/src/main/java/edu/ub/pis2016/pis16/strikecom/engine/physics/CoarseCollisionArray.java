package edu.ub.pis2016.pis16.strikecom.engine.physics;

import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

/**
 * Created by Alexander Bevzenko
 * as for now - axis-bound rectangles only
 * TODO: include other shapes after testing this
 */
public class CoarseCollisionArray {
	public Array<Body> dynamicBodies = new Array<>(false, 64);
	public Array<Body> staticBodies = new Array<>(false, 64);
	public Array<Body> allBodies = new Array<>(false, 128);
	private Array<Body> foundObjects = new Array<>(false, 16);

	public void insertStaticObject(Body body) {
		staticBodies.add(body);
		allBodies.add(body);
	}

	public void insertDynamicObject(Body body) {
		dynamicBodies.add(body);
		allBodies.add(body);
	}

	public void removeObject(Body body) {
		dynamicBodies.removeValue(body);
		staticBodies.removeValue(body);
		allBodies.removeValue(body);
	}

	public Array<Body> getPotentialColliders(Body body) {
		foundObjects.clear();

		float range = (body.bounds.getWidth() + body.bounds.getHeight());
		float minX = body.position.x - range;
		float maxX = body.position.x + range;
		float minY = body.position.y - range;
		float maxY = body.position.y + range;

		for (Body other : allBodies) {
			if (body == other || !Physics2D.Filter.test(body.filter, other.filter))
				continue;

			float oRange = (body.bounds.getWidth() + body.bounds.getHeight());
			minX -= oRange;
			maxX += oRange;
			minY -= oRange;
			maxY += oRange;

			float x = other.position.x, y = other.position.y;
			if (x >= minX && x <= maxX && y >= minY && y <= maxY)
				foundObjects.add(other);

			minX += oRange;
			maxX -= oRange;
			minY += oRange;
			maxY -= oRange;

		}
		return foundObjects;
	}

	@Deprecated
	public void clearDynamicCells() {
	}

	public Array<Body> getStaticBodies() {
		return staticBodies;
	}

	public Array<Body> getDynamicBodies() {
		return dynamicBodies;
	}
}
