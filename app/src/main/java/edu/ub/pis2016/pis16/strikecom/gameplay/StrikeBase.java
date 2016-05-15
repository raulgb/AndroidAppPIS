package edu.ub.pis2016.pis16.strikecom.gameplay;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Animation;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

public class StrikeBase extends Vehicle {

	private static final int LEFT = 0, RIGHT = 1;

	private Sprite hull;
	private Sprite plateArmor;
	private Sprite compositeArmor;

	private Sprite leftThreads;
	private Sprite rightThreads;

	private TextureRegion[] sbmk1_hull;
	private TextureRegion[][] sbmk1_threads;

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
	private Vector2 turret_0 = new Vector2();
	private Vector2 turret_1 = new Vector2();
	private Vector2 turret_2 = new Vector2();
	private Vector2 turret_3 = new Vector2();

	private Vector2 pivot = new Vector2();
	private Vector2 leftThread = new Vector2();
	private Vector2 rightThread = new Vector2();

	private Animation[] threadAnim;

	/** Config instance to modify this StrikeBase's properties */
	private StrikeBaseConfig cfg;
	private boolean hasPlateArmor = false;
	private boolean hasCompositeArmor = false;

	private HashMap<Integer, TurretItem> equippedTurrets = new HashMap<>();
	private HashMap<Integer, UpgradeItem> equippedUpgrades = new HashMap<>();

	public StrikeBase(StrikeBaseConfig cfg) {
		super();

		// TODO Create FuelBehavior

		//  Create Physics component
		physics = new PhysicsComponent(new KinematicBody(new Rectangle(1.8f, 1.8f)));
		putComponent(physics);

		this.cfg = cfg;
		String model = cfg.modelName;

		sbmk1_hull = new TextureRegion[cfg.animHullFrames];
		for (int i = 0; i < cfg.animHullFrames; i++)
			sbmk1_hull[i] = Assets.SPRITE_ATLAS.getRegion(model + "_hull", i);

		sbmk1_threads = new TextureRegion[2][cfg.animThreadFrames];
		for (int i = 0; i < cfg.animThreadFrames; i++) {
			sbmk1_threads[LEFT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_left", i);
			sbmk1_threads[RIGHT][i] = Assets.SPRITE_ATLAS.getRegion(model + "_threads_right", i);
		}

		// Create sprites
		hull = new Sprite(sbmk1_hull[0]);
		hull.setSize(GameConfig.TILE_SIZE * 2);

		compositeArmor = new Sprite(Assets.SPRITE_ATLAS.getRegion("composite_" + model));
		compositeArmor.setSize(GameConfig.TILE_SIZE * 2);
		plateArmor = new Sprite(Assets.SPRITE_ATLAS.getRegion("plate_" + model));
		plateArmor.setSize(GameConfig.TILE_SIZE * 2);


		leftThreads = new Sprite(sbmk1_threads[LEFT][0]);
		leftThreads.setSize(GameConfig.TILE_SIZE * 2);
		rightThreads = new Sprite(sbmk1_threads[RIGHT][0]);
		rightThreads.setSize(GameConfig.TILE_SIZE * 2);

		// Animations
		threadAnim = new Animation[2];
		threadAnim[0] = new Animation(cfg.animThreadFrames);
		threadAnim[0].setFrameSpeed(0);
		threadAnim[1] = new Animation(cfg.animThreadFrames);
		threadAnim[1].setFrameSpeed(0);

		// Create and put anchors
		this.putAnchor("turret_0", turret_0);
		this.putAnchor("turret_1", turret_1);
		this.putAnchor("turret_2", turret_2);
		this.putAnchor("turret_3", turret_3);

		this.putAnchor("pivot", pivot);
		this.putAnchor("left_thread", leftThread);
		this.putAnchor("right_thread", rightThread);
	}

	public StrikeBaseConfig getCfg() {
		return this.cfg;
	}

	@Override
	public void update(float delta) {
		// Animations
		threadAnim[0].setFrameSpeed(leftThreadVel / 2f);
		threadAnim[1].setFrameSpeed(rightThreadVel / 2f);
		for (Animation a : threadAnim)
			a.update(delta);

		updatePhysics(delta);

		float rotation = physics.getRotation();
		Vector2 pos = physics.getPosition();

		// TODO Make this more universal, range 0-1 and depending on actual size (game units)
		switch (cfg.modelName) {
			case "sbmk1":
				turret_0.set(-8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_1.set(8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_2.set(-8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_3.set(8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
				break;
			case "sbmk2":
				turret_0.set(-8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_1.set(-8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_2.set(8, -8).scl(hull.getScale()).rotate(rotation).add(pos);
				turret_3.set(8, 8).scl(hull.getScale()).rotate(rotation).add(pos);
				break;
		}

		super.update(delta);
	}

	@Override
	protected void updatePhysics(float delta) {
		// Tank-like controls VERSION 2
		// Update speeds
		leftThreadVel = MathUtils.clamp(leftThreadVel + leftThreadAccel * delta, -cfg.max_speed, cfg.max_speed);
		rightThreadVel = MathUtils.clamp(rightThreadVel + rightThreadAccel * delta, -cfg.max_speed, cfg.max_speed);

		rightThreadVel *= 1 - rightThreadDampening * delta;
		leftThreadVel *= 1 - leftThreadDampening * delta;

		final float width = 1.8f * hull.getSize();
		float rotSpeed = (-leftThreadVel + rightThreadVel) / width;

		Vector2 pos = physics.getPosition();
		float rotation = physics.getRotation();

		leftThread.set(0, width / 2f).rotate(rotation).add(pos);
		rightThread.set(0, -width / 2f).rotate(rotation).add(pos);

		// Pivot around either threads or center
		if (rightThreadVel > leftThreadVel)
			pivot.set(leftThread);
		else if (leftThreadVel > rightThreadVel)
			pivot.set(rightThread);
		else
			pivot.set(pos);

		Vector2 threadToCenter = new Vector2(pos).sub(pivot);
		threadToCenter.rotate(rotSpeed * delta);
		pos.set(pivot).add(threadToCenter);

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

		leftThreads.setRegion(sbmk1_threads[LEFT][threadAnim[0].frame()]);
		rightThreads.setRegion(sbmk1_threads[RIGHT][threadAnim[1].frame()]);
		hull.setRegion(sbmk1_hull[0]);

		leftThreads.setRotation(rotation);
		rightThreads.setRotation(rotation);
		hull.setRotation(rotation);

		leftThreads.draw(batch, pos.x, pos.y);
		rightThreads.draw(batch, pos.x, pos.y);
		hull.draw(batch, pos.x, pos.y);

		if (hasPlateArmor) {
			plateArmor.setRotation(rotation);
			plateArmor.draw(batch, pos.x, pos.y);
		}

		if (hasCompositeArmor) {
			compositeArmor.setRotation(rotation);
			compositeArmor.draw(batch, pos.x, pos.y);
		}
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
		turret.putComponent(item.getBehavior());
		turret.getComponent(TurretBehavior.class).setTargetTag("enemy");

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

}
