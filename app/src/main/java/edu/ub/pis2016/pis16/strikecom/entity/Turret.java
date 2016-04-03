package edu.ub.pis2016.pis16.strikecom.entity;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

/** A Generic turret for use with a {@link Vehicle}. */
public class Turret extends GameObject {

	String model;

	Vector2 pos;
	float rotation;


	Vector2 target;
	Vector2 tmp;

	TextureSprite sprite;
	int upgradeStatus = 0;

	Vector2 anchor;
	Vehicle owner;

	public Turret(String model, Vehicle owner, String anchor) {
		this.model = model;
		this.pos = new Vector2();
		this.tmp = new Vector2();

		this.owner = owner;
		this.anchor = owner.getAnchor(anchor);

		sprite = new TextureSprite(Assets.SPRITE_ATLAS.getRegion(model, upgradeStatus));
	}

	public void update(float delta) {
		this.pos.set(anchor);
	}

	public void draw(SpriteBatch batch) {
		sprite.setRotation(rotation);
		sprite.draw(batch, pos.x, pos.y);
	}

	public int getUpgradeStatus() {
		return upgradeStatus;
	}

	public void setUpgradeStatus(int status) {
		this.upgradeStatus = status;
		sprite = new TextureSprite(Assets.SPRITE_ATLAS.getRegion(model, status));
	}

	public void aimAt(Vector2 target) {
		float angleToTarget = tmp.set(target).sub(pos).angle();
		float angleDelta = Angle.angleDelta(rotation, angleToTarget);

		this.rotation += angleDelta * 0.05f;
	}

}
