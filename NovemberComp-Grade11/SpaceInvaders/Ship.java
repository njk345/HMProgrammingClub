
/**
 * This is a subclass of GImage that possesses methods necessary to ships used in the game — both BadShips and GoodShips
 * 
 * 
 */
import acm.graphics.*;
import acm.util.RandomGenerator;

public class Ship extends GImage
{
    private double motionAngle;
    private double stepLen;
    private double locXOffset;
    private double locYOffset;
    private double boundWidth;
    private double boundHeight;
    private double ballistic1XOffset;
    private double ballistic1YOffset;
    private double ballistic2XOffset;//only used by GoodShip
    private double ballistic2YOffset;
    private boolean remove;
    
    private int health; // 0 empty; 100 full
    private int spawnPoint;
    private int damageLevel; //damage it inflicts simply by colliding with object (not from ballistics)
    
    public Ship(String imgFile,double x,double y,double motionAngle)
    {
        super(imgFile,x,y);
        this.motionAngle = motionAngle;
    }
    
    public double getMotionAngle () {
        return this.motionAngle;
    }
    public void setMotionAngle (double newAng) {
        this.motionAngle = newAng;
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
    public int getDamageLevel () {
       return this.damageLevel;
    }
    public void setDamageLevel (int newDam) {
       this.damageLevel = newDam;
    }
    
    public void move (double dist) {
        this.movePolar(dist,this.motionAngle);
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
    
    public void setBallistic1XOffset (double xo) {
        this.ballistic1XOffset = xo;
    }
    public double getBallistic1XOffset () {
        return this.ballistic1XOffset;
    }
    public void setBallistic1YOffset (double yo) {
        this.ballistic1YOffset = yo;
    }
    public double getBallistic1YOffset () {
        return this.ballistic1YOffset;
    }
    public void setBallistic2XOffset (double xo) {
        this.ballistic2XOffset = xo;
    }
    public double getBallistic2XOffset () {
        return this.ballistic2XOffset;
    }
    public void setBallistic2YOffset (double yo) {
        this.ballistic2YOffset = yo;
    }
    public double getBallistic2YOffset () {
        return this.ballistic2YOffset;
    }
    
    public void setHealth (int h) {
        this.health = h;
    }
    public int getHealth () {
        return this.health;
    }
    public void subtractHealth (int amount) {
        this.health -= amount;
    }
    
    public void setSpawn (GPoint pt) {
        this.setLocation(locXOffset + pt.getX(),locYOffset+pt.getY());
    }
    public int getSpawnPoint () {
        return this.spawnPoint;
    }
    
    @Override
    public GPoint getLocation () {
        return new GPoint(this.getX()-locXOffset,this.getY()-locYOffset);
    }
    @Override
    public GRectangle getBounds () {
        return new GRectangle(this.getX()-locXOffset,this.getY()-locYOffset,boundWidth,boundHeight);
    }
    public GPoint getTopLCorner () {
        return new GPoint (this.getX()-locXOffset,this.getY()-locYOffset);
    }
    public GPoint getTopRCorner () {
        return new GPoint (this.getX()-locXOffset+this.getWidth(),this.getY()-locYOffset);
    }
    public GPoint getBottomLCorner () {
        return new GPoint (this.getX()-locXOffset,this.getY()-locYOffset+this.getHeight());
    }
    public GPoint getBottomRCorner () {
        return new GPoint (this.getX()-locXOffset+this.getWidth(),this.getY()-locYOffset+this.getHeight());
    }
    
    
    public boolean isTouchingShip (Ship s) {
        if (this.getBounds().intersects(s.getBounds())) {
           return true; 
        }
        return false;
    }
    public boolean isTouchingAsteroid (Asteroid a) {
        GRectangle bounds = this.getBounds();
        for (double y = bounds.getY(); y < bounds.getY() + bounds.getHeight(); y++) {
            for (double x = bounds.getX(); x < bounds.getX() + bounds.getWidth(); x++) {
                if (a.contains(x,y)) {
                    return true;
                }
             }
        }
        return false;
    }
    public boolean isTouchingBallistic (Ballistic b) {
        if (b.getMotionAngle() > 180) {
            if (this.getBounds().intersects(b.getNose(false))) {
                return true;
            }
            return false;
        }
        else {
            if (this.getBounds().intersects(b.getNose(true))) {
                return true;
            }
            return false;
        }
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
