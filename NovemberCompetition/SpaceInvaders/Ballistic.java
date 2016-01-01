
/**
 * This is a subclass of GImage. It defines certain methods and properties common and necessary among both types of Ballistic --> Laser and Missile
 * 
 * 
 */
import acm.graphics.*;
import acm.util.RandomGenerator;

public class Ballistic extends GImage
{
    private double motionAngle;
    private double locXOffset;
    private double locYOffset;
    private double noseWidth;
    private double noseHeight;
    private double boundWidth;
    private double boundHeight;
    private double stepLen;
    private int damageLevel;
    private boolean remove;
    //if Ballistic instance collides with object is determined by if its nose (a GRectangle) is touching something
    public Ballistic(String imgFile,double x,double y,double motionAngle)
    {
        super(imgFile,x,y);
        this.motionAngle = motionAngle;
    }
    public double getMotionAngle () {
        return this.motionAngle;
    }
    public void setMotionAngle (double motionAngle) {
        this.motionAngle = motionAngle;
    }
    public void setRemove (boolean yesOrNo) {
        this.remove = yesOrNo;
    }
    public boolean shouldRemove () {
        return this.remove;
    }
    public double getStepLen () {
        return this.stepLen; 
    }
    public void setStepLen (double newLen) {
       this.stepLen = newLen;
    }
    public void setDamageLevel (int newDam) {
       this.damageLevel = newDam;
    }
    public int getDamageLevel () {
       return this.damageLevel;
    }
    
    public void setLocXOffset (double xo) {
        this.locXOffset = xo;
    }
    public double getLocXOffset () {
        return this.locXOffset;
    }
    public void setLocYOffset (double yo) {
        this.locYOffset = yo;
    }
    public double getLocYOffset () {
        return this.locYOffset;
    }
    public void setBoundWidth (double bw) {
        this.boundWidth = bw;
    }
    public double getBoundWidth () {
        return this.boundWidth;
    }
    public void setBoundHeight (double bh) {
        this.boundHeight = bh;
    }
    public double getBoundHeight () {
        return this.boundHeight;
    }
    
    public void setNoseWidth (double nw) {
        this.noseWidth = nw;
    }
    public double getNoseWidth () {
        return this.noseWidth;
    }
    public void setNoseHeight (double nh) {
        this.noseHeight = nh;
    }
    public double getNoseHeight () {
        return this.noseHeight;
    }
    
    public void move (double dist) {
        this.movePolar(dist,this.motionAngle);
    }
    @Override
    public GPoint getLocation () {
        return new GPoint(this.getX()-locXOffset,this.getY()-locYOffset);
    }
    @Override
    public GRectangle getBounds () {
        return new GRectangle(this.getX()-locXOffset,this.getY()-locYOffset,boundWidth,boundHeight);
    }
    public GRectangle getNose (boolean ballisticFromUser) {
        if (!ballisticFromUser) {
            GRectangle noseBounds = new GRectangle(this.getX()-this.getLocXOffset(),this.getY()-this.getLocYOffset()+this.boundHeight-3,this.noseWidth,this.noseHeight);
            return noseBounds;
        }
        else {
            GRectangle noseBounds = new GRectangle(this.getX()-this.getLocXOffset()+25,this.getY()-this.getLocYOffset()+this.boundHeight-3-15,this.noseWidth,this.noseHeight);
            return noseBounds;
        }
    }
    
    public boolean isTouchingAsteroid (Asteroid a) {
        boolean dir = (this.motionAngle>180)? false:true;
        GOval y = a.getOvalBounds();
        GRectangle x = this.getNose(dir);
        GPoint topLCorner = new GPoint(x.getX(),x.getY());
        GPoint topRCorner = new GPoint(x.getX()+x.getWidth(),x.getY());
        GPoint bottomLCorner = new GPoint(x.getX(),x.getY()+x.getHeight());
        GPoint bottomRCorner = new GPoint(x.getX()+x.getWidth(),x.getY()+x.getHeight());
        if (y.contains(topLCorner) || y.contains(topRCorner) || y.contains(bottomLCorner) || y.contains(bottomRCorner)) {
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
