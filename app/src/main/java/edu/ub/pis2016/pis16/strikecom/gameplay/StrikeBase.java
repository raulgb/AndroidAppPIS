package edu.ub.pis2016.pis16.strikecom.gameplay;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Angle;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.AnimatedSprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.CameraBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.*;

public class StrikeBase extends Vehicle {

	private static final int LEFT = 0, RIGHT = 1;

	private Sprite hull;
	//private Sprite plateArmor;
	//private Sprite compositeArmor;

	private AnimatedSprite threadsLeft;
	private AnimatedSprite threadsRight;

	private TextureRegion[] sbmk1_hull;

	private PhysicsComponent physics;
	private Vector2 tmp = new Vector2();

	// Current accel
	private float leftThreadAccel = 0;
	private float rightThreadAccel = 0;

	// Current Dampening
	private float leftThreadDampening = 0;
	private float rightThreadDampening = 0;

	// Current Speed
	private float leftThreadVel;
	private float rightThreadVel;

	// Anchors
	private Vector2[] turret_anchors;

	private Vector2 pivot = new Vector2();
	private Vector2 leftThreadPos = new Vector2();
	private Vector2 rightThreadPos = new Vector2();

	/** Config instance to modify this StrikeBase's properties */
	private StrikeBaseConfig cfg;
	private boolean hasPlateArmor = false;
	private boolean hasCompositeArmor = false;

	private HashMap<Integer, TurretItem> equippedTurrets = new HashMap<>();
	private HashMap<Integer, UpgradeItem> equippedUpgrades = new HashMap<>();

	public StrikeBase(StrikeBaseConfig cfg) {
		// TODO Create FuelBehavior

		this.cfg = cfg;
		String model = cfg.modelName;

		//  Create Physics component
		// Width actually signifies the horizontal X axis, as the sprite looks to the right.
		// Height is from thread to thread.
		float hitWidth = cfg.size_tiles * cfg.hitbox_factor[0];
		float hitHeight = cfg.size_tiles * cfg.hitbox_factor[1];
		physics = new PhysicsComponent(new KinematicBody(new Rectangle(hitWidth, hitHeight)));
		putComponent(physics);

		// Create sprites
		sbmk1_hull = new TextureRegion[cfg.anim_hull_frames];
		for (int i = 0; i < cfg.anim_hull_frames; i++)
			sbmk1_hull[i] = Assets.SPRITE_ATLAS.getRegion(model + "_hull", i);

		hull = new Sprite(sbmk1_hull[0]);
		hull.setSize(cfg.size_tiles * TILE_SIZE);

		/*
		compositeArmor = new Sprite(Assets.SPRITE_ATLAS.getRegion("composite_" + model));
		compositeArmor.setSize(cfg.size_tiles * TILE_SIZE);
		plateArmor = new Sprite(Assets.SPRITE_ATLAS.getRegion("plate_" + model));
		plateArmor.setSize(cfg.size_tiles * TILE_SIZE);
		*/

		// Thread setup
		threadsLeft = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions(model + "_threads"), 0);
		threadsLeft.setFrameSpeed(0f);
		threadsLeft.setScale(hull.getScale());

		threadsRight = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions(model + "_threads"), 0);
		threadsRight.setFrameSpeed(0f);
		threadsRight.setScale(hull.getScale());

		// Create and put anchors
		turret_anchors = new Vector2[cfg.turret_num];
		for (int i = 0; i < cfg.turret_num; i++) {
			turret_anchors[i] = new Vector2();
			this.putAnchor("turret_" + i, turret_anchors[i]);
		}

		this.putAnchor("pivot", pivot);
		this.putAnchor("left_thread", leftThreadPos);
		this.putAnchor("right_thread", rightThreadPos);

		addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				Assets.sfx_expl_heavy.play(5f);
			}
		});
	}

	@Override
	public void update(float delta) {
		// Update vehicle physics
		updatePhysics(delta);

		// Update turret anchors
		float rotation = physics.getRotation();
		Vector2 pos = physics.getPosition();

		int i = 0;
		for (float[] offset : cfg.turret_offset) {
			float distance = hull.getSize() / 2f;
			float dx = offset[0] * distance;
			float dy = offset[1] * distance;
			turret_anchors[i].set(dx, dy).rotate(rotation).add(pos);
			i++;
		}

		super.update(delta);

		// Thread Animation
		threadsLeft.setFrameSpeed(leftThreadVel / 1.5f);
		threadsRight.setFrameSpeed(leftThreadVel / 1.5f);
		threadsLeft.update(delta);
		threadsRight.update(delta);
	}

	@Override
	protected void updatePhysics(float delta) {
		// Tank-like controls VERSION 2
		// Update speeds
		leftThreadVel = MathUtils.clamp(leftThreadVel + leftThreadAccel * delta, -cfg.max_speed, cfg.max_speed);
		rightThreadVel = MathUtils.clamp(rightThreadVel + rightThreadAccel * delta, -cfg.max_speed, cfg.max_speed);

		rightThreadVel *= 1 - rightThreadDampening * delta;
		leftThreadVel *= 1 - leftThreadDampening * delta;

		// Thread offset is interpreted as the vehicles max pivoting point along its Y axis,
		// So in some way it's the "width" of the vehicle
		final float width = cfg.thread_offsetY * hull.getSize();
		float rotSpeed = (-leftThreadVel + rightThreadVel) / width * cfg.maneuverability;

		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		leftThreadPos.set(cfg.thread_offsetX * width, width / 2f).rotate(rotation).add(pos);
		rightThreadPos.set(cfg.thread_offsetX * width, -width / 2f).rotate(rotation).add(pos);

		// Pivot around either threads or center
		if (rightThreadVel > leftThreadVel)
			pivot.set(leftThreadPos);
		else if (leftThreadVel > rightThreadVel)
			pivot.set(rightThreadPos);
		else
			pivot.set(pos);

//		Vector2 threadToCenter = new Vector2(pos).sub(pivot);
//		threadToCenter.rotate(rotSpeed * delta);
//		pos.set(pivot).add(threadToCenter);

		tmp.set(pos).sub(pivot);
		tmp.rotate(rotSpeed * delta);
		pos.set(pivot).add(tmp);

		// Average thread velocity and rotate to get a velocity vector
		tmp.set(leftThreadVel + rightThreadVel, 0).scl(0.5f).rotate(rotation);
		pos.add(tmp.scl(delta));

		rotation = (rotation + rotSpeed) % 360;
		if (rotation < 0)
			rotation = 360 + rotation;

		// Commit Physics changes
		physics.setVelocity(tmp.set((leftThreadVel + rightThreadVel) / 2f, 0).rotate(rotation));
		physics.setRotation(rotation);
		physics.setPosition(pos);
	}


	@Override
	public void draw(SpriteBatch batch) {
		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		int dmgFrame = (int) MathUtils.lerp(cfg.anim_hull_frames - 0.01f, 0, (float) hitpoints / maxHitpoints);
		hull.setRegion(sbmk1_hull[dmgFrame]);

		threadsLeft.setRotation(rotation);
		threadsLeft.setPosition(leftThreadPos);
		threadsRight.setRotation(rotation);
		threadsRight.setPosition(rightThreadPos);
		hull.setRotation(rotation);

		threadsLeft.draw(batch);
		threadsRight.draw(batch);
		hull.draw(batch, pos.x, pos.y);

		/*
		if (hasPlateArmor) {
			plateArmor.setRotation(rotation);
			plateArmor.draw(batch, pos.x, pos.y);
		}

		if (hasCompositeArmor) {
			compositeArmor.setRotation(rotation);
			compositeArmor.draw(batch, pos.x, pos.y);
		}
		*/
	}

	@Override
	public void turnLeft() {
		this.rightThreadAccel = cfg.accel * 0.75f;
		this.leftThreadAccel = -cfg.accel * 0.25f;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void turnRight() {
		this.leftThreadAccel = cfg.accel * 0.75f;
		this.rightThreadAccel = -cfg.accel * 0.25f;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void accelerate() {
		this.leftThreadAccel = cfg.accel;
		this.rightThreadAccel = cfg.accel;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void brake() {
		this.leftThreadAccel = 0;
		this.rightThreadAccel = 0;

		this.leftThreadDampening = 0.95f;
		this.rightThreadDampening = 0.95f;
	}

	public void reverse() {

	}

	public void addTurret(TurretItem item, int slot) {
		String tName = "turret_" + Integer.toString(slot);
		Turret turret = new Turret(item.getModel(), this, tName);
		turret.setParent(this);
		turret.setLayer(Screen.LAYER_STRIKEBASE_TURRETS);

		turret.setTag(getTag() + "_" + tName);

		// Create a modified physics component that takes parent rotation into account
		PhysicsComponent turretPhysics = new PhysicsComponent();

		TurretBehavior turretBehavior = new TurretBehavior();
		turretBehavior.setTargetTag("enemy");
		turretBehavior.setAngleLimit(cfg.turret_angle_lim[slot]);

		turret.putComponent(turretBehavior);
		turret.putComponent(turretPhysics);
		turret.cfg = item.getConfig();
		//getComponent(GraphicsComponent.class).getSprite().setSize(GameConfig.TILE_SIZE *2);

		screen.addGameObject(tName, turret);

		equippedTurrets.put(slot, item);
	}

	public void removeTurret(int slot) {
		String tName = "turret_" + Integer.toString(slot);
		screen.removeGameObject(screen.getGameObject(tName));

		equippedTurrets.remove(slot);
	}

	public void addUpgrade(UpgradeItem item, int slot) {
		// TODO implement

		switch (UpgradeItem.AVAILABLE_FUNCTIONS.valueOf(item.getFunction())) {
			case AI:
				// Smart turrets
				break;

			case ARMOUR_COMPOSITE:
				// Increases endurance
				this.hitpoints += 100;
				this.maxHitpoints += 100;
				hasCompositeArmor = true;
				break;

			case ARMOUR_PLATE:
				// Slightly increases endurance
				this.hitpoints += 25;
				this.maxHitpoints += 25;
				hasPlateArmor = true;
				break;

			case ENGINE_EFFICIENCY:
				// Reduces fuel consumption
				cfg.fuel_usage_mult = 0.75f;
				break;

			case FUEL:
				break;
		}
		equippedUpgrades.put(slot, item);
	}

	public void removeUpgrade(int slot) {
		// TODO implement

		switch (UpgradeItem.AVAILABLE_FUNCTIONS.valueOf(equippedUpgrades.get(slot).getFunction())) {
			case AI:
				// Smart turrets
				break;
			case ARMOUR_COMPOSITE:
				this.maxHitpoints -= 100;
				this.hitpoints = MathUtils.min(this.hitpoints, this.maxHitpoints);
				// Increases endurance
				break;
			case ARMOUR_PLATE:
				this.maxHitpoints -= 25;
				this.hitpoints = MathUtils.min(this.hitpoints, this.maxHitpoints);
				// Slightly increases endurance
				break;
			case ENGINE_EFFICIENCY:
				// Reduces fuel consumption
				cfg.fuel_usage_mult = 1;
				break;
			case FUEL:
				break;
		}
		equippedUpgrades.remove(slot);
	}

	public TurretItem getTurret(int slot) {
		return equippedTurrets.get(slot);
	}

	public UpgradeItem getUpgrade(int slot) {
		return equippedUpgrades.get(slot);
	}

	public HashMap getEquippedTurrets() { return this.equippedTurrets; }

	public HashMap getEquippedUpgrades() { return this.equippedUpgrades; }

}
