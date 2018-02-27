import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player class which is responsible for all input of the user and appropriate (legal) movement in game.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class Player extends Actor
{
    private GifImage img = new GifImage("images/pacman.gif");
    
    /**
     * Stores information about the current level.
     */
    public LevelInfo level = new LevelInfo();
    
    private int addX = 0;
    private int addY = 0;
    
    private int targetX;
    private int targetY;
    
    private boolean ghostIsEdible = false;
    
    private int score = 0;
    
    private String lastPressed = "";
    private boolean moving = false;
    private int palletsEaten = 0;
    
    private Animation deathAnimation = new Animation("images/die.gif");
    
    private boolean death = false;
    
    /** 
     * Shows if player ran out of lives 
     */
    public boolean alive = true;
    
    private int timer = 0;
    private int defaultTime = 10;
    
    /**
     * The movement speed of player per frame
     */
    public int moveSpeed = 8;
    
    
    private GreenfootSound music = new GreenfootSound("sounds/Waka.mp3");
    private GreenfootSound deathSound = new GreenfootSound("sounds/dead.mp3");
    private GreenfootSound startSound = new GreenfootSound("sounds/start-sound.mp3");
    private boolean audioPlayed = false;
    
    
    /**
     * The main loop of the Player class
     */
    public void act(){   
        if(death == true){
            GreenfootImage tempImg = deathAnimation.playOnce();
            if(tempImg != null){
                tempImg.scale(40, 40);
                setImage(tempImg);
            }
            else if(alive){
                MyWorld world = getWorldOfType(MyWorld.class);
                world.handleDeath();
                if(world.life == 0){
                    alive = false;
                }
            }
        }
        else{
            GreenfootImage playerImg = img.getCurrentImage();
            playerImg.scale(40, 40);
            setImage(playerImg);
            
            if(!audioPlayed){
                startSound.play();
                audioPlayed = true;
                Greenfoot.delay(300);
            }
            
            keyPressed();
            setMovement();
            moveToTarget();
            updateTimer();
            
            eatPallets();
            eatOrbs();
            eatGhosts();
        }
    }
    
    private void updateTimer(){
        if(timer > 0){
            timer--;
        }
    }
    
    /**
     * Called when player is touched by a hostile Ghost.
     * @see Ghost
     */
    public void handleGhostTouch(){
        death = true;
        music.stop();
        getWorldOfType(MyWorld.class).subtract_life();
        deathSound.play();
    }
    
    /**
     * Checks if player is touching a pallet.
     * <p>
     * If player is touching a pallet, it is removed and added to the score.
     */
    public void eatPallets(){
        if(isTouching(Pallets.class)){
            score += 10;
            palletsEaten++;
        }
        removeTouching(Pallets.class);
    }
    
    /**
     * Checks if player is touching an orb.
     * <p>
     * If player is touching an orb, it is removed and all ghosts become edible (and non-hostile).
     * @see handleOrbEat()
     */
    public void eatOrbs(){
        if(isTouching(EnergyOrb.class)){
            getWorldOfType(MyWorld.class).handleOrbEat();
            palletsEaten++;
        }
        removeTouching(EnergyOrb.class);
    }
    
    /**
     * Checks if player is touching an edible Ghost.
     * <p>
     * If player is touching an edible Ghost, it is removed and added to the score.
     * @see setGhostEdible
     */
    public void eatGhosts(){
        if(ghostIsEdible){
            int touchNum = getIntersectingObjects(Ghost.class).size();
            score += 100 * touchNum;
            getWorldOfType(MyWorld.class).handleEat();
        }
    }
    
    /**
     * Sets whether the ghosts are edible or not.
     * @param edible    boolean value of the state of the Ghosts
     * @see Ghost
     */
    public void setGhostEdible(boolean edible){
        ghostIsEdible = edible;
    }
    
    /**
     * Gets whether the player is dead.
     * @return boolean value of player state. True if dead, false otherwise.
     */
    public boolean getDeath(){
        return death;
    }
    
    /**
     * Gets amount of pallets eaten.
     * @return integer of pallets eaten in this level.
     */
    public int getPalletsEaten(){
        return palletsEaten;
    }
    
    /**
     * Gets current score of player.
     * @return integer of the current score
     */
    public int getScore(){
        return score;
    }
    
    private void setMovement(){
        int[] tiles = level.findTile(getX(), getY());
        if(moving == false){
            if(lastPressed.equals("right")){
                addX = 1;
                addY = 0;
                timer = defaultTime;
            }
            else if(lastPressed.equals("left")){
                addX = -1;
                addY = 0;
                timer = defaultTime;
            }
            else if(lastPressed.equals("up")){
                addX = 0;
                addY = -1;
                timer = defaultTime;
            }
            else if(lastPressed.equals("down")){
                addX = 0;
                addY = +1;
                timer = defaultTime;
            }
            targetX = tiles[0] + addX;
            targetY = tiles[1] + addY;
        }
        else{
            if(timer == 0){
                if(lastPressed.equals("right") && getRotation() == 180){
                    addX = 1;
                    addY = 0;
                    targetX = tiles[0] + addX;
                    targetY = tiles[1] + addY;
                    moving = false;
                }
                else if(lastPressed.equals("left") && getRotation() == 0){
                    addX = -1;
                    addY = 0;
                    targetX = tiles[0] + addX;
                    targetY = tiles[1] + addY;
                    moving = false;
                }
                 else if(lastPressed.equals("up") && getRotation() == 90){
                    addX = 0;
                    addY = -1;
                    targetX = tiles[0] + addX;
                    targetY = tiles[1] + addY;
                    moving = false;
                }
                else if(lastPressed.equals("down") && getRotation() == 270){
                    addX = 0;
                    addY = 1;
                    targetX = tiles[0] + addX;
                    targetY = tiles[1] + addY;
                    moving = false;
                }
            }
        }
    }
    
    private void moveToTarget(){
        int[] tiles = level.findTile(getX(), getY());
        
         if(moving == false && !(addX == 0 && addY == 0)){
            if(!music.isPlaying())
                music.play();
            if(level.legalMove(targetX, targetY) >= 1){
                turnTowards(getX() + addX, getY() + addY);
                stepMove(moveSpeed);
            
                if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                    moving = false;
                }
                else{
                    moving = true;
                }
            }
            else{
                rotationSetter();
                if(level.legalMove(targetX, targetY) >= 1){
                    stepMove(moveSpeed);
                    if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                        moving = false;
                    }
                    else{
                        moving = true;
                    }
                }
            }
            
        }
        else{
            if(!music.isPlaying())
                music.play();
            stepMove(moveSpeed);  
            if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                moving = false;
            }
        }
    }
    
    private void stepMove(int dist){
        /*
         * Moves player 1-by-1, ensuring he does not pass the tile.
         */
        for(int x = 0; x < dist; x++){
           if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
               move(1);
           }
        }
    }
  
    private void keyPressed(){
        if(Greenfoot.isKeyDown("right"))
            lastPressed = "right";
        else if(Greenfoot.isKeyDown("left"))
            lastPressed = "left";
        else if(Greenfoot.isKeyDown("up"))
            lastPressed = "up";
        else if(Greenfoot.isKeyDown("down"))
            lastPressed = "down";
    }
    
    private void rotationSetter(){
        /* 
         * Function which sets next target to next tile in current path.
         * Important if illegal input is given so that pacman would not stop.
         */
        if(getRotation() == 0){
            addX = 1;
            addY = 0;
        }
        else if(getRotation() == 180){
            addX = -1;
            addY = 0;
        }
        else if(getRotation() == 270){
            addX = 0;
            addY = -1;
        }
        else if(getRotation() == 90){
            addX = 0;
            addY = 1;
        }
        int[] tiles = level.findTile(getX(), getY());
        targetX = tiles[0] + addX;
        targetY = tiles[1] + addY;
    }
    
    /**
     * Resets important player parameters for new life.
     */
    public void reset(){
        moving = false;
        addX = 0; 
        addY = 0;
        targetX = 0;
        targetY = 0;
        ghostIsEdible = false;
        death = false;
        lastPressed = "";
        deathAnimation = new Animation("images/die.gif");
    }
    
    /**
     * Gets the Actor of this Player object.
     * @return Actor object of Player
     * @see greenfoot.Actor
     */
    public Actor getActor(){
        return this;
    }
}
