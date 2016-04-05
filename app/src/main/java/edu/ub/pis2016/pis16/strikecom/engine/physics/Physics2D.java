package edu.ub.pis2016.pis16.strikecom.engine.physics;

import java.util.ArrayList;
import java.util.List;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Akira on 2016-03-15.
 */
public class Physics2D {

	private ArrayList<Body> staticBodies; // all   staticBodies here
	private ArrayList<DynamicRectangleObject> dynamicBodies; // all   dynamicBodies here
	private Vector2 gravity; // not sure if needed
	private float worldWidth;
	private float worldHeight;
	private float scale; //lets store here  grafix/physics scaling rate here for now
	private SpatialHashGrid spatialHashGrid; // collision detection optimisation

	public float getWorldWidth() {
		return worldWidth;
	}

	public float getWorldHeight() {
		return worldHeight;
	}

	public float getScale() {
		return scale;
	}

	/**
	 * creates 2d physics world
	 * @param worldWidth width of world
	 * @param worldHeight height of world
	 * @param cellSize size of a cell used in hash grid optimizaton - should be  more than 2x bigger than largest
	 *                    game object
	 * @param gravity gravity vector, not sure if needed
	 */
	public Physics2D(float worldWidth, float worldHeight, float cellSize, Vector2 gravity){
		this.staticBodies =new ArrayList<>(); //initialize list of staticBodies
		this.dynamicBodies =new ArrayList<>(); //initialize list of dynamicBodies
		this.gravity= gravity;
		this.worldWidth=worldWidth;
		this.worldWidth=worldWidth;

		spatialHashGrid= new SpatialHashGrid(worldWidth, worldHeight, cellSize);

	}

	public void update(float delta){//update all Bodies of the game
		spatialHashGrid.clearDynamicCells(); //clear previous cells numbers assigned to dynamic objects

		//update velocity and positions of all dynamic bodies
		for(int i =0;i<this.dynamicBodies.size();i++){
			DynamicRectangleObject obj=this.dynamicBodies.get(i);
			obj.getVelocity().add(obj.getAccel().x*delta,obj.getAccel().y*delta);// I'll check how movement is implemented, probably i
			// should just update positions
			obj.getPosition().add(obj.getVelocity());
			obj.getBounds().getLlpos().add(obj.getVelocity());
			this.spatialHashGrid.insertDynamicObject(obj); // insert back to  hash grid
		}
		// now lets handle collisions
		for(int i =0;i<this.dynamicBodies.size();i++) { // currecntly it checks TWICE - fix this asap
			DynamicRectangleObject obj = this.dynamicBodies.get(i);
			List<RectangleObject> colliders = this.spatialHashGrid.getPotentialColliders(obj);
			for(int j =0;j<colliders.size();j++) {
				if(obj.getBounds().overlaps(colliders.get(j).getBounds())){
					/*

					COLLISION DETECTED

					 */
				}
			}

		}


	}

	public void addStaticBody(Body b){//add static body to physics engine
		this.staticBodies.add(b);
		this.spatialHashGrid.insertStaticObject((RectangleObject)b); //warning

	}
	public void addDynamicBody(Body b){//add dynamic body to physics engine
		this.dynamicBodies.add((DynamicRectangleObject)b);

	}

	public void addContactListener(ContactListener cl){

	}
}
/*
* http://box2d.org/manual.pdf
* https://github.com/erincatto/Box2D
*
* */