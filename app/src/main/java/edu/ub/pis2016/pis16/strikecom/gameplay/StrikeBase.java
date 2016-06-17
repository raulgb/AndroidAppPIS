package edu.ub.pis2016.pis16.strikecom.gameplay;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.AnimatedSprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.physics.KinematicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.TurretBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;
import edu.ub.pis2016.pis16.strikecom.screens.GameScreen;

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
	private float rotSpeed;

	// Anchors
	private Vector2[] turret_anchors;

	private Vector2 pivot = new Vector2();
	private Vector2 leftThreadPos = new Vector2();
	private Vector2 rightThreadPos = new Vector2();

	/** Config instance to modify this StrikeBase's properties */
	private StrikeBaseConfig cfg;
	private boolean hasFuel = true;
	private boolean hasPlateArmor = false;
	private boolean hasCompositeArmor = false;
	private boolean hasReactiveArmor = false;
	private boolean hasAI = false;
	private float lerpModifier = 1f;
	private int dmgModifier = 0;

	private HashMap<Integer, TurretItem> equippedTurrets = new HashMap<>();
	private HashMap<Integer, UpgradeItem> equippedUpgrades = new HashMap<>();
	private HashMap<String, Turret> equippedGameObjects = new HashMap<>();

	public StrikeBase(StrikeBaseConfig cfg) {
		// TODO Create FuelBehavior

		this.cfg = cfg;
		String model = cfg.modelName;

		this.killable = true;
		this.hitpoints = cfg.hitpoints;
		this.maxHitpoints = cfg.hitpoints;

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
		threadsLeft.setFrameSpeed(leftThreadVel * 1.5f);
		threadsRight.setFrameSpeed(leftThreadVel * 1.5f);
		threadsLeft.update(delta);
		threadsRight.update(delta);

		if (hasFuel)
			computeFuel(delta);
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
		rotSpeed = (-leftThreadVel + rightThreadVel) / width * cfg.maneuverability;

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
	}

	@Override
	public void turnLeft(float power) {
		this.rightThreadAccel = cfg.accel * 0.75f * power;
		this.leftThreadAccel = -cfg.accel * 0.25f * power;

		if (rotSpeed < 0)
			this.leftThreadDampening = 0.99f;
		else
			this.leftThreadDampening = 0f;

		this.rightThreadDampening = 0f;
	}

	@Override
	public void turnRight(float power) {
		this.leftThreadAccel = cfg.accel * 0.75f * power;
		this.rightThreadAccel = -cfg.accel * 0.25f * power;

		if (rotSpeed > 0)
			this.rightThreadDampening = 0.99f;
		else
			this.rightThreadDampening = 0f;

		this.leftThreadDampening = 0f;
	}

	@Override
	public void accelerate(float power) {
		this.leftThreadAccel = cfg.accel * power;
		this.rightThreadAccel = cfg.accel * power;

		this.leftThreadDampening = 0f;
		this.rightThreadDampening = 0f;
	}

	@Override
	public void brake(float power) {
		power = MathUtils.clamp(power, 0, 1);
		this.leftThreadAccel = 0;
		this.rightThreadAccel = 0;

		this.leftThreadDampening = 0.90f * (1 - power);
		this.rightThreadDampening = 0.90f * (1 - power);
	}

	public void reverse(float power) {
		accelerate(-power * 0.75f);
	}

	/** Add a TurretItem to this strikebase and to the Screen */
	public void addTurret(TurretItem item, int slot) {
		String tName = "turret_" + Integer.toString(slot);
		Turret turret = new Turret(item.getModel(), this, tName);

		turret.faction = this.faction;
		turret.setParent(this);
		turret.setLayer(Screen.LAYER_STRIKEBASE_TURRETS);

		turret.setTag(getTurretTag(slot));

		PhysicsComponent turretPhysics = new PhysicsComponent();
		TurretBehavior turretBehavior = new TurretBehavior();

		turret.putComponent(turretBehavior);
		turret.putComponent(turretPhysics);
		turret.cfg = item.getConfig();
		if (hasAI)
			turret.cfg.lerp_speed *= lerpModifier;

		// Player turrets are faster to track targets
		turret.cfg.idle_seconds = 0.25f;

		// Store in local arrays
		equippedTurrets.put(slot, item);
		equippedGameObjects.put(turret.getTag(), turret);

		screen.addGameObject(turret.getTag(), turret);
	}

	/** Remove a Turret slot from this StrikeBase and the Screen */
	public void removeTurret(int slot) {
		screen.removeGameObject(getTurretTag(slot));

		equippedTurrets.remove(slot);
		equippedGameObjects.remove(getTurretTag(slot));
	}

	/** Generate a Turret tag for a given slot */
	private String getTurretTag(int slot) {
		return getTag() + "_turret_" + slot;
	}

	public void addUpgrade(UpgradeItem item, int slot) {

		switch (item.cfg.function) {
			case REPAIR:
				// restores some hp
				this.hitpoints = MathUtils.min(maxHitpoints, hitpoints + MathUtils.round(item.cfg.value));
				break;

			case ARMOUR_PLATE:
				// Increases endurance
				if (!hasPlateArmor) {
					this.hitpoints += MathUtils.round(item.cfg.value);
					this.maxHitpoints += MathUtils.round(item.cfg.value);
					hasPlateArmor = true;
				}
				break;

			case ARMOUR_COMPOSITE:
				// Increases endurance
				if (!hasCompositeArmor) {
					this.hitpoints += MathUtils.round(item.cfg.value);
					this.maxHitpoints += MathUtils.round(item.cfg.value);
					hasCompositeArmor = true;
				}
				break;

			case ARMOUR_REACTIVE:
				// Reduces incoming damage
				hasReactiveArmor = true;
				dmgModifier = MathUtils.round(item.cfg.value);
				break;

			case AI:
				// Smart turrets
				if (!hasAI) {
					lerpModifier = item.cfg.value;
					hasAI = true;
					setTurretAI();
				}
				break;

			case ENGINE_EFFICIENCY:
				// Reduces fuel consumption
				cfg.fuel_usage_mult = item.cfg.value;
				break;

			case SCAVENGER:
				// Increases scrap income
				FragmentedGameActivity.playerState.setScrapMultiplier(item.cfg.value);
				break;
		}

		equippedUpgrades.put(slot, item);
	}

	public void removeUpgrade(int slot) {

		UpgradeItem item = equippedUpgrades.get(slot);
		switch (item.cfg.function) {
			case FUEL:
				break;

			case AI:
				lerpModifier = 1f;
				hasAI = false;
				setTurretAI();
				// Smart turrets
				break;

			case ARMOUR_COMPOSITE:
				this.maxHitpoints -= MathUtils.round(item.cfg.value);
				this.hitpoints = MathUtils.min(this.hitpoints, this.maxHitpoints);
				hasPlateArmor = false;
				// Increases endurance
				break;

			case ARMOUR_PLATE:
				this.maxHitpoints -= MathUtils.round(item.cfg.value);
				this.hitpoints = MathUtils.min(this.hitpoints, this.maxHitpoints);
				hasCompositeArmor = false;
				// Slightly increases endurance
				break;

			case ARMOUR_REACTIVE:
				dmgModifier = 5;
				hasReactiveArmor = false;
				break;

			case ENGINE_EFFICIENCY:
				// Reduces fuel consumption
				cfg.fuel_usage_mult = 1;
				break;

			case SCAVENGER:
				FragmentedGameActivity.playerState.setScrapMultiplier(1f);
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

	public HashMap getEquippedTurrets() {
		return this.equippedTurrets;
	}

	public HashMap getEquippedUpgrades() {
		return this.equippedUpgrades;
	}

	private void setTurretAI() {
		if (hasAI) {
			for (String k : equippedGameObjects.keySet()) {
				equippedGameObjects.get(k).cfg.lerp_speed *= lerpModifier;
			}
		} else {
			for (String k : equippedGameObjects.keySet()) {
				equippedGameObjects.get(k).cfg.lerp_speed /= lerpModifier;
			}
		}
	}

	private void computeFuel(float delta) {
		FragmentedGameActivity.playerState.addFuel(-delta * cfg.fuel_usage * cfg.fuel_usage_mult);
		if (FragmentedGameActivity.playerState.isOutOfFuel()) {
			hasFuel = false;
			cfg.accel = 0;
			cfg.max_speed = 0;
			cfg.max_reverse_speed = 0;

			leftThreadDampening = 0.90f;
			rightThreadDampening = 0.90f;

			((GameScreen) screen).outOfFuel();
		}
	}

	@Override
	public void takeHit(float dmg) {
		if (hasReactiveArmor) {
			dmg = MathUtils.max(1, dmg - dmgModifier);
		}
		super.takeHit(dmg);
	}
}
