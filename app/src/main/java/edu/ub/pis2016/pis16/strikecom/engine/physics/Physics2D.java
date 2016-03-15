package edu.ub.pis2016.pis16.strikecom.engine.physics;

import java.util.ArrayList;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Akira on 2016-03-15.
 */
public class Physics2D {

	ArrayList<Body> bodies;

	public Physics2D(Vector2 gravity){
		bodies=new ArrayList<>(); //initialize list of bodies

	}

	public void update(float delta){//update all bodies of the game

	}

	public void addBody(Body b){//add body to physics engine

	}

	public void addContactListener(ContactListener cl){

	}
}
/*
* http://box2d.org/manual.pdf
* https://github.com/erincatto/Box2D
*
* */