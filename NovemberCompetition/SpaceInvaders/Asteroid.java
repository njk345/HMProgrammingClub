
/**
 *  This is a subclass of GImage and constructs GImage instances using the "asteroid.png" file
 *  It defines certain methods necessary to all instances of these asteroid images
 * 
 * 
 */
import acm.graphics.*;
import acm.util.RandomGenerator;

public class Asteroid extends GImage
{
    private static String asteroidFile = "asteroid.png";
    private double motionAngle;
    private int spawnPoint;
    private double stepLen;
    private int damageLevel;
    private boolean remove;
    private int numTimesKilled;
    
    //if something collides with Asteroid determined by if its modified bounds (a GOval) contain other object
    public Asteroid(double x, double y)
    {
        super(asteroidFile,x,y);
        this.setSize(50,50);//only comes in one size
        this.motionAngle = 210;
        this.setStepLen(1.5);
        this.setDamageLevel(10);
        this.numTimesKilled = 0;
    }
    
    
    public double getMotionAngle () {
        return this.motionAngle;
    }
    public void setMotionAngle (double newAngle) {
        this.motionAngle = newAngle;
    }
    public void setRemove (boolean yesOrNo) {
        this.remove = yesOrNo;
    }
    public boolean shouldRemove () {
        return this.remove;
    }
    public void move (double dist) {
        this.movePolar(dist,this.motionAngle);
    }
    public double getStepLen () {
        return this.stepLen; 
    }
    public void setStepLen (double newLen) {
       this.stepLen = newLen;
    }
    public int getDamageLevel () {
       return this.damageLevel;
    }
    public void setDamageLevel (int newDam) {
       this.damageLevel = newDam; 
    }
    
    public void setSpawnPoint (int point) {
        this.spawnPoint = point;
    }
    public int getSpawnPoint () {
        return this.spawnPoint;
    }
    
    public int getTimesKilled () {
        return this.numTimesKilled;
    }
    public void addTimeKilled () {
        this.numTimesKilled += 1;
    }
    
    
    public GOval getOvalBounds() {
        GOval bounds = new GOval (this.getX(),this.getY(),this.getWidth(),this.getHeight());
        return bounds;
    }
    
    public boolean isTouchingShip (Ship s) {
        GOval roid = this.getOvalBounds();
        if (roid.contains(s.getTopLCorner()) || roid.contains(s.getTopRCorner()) || roid.contains(s.getBottomLCorner()) || roid.contains(s.getBottomRCorner())) {
           return true; 
        }
        return false;
    }
    
    
    public boolean isOutsideRectangle (GRectangle r) { //returns true if any of object's four corners are outside given rectangle
        GRectangle bounds = this.getBounds();
        double x = bounds.getX();
        double y = bounds.getY();
        if (!r.contains(x,y) || !r.contains(x+this.getWidth(),y) || !r.contains(x,y+this.getHeight()) || !r.contains(x+this.getWidth(),y+this.getHeight())) {
            return true;
        }
        return false;
    }
    public boolean isFullyOutsideRectangle (GRectangle r) {
        GRectangle bounds = this.getBounds();
        double x = bounds.getX();
        double y = bounds.getY();
        if (!r.contains(x,y) && !r.contains(x+this.getWidth(),y) && !r.contains(x,y+this.getHeight()) && !r.contains(x+this.getWidth(),y+this.getHeight())) {
            return true;
        }
        return false;
    }
    public boolean isFullyOffScreen () {
       GRectangle scr = new GRectangle(0,0,SpaceInvaders.scrWidth,SpaceInvaders.scrHeight);
       if (isFullyOutsideRectangle(scr)) {
           return true;
       }
       return false;
    }
}
