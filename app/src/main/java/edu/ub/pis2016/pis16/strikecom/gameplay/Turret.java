package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

/** A Generic turret for use with a {@link Vehicle}. */
public class Turret extends GameObject {

	private String id;

	String model;

	Vector2 tmp;

	TextureSprite sprite;
	int upgradeStatus = 0;

	Vector2 anchor;
	Vehicle owner;

	public Turret(String model, Vehicle owner, String anchor) {
		this.model = model;
		this.tmp = new Vector2();

		this.owner = owner;
		this.group = owner.group;
		this.anchor = owner.getAnchor(anchor);

		this.setParent(owner);
		this.setTag(owner.getTag() + "_turret");

		putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion(model, upgradeStatus)));
		putComponent(new PhysicsComponent());

		sprite = new TextureSprite(Assets.SPRITE_ATLAS.getRegion(model, upgradeStatus));
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public void update(float delta) {
		getComponent(PhysicsComponent.class).setPosition(anchor);
		super.update(delta);
	}

	public Vehicle getOwner() {
		return owner;
	}

	public int getUpgradeStatus() {
		return upgradeStatus;
	}

	public void setUpgradeStatus(int status) {
		this.upgradeStatus = status;
		getComponent(GraphicsComponent.class).setRegion(Assets.SPRITE_ATLAS.getRegion(model, upgradeStatus));
	}
}
