package edu.ub.pis2016.pis16.strikecom.controller;

import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;

/**
 * Created by Alexander Bevzenko on 14.03.2016.
 *
 * this is generic movement controller for all vehicles (mobile base and enemies)
 */
public class VehicleMovementController {
    public Vector2 pos, vel, accel; // copypaste from vehicle class
    private float angle=0; // copypaste from vehicle class
    private float rotationAngle=0; // vehicle wouldl slowly rotate to this angle
    private float rotationSpeed; // using this rotation speed
    private float moveSpeed; // desired movement speed


    /**
     * Default constructor
     */
    public VehicleMovementController(){
        this.pos = new Vector2();
        this.vel = new Vector2();
        this.accel = new Vector2();
        this.angle=0;
        this.rotationAngle=0;
        this.rotationSpeed=20;
        this.moveSpeed=16;
    }
    /*default Setters and getters*/
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    /**
     * sets rotation angle
     * @param vAngle vector2 raw input
     */

    public void setRotationAngle(Vector2 vAngle){//rotation controller -AB.

        this.rotationAngle = vAngle.angle();
        rotationAngle=rotationAngle%360;
        angle=angle%360;


    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    /**
     * slowly adjusts current angle to rotationAngle
     * @param delta deltaTime - general time interval between frames
     */
    private void rotationProcedure(float delta){//rotation controller -AB.

        if (Math.abs(rotationAngle-angle)%360>5){
			/*if((rotationAngle-angle)<(360-rotationAngle+angle)){
				angle+=20*delta;
			}else{
				angle-=20*delta;
			}*/
            if((rotationAngle-angle+360)%360<180){
                angle+=rotationSpeed*delta;
            }else{
                angle-=rotationSpeed*delta;
            }
        }


    }
}
