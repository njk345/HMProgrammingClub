import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import acm.util.RandomGenerator;
import acm.util.SoundClip;
import java.util.ArrayList;
import java.util.Iterator;

public class SpaceInvaders extends GraphicsProgram
{
    private static String spaceBgFile = "space.jpg";
    public static double scrWidth;
    public static double scrHeight;
    private GImage background = new GImage(spaceBgFile,0,0);
    private GPoint spawn1;
    private GPoint spawn2;
    private GPoint spawn3;
    private GPoint spawn4;
    private GPoint spawn5;
    private GPoint spawn6;
    private GPoint spawn7;
    private GPoint spawn8;
    private GRectangle playerArea;
    private GoodShip user;
    private G3DRect healthMeter;
    private G3DRect healthHolder;
    private GLabel healthLabel;
    private G3DRect letThroughHolder;
    private G3DRect letThroughMeter;
    private GLabel letThroughLabel;
    private ArrayList<BadShip> onScreenEnemies = new ArrayList<BadShip>();
    private ArrayList<Asteroid> onScreenAsteroids = new ArrayList<Asteroid>();
    private ArrayList<Ballistic> onScreenEnemyBallistics = new ArrayList<Ballistic>();
    private ArrayList<Ballistic> onScreenUserBallistics = new ArrayList<Ballistic>();
    private ArrayList<Ballistic> userBallisticQueue = new ArrayList<Ballistic>();
    private boolean playerDead;
    private boolean playerWon;
    private boolean gamePaused;
    private boolean levelHappening;
    private boolean repeatGame;
    private boolean playAgainDecisionMade;
    private int missilesUsed;
    private int missilesPerLevel;
    private JLabel missilesLeftLabel;
    private JLabel spacer1;
    private JButton pause;
    private JLabel levelNumLabel;
    private JLabel spacer2;
    private JLabel scoreLabel;
    private JButton unpause;
    private JButton playAgain;
    private JButton notAgain;
    private RandomGenerator rand = RandomGenerator.getInstance();

    @Override
    public void init () {     
        scrWidth = getWidth();
        scrHeight = getHeight();
        background.setSize(scrWidth,scrHeight);
        //add(background);

        playerArea = new GRectangle(scrWidth/9,scrHeight*1/2,scrWidth*1/3,scrHeight*2/5);
        //player will be restricted from moving outside bounds of this rectangle

        GRect area = new GRect(playerArea.getX(),playerArea.getY(),playerArea.getWidth(),playerArea.getHeight());
        area.setFilled(true);
        area.setFillColor(Color.green);

        spawn1 = new GPoint(scrWidth*6/10,0);
        spawn2 = new GPoint(scrWidth*8/10,0);
        spawn3 = new GPoint(scrWidth*9/10,0);
        spawn4 = new GPoint(scrWidth,0);
        spawn5 = new GPoint(scrWidth,scrHeight*1/10);
        spawn6 = new GPoint(scrWidth,scrHeight*2/10);
        spawn7 = new GPoint(scrWidth,scrHeight*3/10);
        spawn8 = new GPoint(scrWidth,scrHeight*4/10);

        user = makeXwingAt(scrWidth/4,scrHeight/2);
        add(user);

        addKeyListeners();

        healthHolder = new G3DRect(0,0,100,30);
        healthHolder.setRaised(true);
        healthHolder.setFilled(true);
        healthHolder.setFillColor(Color.white);
        add(healthHolder);
        healthMeter = new G3DRect(healthHolder.getX(),healthHolder.getY(),healthHolder.getWidth(),healthHolder.getHeight());
        healthMeter.setRaised(true);
        healthMeter.setFilled(true);
        healthMeter.setFillColor(Color.green);
        add(healthMeter);
        healthLabel = new GLabel("HEALTH",10,healthHolder.getHeight()+20);
        healthLabel.setFont(new Font("Tahoma",Font.BOLD,14));
        healthLabel.setColor(Color.green);
        add(healthLabel);

        letThroughHolder = new G3DRect(scrWidth*4/10,0,100,30);
        letThroughHolder.setRaised(true);
        letThroughHolder.setFilled(true);
        letThroughHolder.setFillColor(Color.white);
        add(letThroughHolder);
        letThroughMeter = new G3DRect(letThroughHolder.getX(),letThroughHolder.getY(),0,letThroughHolder.getHeight());
        letThroughMeter.setRaised(true);
        letThroughMeter.setFilled(true);
        letThroughMeter.setFillColor(Color.green);
        add(letThroughMeter);
        letThroughLabel = new GLabel("ENEMIES ESCAPED",letThroughHolder.getX()-10,letThroughHolder.getHeight()+20);
        letThroughLabel.setFont(new Font("Tahoma",Font.BOLD,14));
        letThroughLabel.setColor(Color.green);
        add(letThroughLabel);

        missilesLeftLabel = new JLabel ("MISSILES LEFT = 4");
        add(missilesLeftLabel,SOUTH);

        spacer1 = new JLabel ("      ");
        add(spacer1,SOUTH);

        pause = new JButton ("PAUSE");
        pause.addActionListener(this);
        add(pause,SOUTH);

        levelNumLabel = new JLabel ("Level 1");
        add(levelNumLabel,SOUTH);

        spacer2 = new JLabel ("  |  ");
        add(spacer2,SOUTH);

        scoreLabel = new JLabel ("SCORE = 0");
        add(scoreLabel,SOUTH);

        unpause = new JButton ("UNPAUSE");
        unpause.addActionListener(this);
        add(unpause,SOUTH);
        

        playerWon = false;
        playerDead = false;
        gamePaused = false;
        playAgainDecisionMade = false;
        addActionListeners();
    }

    public void keyPressed (KeyEvent e) {
        if (!playerDead && !gamePaused) {
            char key = e.getKeyChar();
            if (key == 'w') {
                user.setMotionAngle(90);
                user.move(user.getStepLen());
                if (user.isOutsideRectangle(playerArea)) {
                    user.move(-user.getStepLen());
                }
            }
            else if (key == 'a') {
                user.setMotionAngle(180);
                user.move(user.getStepLen());
                if (user.isOutsideRectangle(playerArea)) {
                    user.move(-user.getStepLen());
                }
            }
            else if (key == 's') {
                user.setMotionAngle(270);
                user.move(user.getStepLen());
                if (user.isOutsideRectangle(playerArea)) {
                    user.move(-user.getStepLen());
                }
            }
            else if (key == 'd') {
                user.setMotionAngle(0);
                user.move(user.getStepLen());
                if (user.isOutsideRectangle(playerArea)) {
                    user.move(-user.getStepLen());
                }
            }
            else if (key == 'l' && levelHappening) {
                userShoot(true);//shoot a laser
            }
            else if (key == 'm' && levelHappening && missilesUsed < missilesPerLevel) {
                userShoot(false);//shoot a missile
                missilesUsed += 2;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("PAUSE")) {
            gamePaused = true;
        }
        else if (e.getActionCommand().equals("UNPAUSE")){
            gamePaused = false;
        }  
        else if (e.getActionCommand().equals("PLAY AGAIN")) {
            playAgainDecisionMade = true;
            repeatGame = true;
        }
        else if (e.getActionCommand().equals("NOT AGAIN")) {
            playAgainDecisionMade = true;
            repeatGame = false;
        }
    }
    
    

    @Override
    public void run () {
        int score = 0;
        for (int level = 1; level <= 7; level++) {
            if (playerDead) {
                break; 
            }
            if (level == 7) {
                playerWon = true;
                break;
            }
            //number of BadShips and Asteroids to be spawned depending on level
            int numObjectsToSpawn = (level==1)?4:(level==2)?7:(level==3)?10:(level==4)?12:14;

            //number of milliseconds between automatic enemy spawning, dependent on level
            int enemySpawnInterval = (level==1)?200:(level==2)?180:(level==3)?160:(level==4)?140:(level==5)?120:100;

            //number of milliseconds between automatic enemy shooting, dependent on level
            int enemyShootInterval = (level==1)?300:(level==2)?250:(level==3)?200:(level==4)?175:150;

            missilesPerLevel = (level==1)?4:(level==2)?4:(level==3)?6:(level==4)?6:(level==5)?8:10;
            int maxEnemiesLetThrough = (level==1)?4:(level==2)?6:8;

            user.setHealth(100);
            healthMeter.setFillColor(Color.green);
            letThroughMeter.setFillColor(Color.green);
            levelNumLabel.setText("LEVEL " + level);

            int count = 0;
            int roidsSpawned = 0;
            int enemiesSpawned = 0;
            int enemiesLetThrough = 0;
            missilesUsed = 0;

            levelHappening = true;
            while (true) {
                count++;
                if (count % enemySpawnInterval == 0) {//each loop takes ~10ms --> this block executes ~every enemySpawnInterval*10 ms = 2 sec in level 1, etc.
                    int kind = rand.nextInt(0,1);
                    if ((kind == 0 || enemiesSpawned >= numObjectsToSpawn) && roidsSpawned < numObjectsToSpawn) {
                        Asteroid willSpawn = makeAsteroidAtRandomSpawn();
                        add(willSpawn);
                        onScreenAsteroids.add(willSpawn);
                        roidsSpawned++;
                    }
                    else if (enemiesSpawned < numObjectsToSpawn) {
                        int randMax = level > 4 ? 3 : level-1;
                        BadShip willSpawn = makeRandomTieAtRandomSpawn(randMax);
                        add(willSpawn);
                        onScreenEnemies.add(willSpawn);
                        enemiesSpawned++;
                    }
                    else {// max number of enemies and asteroids have spawned
                        if (onScreenEnemies.isEmpty()&&onScreenAsteroids.isEmpty()&&onScreenUserBallistics.isEmpty()&&onScreenEnemyBallistics.isEmpty()) {
                            levelHappening = false;
                            pause(2000);
                            break;
                        }
                    }
                }
                
                //add all Ballistics in the queue to the onscreen list and remove them from queue as we go
                for (Iterator<Ballistic> i = userBallisticQueue.iterator(); i.hasNext();) {
                    onScreenUserBallistics.add(i.next());
                    i.remove();
                }

                //move all spawned BadShips Asteroids and Ballistics forward by their own step lengths
                for (BadShip i : onScreenEnemies) {
                    i.move(i.getStepLen());
                }
                for (Asteroid i : onScreenAsteroids) {
                    i.move(i.getStepLen());
                }
                for (Ballistic i : onScreenEnemyBallistics) {
                    i.move(i.getStepLen());
                }
                for (int i = 0; i < onScreenUserBallistics.size(); i++) { // was throwing funny errors with for each loop syntax --> something with null pointers
                    onScreenUserBallistics.get(i).move(onScreenUserBallistics.get(i).getStepLen());
                }

                if (count % enemyShootInterval == 0) {
                    boolean laserOrMissile = rand.nextBoolean();
                    for (BadShip i: onScreenEnemies) {
                        Ballistic willSpawn = i.shoot(laserOrMissile);
                        add(willSpawn);
                        onScreenEnemyBallistics.add(willSpawn);
                    }
                }

                for (BadShip i: onScreenEnemies) {
                    if (!i.isFullyOffScreen()) {
                        for (int x = 0; x< onScreenUserBallistics.size(); x++) {
                            if (!onScreenUserBallistics.get(x).isFullyOffScreen()) {
                                if (i.isTouchingBallistic(onScreenUserBallistics.get(x))) {
                                    i.subtractHealth(onScreenUserBallistics.get(x).getDamageLevel());
                                    if (i.getHealth() <= 0) {
                                        i.setRemove(true); //tag element to be removed from on screen ArrayList later
                                        remove(i); //make invisible on game canvas
                                        score += i.getDamageLevel();
                                    }
                                    onScreenUserBallistics.get(x).setRemove(true);
                                    remove(onScreenUserBallistics.get(x));
                                }   
                            }
                            else {
                                onScreenUserBallistics.get(x).setRemove(true);
                                remove(onScreenUserBallistics.get(x));
                            }
                        }
                        if (i.isTouchingShip(user)) {
                            i.subtractHealth(user.getDamageLevel());
                            if (i.getHealth() <= 0) {
                                i.setRemove(true);
                                remove(i);
                            }
                            user.subtractHealth(i.getDamageLevel());
                        }
                    }
                    else { //it's fully off the screen
                        i.setRemove(true);
                        remove(i);
                        enemiesLetThrough++;
                    }
                }

                for (Asteroid i : onScreenAsteroids) {
                    if (!i.isFullyOffScreen()) {
                        for (int x = 0; x < onScreenUserBallistics.size(); x++) {
                            if (!onScreenUserBallistics.get(x).isFullyOffScreen()) {
                                if (onScreenUserBallistics.get(x).isTouchingAsteroid(i)) {
                                    i.setRemove(true);
                                    remove(i);
                                    score += i.getDamageLevel();
                                    onScreenUserBallistics.get(x).setRemove(true);
                                    remove(onScreenUserBallistics.get(x));
                                }
                            }
                            else {
                                onScreenUserBallistics.get(x).setRemove(true);
                                remove(onScreenUserBallistics.get(x));
                            }
                        }
                        if (i.isTouchingShip(user)) {
                            user.subtractHealth(i.getDamageLevel());
                            i.setRemove(true);
                            remove(i);
                        }
                    }
                    else {
                        i.setRemove(true);
                        remove(i);
                        enemiesLetThrough++;
                    }
                }

                for (Ballistic i : onScreenEnemyBallistics) {
                    if (!i.isFullyOffScreen()) {
                        if (user.isTouchingBallistic(i)) {
                            user.subtractHealth(i.getDamageLevel());
                            i.setRemove(true);
                            remove(i);
                        }
                    }
                    else {
                        i.setRemove(true);
                        remove(i);
                    }
                }

                for (int i = 0; i < onScreenUserBallistics.size(); i++) {
                    if (onScreenUserBallistics.get(i).isFullyOffScreen()) {
                        onScreenUserBallistics.get(i).setRemove(true);
                        remove(onScreenUserBallistics.get(i));
                    }
                }

                if (user.getHealth() > 0) {
                    healthMeter.setSize(user.getHealth(),healthMeter.getHeight());
                    if (user.getHealth() <= 60 && user.getHealth() > 25) {
                        healthMeter.setFillColor(Color.yellow); 
                    }
                    else if (user.getHealth() <= 25) {
                        healthMeter.setFillColor(Color.red);
                    }
                    //set healthMeter's width to user's health attribute (both range from 0-100)
                }
                else {
                    healthMeter.setSize(0,0); //collapse meter to no size
                    playerDead = true;
                    levelHappening = false;
                    pause(2000);
                    break;
                }

                if (enemiesLetThrough < maxEnemiesLetThrough) { //100/6 = 16.67 --> expand colored meter in increment of that, so 6 means full meter
                    letThroughMeter.setSize(enemiesLetThrough*100/maxEnemiesLetThrough,letThroughMeter.getHeight());
                    if (enemiesLetThrough > maxEnemiesLetThrough/3 && enemiesLetThrough <= maxEnemiesLetThrough*2/3) {
                        letThroughMeter.setFillColor(Color.yellow);
                    }
                    else if (enemiesLetThrough >= maxEnemiesLetThrough*2/3) {
                        letThroughMeter.setFillColor(Color.red); 
                    }      
                }
                else {
                    letThroughMeter.setSize(100,letThroughMeter.getHeight());
                    playerDead = true;
                    levelHappening = false;
                    pause(2000);
                    break;
                }

                scoreLabel.setText("SCORE = " + score);
                missilesLeftLabel.setText("MISSILES LEFT = " + (missilesPerLevel - missilesUsed));

                //iterators through each of "onScreen__" ArrayLists --> remove elements tagged for removal while iterating!
                for (Iterator<BadShip> i = onScreenEnemies.iterator(); i.hasNext();) {
                    if (i.next().shouldRemove()) {
                        i.remove(); //take out all elements tagged with "remove"
                    }
                }
                for (Iterator<Asteroid> i = onScreenAsteroids.iterator(); i.hasNext();) {
                    if (i.next().shouldRemove()) {
                        i.remove(); //take out all elements tagged with "remove"
                    }
                }
                for (Iterator<Ballistic> i = onScreenEnemyBallistics.iterator(); i.hasNext();) {
                    if (i.next().shouldRemove()) {
                        i.remove(); //take out all elements tagged with "remove"
                    }
                }
                for (Iterator<Ballistic> i = onScreenUserBallistics.iterator(); i.hasNext();) {
                    if (i.next().shouldRemove()) {
                        i.remove(); //take out all elements tagged with "remove"
                    }
                }

                //keep memory size of the ArrayLists consistent with their size and at minimum
                onScreenUserBallistics.trimToSize();
                onScreenEnemyBallistics.trimToSize();
                onScreenEnemies.trimToSize();
                onScreenAsteroids.trimToSize();

                while (gamePaused) {
                    pause(1);
                }

                pause(10);
            }
        }
        if (playerWon) {
            //display congrats
            removeAll();
            levelNumLabel.setText("WINNER!");
        }
        if (playerDead) {
            //display game over
            removeAll();
            levelNumLabel.setText("GAME OVER");
        }
    }


    private GoodShip makeXwingAt (double x,double y) {
        GoodShip ship = new GoodShip(x,y);
        return ship;
    }

    private BadShip makeTieAt (int type,double x,double y) {
        switch (type) {
            case 0:
            BadShip a = new BadShip(BadShip.baddieOne,x,y);
            return a;
            case 2:
            BadShip b = new BadShip(BadShip.baddieTwo,x,y);
            return b;
            case 1:
            BadShip c = new BadShip(BadShip.baddieThree,x,y);
            return c;
            case 3:
            BadShip d = new BadShip(BadShip.baddieFour,x,y);
            return d;
            default:
            return null;
            //bad input - nothing spawns
        }
    }

    private BadShip makeRandomTieAt (int randMax,double x,double y) {
        int r = rand.nextInt(0,randMax);
        return makeTieAt(r,x,y);
    }

    private BadShip makeRandomTieAtSpawn (int randMax,int spawn) {
        switch (spawn) {
            case 1:
            BadShip tie1 = makeRandomTieAt(randMax,spawn1.getX(),spawn1.getY());

            tie1.setMotionAngle(217.5);
            return tie1;
            case 2:
            BadShip tie2 = makeRandomTieAt(randMax,spawn2.getX(),spawn2.getY());

            tie2.setMotionAngle(215);
            return tie2;
            case 3:
            BadShip tie3 = makeRandomTieAt(randMax,spawn3.getX(),spawn3.getY());

            tie3.setMotionAngle(212.5);
            return tie3;
            case 4:
            BadShip tie4 = makeRandomTieAt(randMax,spawn4.getX(),spawn4.getY());

            tie4.setMotionAngle(210);
            return tie4;
            case 5:
            BadShip tie5 = makeRandomTieAt(randMax,spawn5.getX(),spawn5.getY());

            tie5.setMotionAngle(207.5);
            return tie5;
            case 6:
            BadShip tie6 = makeRandomTieAt(randMax,spawn6.getX(),spawn6.getY());

            tie6.setMotionAngle(205);
            return tie6;
            case 7:
            BadShip tie7 = makeRandomTieAt(randMax,spawn7.getX(),spawn7.getY());

            tie7.setMotionAngle(202.5);
            return tie7;
            case 8:
            BadShip tie8 = makeRandomTieAt(randMax,spawn8.getX(),spawn8.getY());

            tie8.setMotionAngle(200);
            return tie8;
            default:
            return null;
        }
    }

    private BadShip makeRandomTieAtRandomSpawn (int randMax) {
        int spawn = rand.nextInt(1,8);
        return makeRandomTieAtSpawn(randMax,spawn);
    }

    private Asteroid makeAsteroidAt (double x,double y) {
        return new Asteroid(x,y);
    }

    private Asteroid makeAsteroidAtSpawn(int spawn) {
        switch (spawn) {
            case 1:
            Asteroid roid1 = makeAsteroidAt(spawn1.getX(),spawn1.getY());
            roid1.setMotionAngle(217.5);
            return roid1;
            case 2:
            Asteroid roid2 = makeAsteroidAt(spawn2.getX(),spawn2.getY());
            roid2.setMotionAngle(215);
            return roid2;
            case 3:
            Asteroid roid3 = makeAsteroidAt(spawn3.getX(),spawn3.getY());
            roid3.setMotionAngle(212.5);
            return roid3;
            case 4:
            Asteroid roid4 = makeAsteroidAt(spawn4.getX(),spawn4.getY());
            roid4.setMotionAngle(210);
            return roid4;
            case 5:
            Asteroid roid5 = makeAsteroidAt(spawn5.getX(),spawn5.getY());
            roid5.setMotionAngle(207.5);
            return roid5;
            case 6:
            Asteroid roid6 = makeAsteroidAt(spawn6.getX(),spawn6.getY());
            roid6.setMotionAngle(205);
            return roid6;
            case 7:
            Asteroid roid7 = makeAsteroidAt(spawn7.getX(),spawn7.getY());
            roid7.setMotionAngle(202.5);
            return roid7;
            case 8:
            Asteroid roid8 = makeAsteroidAt(spawn8.getX(),spawn8.getY());
            roid8.setMotionAngle(200);
            return roid8;
            default:
            return null;
        }
    }

    private Asteroid makeAsteroidAtRandomSpawn() {
        int spawn = rand.nextInt(1,8);
        Asteroid roid = makeAsteroidAtSpawn(spawn);
        roid.setSpawnPoint(spawn);
        return roid;
    }

    private void userShoot (boolean lasers) {
        if (lasers) { //shoot laser from each gun
            Laser laz1 = new Laser(Laser.redLaserFile,user.getLocation().getX()+user.getBallistic1XOffset(),user.getLocation().getY()+user.getBallistic1YOffset(),30);
            Laser laz2 = new Laser(Laser.redLaserFile,user.getLocation().getX()+user.getBallistic2XOffset(),user.getLocation().getY()+user.getBallistic2YOffset(),30);
            add(laz1);
            add(laz2);
            onScreenUserBallistics.add(laz1);
            onScreenUserBallistics.add(laz2);
            userBallisticQueue.add(laz1);
            userBallisticQueue.add(laz2);
        }
        else { //shoot missile from each gun
            Missile miss1 = new Missile(user.getLocation().getX()+user.getBallistic1XOffset(),user.getLocation().getY()+user.getBallistic1YOffset(),30);
            Missile miss2 = new Missile(user.getLocation().getX()+user.getBallistic2XOffset(),user.getLocation().getY()+user.getBallistic2YOffset(),30);
            miss1.setDamageLevel(30);
            miss2.setDamageLevel(30);
            add(miss1);
            add(miss2);
            onScreenUserBallistics.add(miss1);
            onScreenUserBallistics.add(miss2);
            userBallisticQueue.add(miss1);
            userBallisticQueue.add(miss2);
        }
    }
}
