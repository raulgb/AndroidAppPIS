package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.VehicleFollowBehavior;

/**
 * First test of a working enemy
 * <p/>
 * Created by German Dempere on 03/05/2016.
 */
public class EnemyTest extends Vehicle {

	PhysicsComponent physics;

	private float speed, maxSpeed = 10f;
	private float rotation = 0, rotationVel = 0;
	private Vector2 tmp = new Vector2();

	Turret turret;
	// Anchors
	private Vector2 turret_0 = new Vector2();

	public EnemyTest() {
		super();

		this.setLayer(Screen.LAYER_1);
		physics = new PhysicsComponent(new KinematicBody(new Rectangle(32, 32)));
		putComponent(physics);
		putComponent(new VehicleFollowBehavior());


		// Config turret
		putAnchor("turret_0", turret_0);
		turret = new Turret("enemy_turret", this, "turret_0");
		turret.setParent(this);
		turret.putComponent(new TurretBehavior());
		turret.getComponent(TurretBehavior.class).setTargetTag("player");
		turret.setLayer(Screen.LAYER_3);

		// Sprites
		putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("enemy")));
		getComponent(GraphicsComponent.class).getSprite().setScale(0.75f);
		turret.getComponent(GraphicsComponent.class).getSprite().setScale(0.75f);
	}

	public void update(float delta) {
		if (!screen.hasGameObject(turret))
			screen.addGameObject(turret);

		rotation += rotationVel * delta;

		Vector2 position = physics.getPosition();
		tmp.set(speed, 0).rotate(rotation);
		position.add(tmp.scl(delta));

		physics.setVelocity(tmp.set(speed, 0).rotate(rotation));
		physics.setPosition(position);
		physics.setRotation(rotation);

		turret_0.set(physics.getPosition());

		super.update(delta);
	}


	@Override
	public void turnLeft() {
		rotationVel += 0.25f;
		speed *= 0.99f;
	}

	@Override
	public void turnRight() {
		rotationVel -= 0.25f;
		speed *= 0.99f;
	}

	@Override
	public void accelerate() {
		speed = MathUtils.min(speed + 0.1f, maxSpeed);
		rotationVel *= 0.95f;
	}

	@Override
	public void brake() {
		speed = MathUtils.max(speed - 0.2f, 0);
	}
}
