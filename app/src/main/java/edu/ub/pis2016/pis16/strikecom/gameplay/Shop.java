package edu.ub.pis2016.pis16.strikecom.gameplay;

import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Rectangle;
import edu.ub.pis2016.pis16.strikecom.engine.physics.StaticBody;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;

import static edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig.TILE_SIZE;

/**
 * Created by sdp on 30/05/16.
 */
public class Shop extends GameObject {
	public Inventory inventory;
	private boolean isVault;

	public Shop() {
		isVault = false;
		inventory = new Inventory();
	}

	public Shop(boolean isVault) {
		this.isVault = isVault;
		inventory = new Inventory();

		putComponent(new PhysicsComponent(new StaticBody(new Rectangle(1.5f, 1.5f))));
		getPhysics().body.filter = Physics2D.Filter.SHOP;

		GraphicsComponent ghc;
		if (isVault) {
			ghc = new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("shop_golden"));
			//ghc.getSprite().setSize(TILE_SIZE);
		} else {
			ghc = new GraphicsComponent(Assets.SPRITE_ATLAS.getRegion("shop"));
			//ghc.getSprite().setSize(2f * TILE_SIZE);
		}
		putComponent(ghc);
		faction = GameObject.Faction.SHOP;
	}

	public boolean thisIsVault(){
		return isVault;
	}

}
