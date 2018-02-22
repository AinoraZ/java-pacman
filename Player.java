import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private GifImage img = new GifImage("./pacman.gif");
    private LevelInfo level = new LevelInfo();
    private int addX = 0;
    private int addY = 0;
    
    private int targetX;
    private int targetY;
    
    private boolean ghostIsEdible = false;
    
    private int score = 0;
    
    private String lastPressed = "";
    private boolean moving = false;
    private int palletsEaten = 0;
    
    private Animation deathAnimation = new Animation("./die.gif");
    
    private boolean death = false;
    
    public void act() 
    {      
        if(death == true){
            GreenfootImage tempImg = deathAnimation.playOnce();
            if(tempImg != null){
                tempImg.scale(40, 40);
                setImage(tempImg);
            }
            else{
                getWorldOfType(MyWorld.class).handleDeath();
            }
        }
        else{
            GreenfootImage playerImg = img.getCurrentImage();
            playerImg.scale(40, 40);
            setImage(playerImg);
            
            keyPressed();
            setMovement();
            moveToTarget();
            eatPallets();
            eatOrbs();
            eatGhosts();
        }
    }
    
    public void eatPallets(){
        if(isTouching(Pallets.class)){
            score += 10;
            palletsEaten++;
        }
        removeTouching(Pallets.class);
    }
    
    public void handleGhostTouch(){
        death = true;
        getWorldOfType(MyWorld.class).subtract_life();
    }
    
    public boolean getDeath(){
        return death;
    }
    
    public void eatOrbs(){
        if(isTouching(EnergyOrb.class)){
            getWorldOfType(MyWorld.class).handleOrbEat();
            palletsEaten++;
        }
        removeTouching(EnergyOrb.class);
    }
    
    public int getPalletsEaten(){
        return palletsEaten;
    }
    
    public void eatGhosts(){
        if(ghostIsEdible){
            if(isTouching(Ghost.class)){
                score += 100;
            }
            getWorldOfType(MyWorld.class).handleEat();
        }
    }
    
    public void setGhostEdible(boolean edible){
        ghostIsEdible = edible;
    }
    
    public int returnScore(){
        return score;
    }
    
    public void setMovement(){
        int[] tiles = level.findTile(getX(), getY());
        if(moving == false){
            if(lastPressed.equals("right")){
                addX = 1;
                addY = 0;
            }
            else if(lastPressed.equals("left")){
                addX = -1;
                addY = 0;
            }
            else if(lastPressed.equals("up")){
                addX = 0;
                addY = -1;
            }
            else if(lastPressed.equals("down")){
                addX = 0;
                addY = +1;
            }
            targetX = tiles[0] + addX;
            targetY = tiles[1] + addY;
        }
        else{
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
                addY = +1;
                targetX = tiles[0] + addX;
                targetY = tiles[1] + addY;
                moving = false;
            }
            
        }
    }
    
    public void moveToTarget(){
        int[] tiles = level.findTile(getX(), getY());
        
        /* DEBUG
        System.out.println("PLayer pos: " + tiles[0] + ", " + tiles[1]);
        System.out.println("PLayer pos: " + getX() + ", " + getY());
        System.out.println("Info: " + (tiles[0] + addX) + ", " + (tiles[1] + addY) + 
        ((level.legalMove(tiles[0] + addX, tiles[1] + addY) == 1)? " Is legal" : " Is not legal"));
        DEBUG */

        
        //System.out.println("Rotation: " + getRotation());
        
         if(moving == false && !(addX == 0 && addY == 0)){
            if(level.legalMove(targetX, targetY) >= 1){
                //setRotation(180);
                turnTowards(getX() + addX, getY() + addY);
                move(2);
                moving = true;
            }
            rotationSetter();
            if(level.legalMove(targetX, targetY) >= 1){
                move(2);
                moving = true;
            }
            
        }
        else{
            if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
                move(1);
                if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
                    move(1);
                }
            }
            
            if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                moving = false;
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
            addY = +1;
        }
        int[] tiles = level.findTile(getX(), getY());
        targetX = tiles[0] + addX;
        targetY = tiles[1] + addY;
    }
    
    void reset(){
        moving = false;
        addX = 0; 
        addY = 0;
        targetX = 0;
        targetY = 0;
        ghostIsEdible = false;
        death = false;
        lastPressed = "";
        deathAnimation = new Animation("./die.gif");
    }
    
    public Actor returnActor(){
        return this;
    }
}
