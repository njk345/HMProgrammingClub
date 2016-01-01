
/**
 * Missile is subclass of Ballistic. It creates GImage instances with the "missile.png" file.
 * 
 * 
 */
public class Missile extends Ballistic
{
    public static String missileFile = "missile.png";
    public Missile(double x, double y, double motionAngle)
    {
        super(missileFile,x,y,motionAngle);
        this.setSize(100,100);
        this.setStepLen(4);
        this.setDamageLevel(20);
        this.setLocXOffset(-42);
        this.setLocYOffset(-40);
        this.setBoundWidth(24);
        this.setBoundHeight(15); 
        this.setNoseWidth(3);
        this.setNoseHeight(3);
        this.setLocation(this.getLocXOffset()+x,this.getLocYOffset()+y);
    }
}
