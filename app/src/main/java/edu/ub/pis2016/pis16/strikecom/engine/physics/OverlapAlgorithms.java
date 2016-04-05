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
        float dx=c1.getPosition().x-c2.getPosition().x;
        float dy=c1.getPosition().y-c2.getPosition().y;
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
        if(r1.getPosition().x<r2.getPosition().x+r2.getWidth()&&r1.getPosition().x+r1.getWidth()>r2.getPosition().x&&r1.getPosition().y<r2.getPosition().y+r2.getHeight()&&r1.getPosition().y+r1.getHeight()>r2.getPosition().y){
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
        float closestX=c.getPosition().x;
        float closestY=c.getPosition().y;
        if (c.getPosition().x<r.getPosition().x){
            closestX=r.getPosition().x;
        }
        else if (c.getPosition().x>r.getPosition().x+r.getWidth()){
            closestX=r.getPosition().x+r.getWidth();
        }
        if (c.getPosition().y<r.getPosition().y){
            closestY=r.getPosition().y;
        }
        else if (c.getPosition().y>r.getPosition().y+r.getHeight()){
            closestY=r.getPosition().y+r.getHeight();
        }
        closestX=c.getPosition().x-closestX;
        closestY=c.getPosition().y-closestY;
        return (closestX*closestX+closestY*closestY<=c.getRadius()*c.getRadius());


    }
}
