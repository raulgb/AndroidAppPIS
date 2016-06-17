package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

/**
 * A simple health bar displaying over a GameObject.
 * Created by Herman on 2016-05-31.
 */
public class HealthBar extends GameObject {

	private GameObject owner;

	private static Sprite blackBar;
	private static Sprite[] healthBar;

	static {
		try {
			blackBar = new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar_bar"));
			healthBar = new Sprite[]{
					new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar", 0)),
					new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar", 1)),
					new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar", 2)),
					new Sprite(Assets.SPRITE_ATLAS.getRegion("healthbar", 3))
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HealthBar(GameObject owner) {
		this.owner = owner;

		setParent(owner);
		setLayer(Screen.LAYER_GUI);
		setTag(owner.getTag() + "_healthbar");

		//putComponent(new PhysicsComponent());
	}

	@Override
	public void update(float delta) {
		return;
	}

	@Override
	public void draw(SpriteBatch batch) {
		if (!owner.hasComponent(GraphicsComponent.class))
			return;

		float healthPerc = (float) owner.hitpoints / owner.maxHitpoints;
		int index = (int) (healthPerc * 3.99f);

		Sprite barSprite = healthBar[index];
		Sprite ownerSprite = owner.getSprite();

		// Update position and size of Sprites
		Vector2 spritePos = owner.getPosition().add(0, ownerSprite.getSize() * 0.8f);

		barSprite.setPosition(spritePos);
		barSprite.setSize(healthPerc * ownerSprite.getSize(), 0.20f * TILE_SIZE);
		blackBar.setPosition(spritePos);
		blackBar.setSize(ownerSprite.getSize(), 0.20f * TILE_SIZE);

		//getPhysics().setPosition(spritePos);

		healthBar[index].draw(batch);
		blackBar.draw(batch);

		//System.out.println("healthbar drawn at " + getPosition());
	}
}
