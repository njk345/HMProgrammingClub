
/**
 * This is subclass of ship, with distinction of being the type of ship that heads toward
 * and shoots lasers and missiles at the user. 
 * 
 * 
 */

import acm.graphics.*;
import acm.util.RandomGenerator;

public class BadShip extends Ship
{
    public static String tieFighterFile = "tieFighter.png";
    public static String tiePhantomFile = "tiePhantom.png";
    public static String tieInterceptorFile = "tieInterceptor.png";
    public static String tieBomberFile = "tieBomber.png";
    public static String tieVaderFile = "tieVader.png";
    public static String biggerBomberFile = "biggerBomber.png";
    public static String biggerFighterFile = "biggerFighter.png";
    
    public static String baddieOne = "EnemyOne.png";
    public static String baddieTwo = "EnemyTwo.png";
    public static String baddieThree = "EnemyThree.png";
    public static String baddieFour = "EnemyFour.png";
    
    private int type; // 0-6; identifies type of BadShip (which image file)
    private int numTimesKilled;
    
    
    public BadShip(String kind, double x, double y)
    {
        super(kind,x,y,225);
        this.setSize(30,30);
        this.setStepLen(2);
        this.numTimesKilled = 0;
        //then based on kind, fix (x,y) location and alter bounding rectangle 
        if (kind.equals(baddieOne)) {
            this.type = 0;
            this.setLocXOffset(-10);
            this.setLocYOffset(-8);
            this.setBoundWidth(62);
            this.setBoundHeight(60);
            this.setBallistic1XOffset(-5);
            this.setBallistic1YOffset(34);
            this.setLocation(-10+x,-8+y);
            this.setDamageLevel(10);
            this.setHealth(20);
        }
        else if (kind.equals(baddieTwo)) {
            this.type = 2;
            this.setLocXOffset(-10);
            this.setLocYOffset(-8);
            this.setBoundWidth(60);
            this.setBoundHeight(60);
            this.setBallistic1XOffset(-5);
            this.setBallistic1YOffset(35);
            this.setLocation(-10+x,-8+y);
            this.setDamageLevel(20);
            this.setHealth(30);
        }
        else if (kind.equals(baddieThree)) {
            this.type = 1;
            this.setLocXOffset(-10);
            this.setLocYOffset(-12);
            this.setBoundWidth(60);
            this.setBoundHeight(50);
            this.setBallistic1XOffset(0);
            this.setBallistic1YOffset(25);
            this.setLocation(-10+x,-12+y);
            this.setDamageLevel(20);
            this.setHealth(30);
        }
        else if (kind.equals(baddieFour)) {
            this.type = 3;
            this.setLocXOffset(-4);
            this.setLocYOffset(-8);
            this.setBoundWidth(65);
            this.setBoundHeight(60);
            this.setBallistic1XOffset(-10);
            this.setBallistic1YOffset(30);
            this.setLocation(-4+x,-8+y);
            this.setDamageLevel(25);
            this.setHealth(40);
        }
        /*else if (kind.equals(tieVaderFile)) {
            this.type = 6;
            this.setLocXOffset(-3);
            this.setLocYOffset(-15);
            this.setBoundWidth(65);
            this.setBoundHeight(60);
            this.setBallistic1XOffset(-5);
            this.setBallistic1YOffset(32);
            this.setLocation(-3+x,-15+y);
            this.setDamageLevel(40);
            this.setHealth(50);
        }
        else if (kind.equals(biggerBomberFile)) {
            this.type = 5;
            this.setLocXOffset(-5);
            this.setLocYOffset(-8);
            this.setBoundWidth(65);
            this.setBoundHeight(60);
            this.setBallistic1XOffset(-7);
            this.setBallistic1YOffset(35);
            this.setLocation(-5+x,-8+y);
            this.setDamageLevel(30);
        }
        else if (kind.equals(biggerFighterFile)) {
            this.type = 4;
            this.setLocXOffset(-5);
            this.setLocYOffset(-5);
            this.setBoundWidth(65);
            this.setBoundHeight(64);
            this.setBallistic1XOffset(-12);
            this.setBallistic1YOffset(47);
            this.setLocation(-5+x,-5+y);
            this.setDamageLevel(30);
            this.setHealth(40);
        }*/
    }
    public int getTimesKilled () {
        return this.numTimesKilled;
    }
    public void addTimeKilled () {
        this.numTimesKilled += 1;
    }
    public Ballistic shoot (boolean laser) {//create and return a laser or missile --> gets pushed into a ballistic Arraylist in Game, which adds it to canvas
        if (laser) {
            Laser laz = new Laser(Laser.greenLaserFile,this.getLocation().getX()+this.getBallistic1XOffset(),this.getLocation().getY()+this.getBallistic1YOffset(),this.getMotionAngle());
            return laz;
        }
        else {
            Missile miss = new Missile(this.getLocation().getX()+this.getBallistic1XOffset(),this.getLocation().getY()+this.getBallistic1YOffset(),this.getMotionAngle());
            return miss;
        }
    }
}
