package edu.ub.pis2016.pis16.strikecom.gameplay.factories;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
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
		PROJ_HE_ROUND            // Howitzer
	}

	public static GameObject newProjectile(Type projType) {
		// Common
		GameObject proj = new GameObject(){
			@Override
			public void destroy() {
				super.destroy();
				// Bullet Explosion on death
				Explosion explosion = new Explosion("explosion_bullet");
				explosion.setPosition(getPosition());
				explosion.getSprite().setScale(0.25f);
				getScreen().addGameObject(explosion);
			}
		};
		proj.putComponent(new ProjectileBehavior());
		Rectangle rect = new Rectangle(.05f * GameConfig.TILE_SIZE, .05f * GameConfig.TILE_SIZE);
		proj.putComponent(new PhysicsComponent(new DynamicBody(rect)));
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
		}

		proj.getSprite().setScale(0.25f);

		return proj;
	}

}
