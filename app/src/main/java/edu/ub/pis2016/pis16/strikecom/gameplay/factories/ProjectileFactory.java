package edu.ub.pis2016.pis16.strikecom.gameplay.factories;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Circle;
import edu.ub.pis2016.pis16.strikecom.engine.physics.DynamicBody;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.Explosion;
import edu.ub.pis2016.pis16.strikecom.gameplay.behaviors.ProjectileBehavior;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;

public class ProjectileFactory {

	public enum Type {
		PROJ_BULLET,             // Default machineguns
		PROJ_GATTLING_BULLET,    // Gattling machinegun
		PROJ_TANKSHELL,          // Enemy Tanks
		PROJ_HE_ROUND ,          // Howitzer
		PROJ_PLASMA				 // Plasma cannon
	}

	public static GameObject newProjectile(Type projType) {
		// Common
		final GameObject proj = new GameObject();
		proj.addOnDestroyAction(new Runnable() {
			@Override
			public void run() {
				// Sound
				Assets.sfx_hit.play(2);

				// Bullet Explosion on death
				Explosion explosion = new Explosion("explosion_bullet");
				explosion.setPosition(proj.getPosition());
				explosion.getSprite().setScale(0.25f);
				proj.getScreen().addGameObject(explosion);
			}
		});
		proj.putComponent(new ProjectileBehavior());
		Circle hitbox = new Circle(0.05f);
		proj.putComponent(new PhysicsComponent(new DynamicBody(hitbox)));
		proj.setLayer(Screen.LAYER_PROJECTILES);

		switch (projType) {
			case PROJ_BULLET:
				proj.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("proj_bullet")));
				break;
			case PROJ_GATTLING_BULLET:
				proj.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("proj_gatling_bullet")));
				proj.getSprite().setScale(1.5f);
				break;
			case PROJ_TANKSHELL:
				proj.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("proj_tank_shell")));
				break;
			case PROJ_HE_ROUND:
				proj.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("proj_he_round")));
				break;
			case PROJ_PLASMA:
				proj.putComponent(new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("proj_plasma")));
		}

		proj.getSprite().setScale(0.25f);

		return proj;
	}

}
