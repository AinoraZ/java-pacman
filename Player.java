import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    private GifImage img = new GifImage("images/pacman.gif");
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
    
    private int timer = 0;
    private int defaultTime = 10;
    
    public int moveSpeed = 3;
    
    public boolean alive = true;
    
    public void act() 
    {      
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
    
    public void handleGhostTouch(){
        death = true;
        getWorldOfType(MyWorld.class).subtract_life();
    }
    
    public void eatPallets(){
        if(isTouching(Pallets.class)){
            score += 10;
            palletsEaten++;
        }
        removeTouching(Pallets.class);
    }

    public void eatOrbs(){
        if(isTouching(EnergyOrb.class)){
            getWorldOfType(MyWorld.class).handleOrbEat();
            palletsEaten++;
        }
        removeTouching(EnergyOrb.class);
    }
    
    public void eatGhosts(){
        if(ghostIsEdible){
            int touchNum = getIntersectingObjects(Ghost.class).size();
            score += 100 * touchNum;
            getWorldOfType(MyWorld.class).handleEat();
        }
    }
    
    public void setGhostEdible(boolean edible){
        ghostIsEdible = edible;
    }
    
    public boolean getDeath(){
        return death;
    }
    
    public int getPalletsEaten(){
        return palletsEaten;
    }
    
    public int getScore(){
        return score;
    }
    
    public void setMovement(){
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
    
    public void moveToTarget(){
        int[] tiles = level.findTile(getX(), getY());
        
         if(moving == false && !(addX == 0 && addY == 0)){
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
            stepMove(moveSpeed);  
            if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                moving = false;
            }
        }
    }
    
    public void stepMove(int dist){
        /*
         * Moves player 1-by-1, ensuring he does not pass the tile.
         */
        for(int x = 0; x < dist; x++){
           if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
               move(1);
           }
        }
    }
  
    public void keyPressed(){
        if(Greenfoot.isKeyDown("right"))
            lastPressed = "right";
        else if(Greenfoot.isKeyDown("left"))
            lastPressed = "left";
        else if(Greenfoot.isKeyDown("up"))
            lastPressed = "up";
        else if(Greenfoot.isKeyDown("down"))
            lastPressed = "down";
    }
    
    public void rotationSetter(){
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
    
    void reset(){
        /*
         * Resets all needed parameters to restart pacman with new life.
         */
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
    
    public Actor returnActor(){
        return this;
    }
}
