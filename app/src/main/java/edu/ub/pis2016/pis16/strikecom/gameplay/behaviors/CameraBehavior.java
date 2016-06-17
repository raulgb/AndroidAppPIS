package edu.ub.pis2016.pis16.strikecom.gameplay.behaviors;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.BehaviorComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;

/**
 * Tracking camera behavior, set a desired target and let the camera track to it.
 * Also contais some camera efects.
 *
 * Created by German Dempere on 22/05/2016.
 */
public class CameraBehavior extends BehaviorComponent {

	GameObject tracking = null;
	Vector2 tmp = new Vector2();

	@Override
	public void update(float delta) {
		if (tracking == null) return;

		if (tracking.isValid()) {
			OrthoCamera camera = (OrthoCamera) gameObject;
			camera.position.lerp(tracking.getPosition(), 0.10f);

			// Round off cam pos
			tmp.set(camera.position);
			tmp.x = (int) (tmp.x * 10) / 10f;
			tmp.y = (int) (tmp.y * 10) / 10f;
			camera.position.set(tmp);
		}

		((OrthoCamera) gameObject).updateOrtho();
	}

	public void setTracking(GameObject go) {
		this.tracking = go;
	}

	/** Shake the camera in a random direction some ammount */
	public void cameraShake(float mag) {
		OrthoCamera camera = (OrthoCamera) gameObject;
		camera.position.x += MathUtils.random(-mag, mag);
		camera.position.y += MathUtils.random(-mag, mag);
	}

	/** Shake the camera along a direction vector. */
	public void cameraShake(Vector2 direction) {
		OrthoCamera camera = (OrthoCamera) gameObject;

		direction.scl(MathUtils.random(1f));
		camera.position.add(direction);
	}
}
