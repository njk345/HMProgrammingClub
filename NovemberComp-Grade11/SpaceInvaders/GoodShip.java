
/**
 *
 *This is a subclass of Ship. It creates GImage instances using "xwing.png"
 *The user controls an instance of this class throughout the game
 *
 */
import acm.graphics.*;
import acm.util.RandomGenerator;

public class GoodShip extends Ship
{
    public static String xwingFile = "xwing.png";
    public static String userFile = "GoodGuy.png";
    public GoodShip(double x, double y)
    {
        super(userFile,x,y,30);
        this.setSize(80,80);
        this.setStepLen(20);
        this.setLocXOffset(0);
        this.setLocYOffset(0);
        this.setBoundWidth(48);
        this.setBoundHeight(35);
        this.setBallistic1XOffset(23);
        this.setBallistic1YOffset(-20);
        this.setBallistic2XOffset(36);
        this.setBallistic2YOffset(8);
        this.setLocation(this.getLocXOffset()+x,this.getLocYOffset()+y);
        this.setDamageLevel(20);
    }
}
