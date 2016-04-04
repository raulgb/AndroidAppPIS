package edu.ub.pis2016.pis16.strikecom.engine.physics;

/**
 * all collision algorithms will be stored here
 */
public class OverlapAlgorithms {
    /**
     * generic circle-circle collision detection
     * @param c1 first circle
     * @param c2 second circle
     * @return true if they are collided
     */
    public static boolean overlapCircles(Circle c1, Circle c2){
        float dx=c1.getCenter().x-c2.getCenter().x;
        float dy=c1.getCenter().y-c2.getCenter().y;
        float rs=c1.getRadius()+c2.getRadius();
        return (dx*dx+dy*dy<=rs*rs);
    }

    /**
     * generic rectangle-rectangle  Axis-Aligned Bounding Box collision detection
     * @param r1 first rectangle
     * @param r2 second rectangle
     * @return true if they are collided
     */
    public static boolean overlapRectangles(Rectangle r1, Rectangle r2){
        if(r1.getLlpos().x<r2.getLlpos().x+r2.getWidth()&&r1.getLlpos().x+r1.getWidth()>r2.getLlpos().x&&r1.getLlpos().y<r2.getLlpos().y+r2.getHeight()&&r1.getLlpos().y+r1.getHeight()>r2.getLlpos().y){
            return true;
        }
        else{return false;}
    }

    /**
     * generic circlre-rectangle collision detection
     * @param c circle
     * @param r rectangle
     * @return true if they are collided
     */
    public static boolean overlapCircleRectangle(Circle c, Rectangle r){
        float closestX=c.getCenter().x;
        float closestY=c.getCenter().y;
        if (c.getCenter().x<r.getLlpos().x){
            closestX=r.getLlpos().x;
        }
        else if (c.getCenter().x>r.getLlpos().x+r.getWidth()){
            closestX=r.getLlpos().x+r.getWidth();
        }
        if (c.getCenter().y<r.getLlpos().y){
            closestY=r.getLlpos().y;
        }
        else if (c.getCenter().y>r.getLlpos().y+r.getHeight()){
            closestY=r.getLlpos().y+r.getHeight();
        }
        closestX=c.getCenter().x-closestX;
        closestY=c.getCenter().y-closestY;
        return (closestX*closestX+closestY*closestY<=c.getRadius()*c.getRadius());


    }
}
