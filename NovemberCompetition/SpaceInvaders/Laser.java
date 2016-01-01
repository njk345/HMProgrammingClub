
/**
 * This is a subclass of Ballistic. It creates GImage instances using either the "greenLaser.png" or "redLaser.png" file
 * 
 * 
 */
public class Laser extends Ballistic
{
    public static String greenLaserFile = "greenLaser.png";
    public static String redLaserFile = "redLaser.png";
    public Laser(String imgFile,double x,double y,double motionAngle)
    {
        super(imgFile,x,y,motionAngle);
        this.setStepLen(5);
        this.setDamageLevel(10); //this way, it takes at least two lasers to kill even the weakest Tie Fighter variant
        if (imgFile.equals(redLaserFile)) {
            this.setLocXOffset(-432);
            this.setLocYOffset(-440);
            this.setBoundWidth(32);
            this.setBoundHeight(18);
            this.setSize(900,900);  
            this.setNoseWidth(3);
            this.setNoseHeight(3);
        }
        else if (imgFile.equals(greenLaserFile)) {
            this.setLocXOffset(-184);
            this.setLocYOffset(-190);
            this.setBoundWidth(30);
            this.setBoundHeight(18);
            this.setSize(400,400);
            this.setNoseWidth(3);
            this.setNoseHeight(3);
        }
        this.setLocation(this.getLocXOffset()+x,this.getLocYOffset()+y);
    }
}
