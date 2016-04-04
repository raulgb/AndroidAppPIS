package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidSprite;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

public class StrikeBase {

	public Vector2 pos, vel, accel;
	private float angle;

	private Sprite sprite;
	private Sprite[] turrets;

	private float numTurrets = 4;

	/** Angles for turret centers */
	private float[] tAngle = new float[]{42.5f, 180f - 42.5f, 180 + 42.5f, 270 + 42.5f};
	/** Offsets for turrets */
	private float[] tDistance = new float[]{11.5f, 11.5f, 11.5f, 11.5f};
	/** Angle for turrets */
	private float[] tRotation = new float[]{0, 0, 0, 0};
	/** Rotational vel for turrets */
	private float[] tRotVel = new float[]{0, 0, 0, 0};

	public StrikeBase(Game g) {
		pos = new Vector2();
		vel = new Vector2();
		accel = new Vector2();

		Pixmap SBPixmap = g.getGraphics().newPixmap("strike_base.png");
		SBPixmap.mirrorY();
		Pixmap turretPixmap = g.getGraphics().newPixmap("turret.png");
		turretPixmap.mirrorY();

		sprite = new AndroidSprite(SBPixmap);
		sprite.setOrigin(0.5f, 0.5f);

		turrets = new Sprite[4];
		for (int i = 0; i < numTurrets; i++) {
			turrets[i] = new AndroidSprite(turretPixmap);
			turrets[i].scaleTo(0.8f, 0.8f);
			turrets[i].setOrigin(0.5f, 0.5f);
		}

	}


	Vector2 tmp = new Vector2();
	Vector2 offset = new Vector2();

	public void update(float delta) {

		vel.add(accel.x * delta, accel.y * delta);
		pos.add(vel.x * delta, vel.y * delta);

		angle = vel.angle();

		sprite.translateTo(pos);
		sprite.rotateTo(angle);

		for (int i = 0; i < numTurrets; i++) {
			tmp.set(pos);
			offset.set(tDistance[i], 0).rotate(tAngle[i] + angle);
			turrets[i].translateTo(tmp.add(offset));

			tRotVel[i] += MathUtils.random(-0.1f, 0.1f);
			tRotation[i] += tRotVel[i];

			turrets[i].rotateTo(tRotation[i] + angle);
		}


	}

	public void draw(Graphics g) {
		sprite.draw(g);
		for (int i = 0; i < numTurrets; i++) {
			turrets[i].draw(g);
		}
	}

	public float getAngle() {
		return angle;
	}
}
