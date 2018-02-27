import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

/**
 * The Enemy (Ghost) class containing all movement and interactions with the Player.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class Ghost extends Actor
{
    private GifImage img;
    
    /**
    * Stores information about the current level.
    */
    public LevelInfo level = new LevelInfo();
    
    private int addX = 0;
    private int addY = 0;
    private int id;
    
    private int targetX;
    private int targetY;
    
    private boolean moving = false;
    
    private int rotation = 0;
    
    private GifImage orange = new GifImage("images/orange.gif");
    private GifImage red = new GifImage("images/red.gif");
    private GifImage cyan = new GifImage("images/cyan.gif");
    private GifImage pink = new GifImage("images/pink.gif");
    
    private boolean edible = false;
    
    private int defaultSpeed = 2;
    private int edibleSpeed = 1;
    private int moveSpeed = defaultSpeed;
    
    private boolean noLimit = true;
    
    
    private Player player;
    
    /**
     * The Constructor of Ghost class.
     * @param _id       The identification number of a Ghost instance.
     * @param _player   The player to interact with.
     */
    public Ghost(int _id, Player _player){
        id = _id;
        setImg();
        player = _player;
    }
    
     /**
     * The main loop of the Ghost class
     */
    public void act() 
    {
        GreenfootImage ghImg = img.getCurrentImage();
        ghImg.scale(40, 40);
        setImage(ghImg);
        
        keyPressed();
        moveToTarget();
        handlePlayer();
    }
    
    /**
     * Gets if this Ghost is touching a Player.
     * @return boolean  True if this Ghost is touching the Player. False otherwise.
     */
    public boolean touchingPlayer(){
        return isTouching(Player.class);
    }
    
    private void handlePlayer(){
        if(touchingPlayer() && !edible && !player.getDeath()){
            player.handleGhostTouch();
        }
    }
    
    private void moveToTarget(){
        int[] tiles = level.findTile(getX(), getY());
        
         if(moving == false && !(addX == 0 && addY == 0)){
            if(level.legalMove(targetX, targetY) >= 1){
                //turnTowards(getX() + addX, getY() + addY);
                if(addX > 0){
                    rotation = 0;
                }
                else if(addX < 0){
                    rotation = 180;
                }
                else if(addY > 0){
                    rotation  = 90;
                }
                else if(addY < 0){
                    rotation = 270;
                }
                
                stepMove(moveSpeed);
                moving = true;
                
            }
            rotationSetter();
            if(level.legalMove(targetX, targetY) >= 1){
                stepMove(moveSpeed);
                moving = true;
            }
            
        }
        else{
            stepMove(moveSpeed);
            
            if(getX() == level.getX(targetX) && getY() == level.getY(targetY)){
                moving = false;
            }
        }
    }
   
    private void keyPressed(){
        if(!edible){
            Random rand = new Random();
            int[] tiles = level.findTile(getX(), getY());
            int n = rand.nextInt(4) + 1;
            int not = (rotation / 90) + 1;
            
            while(n == not){
                n = rand.nextInt(4) + 1;
            }
            
            if(moving == false){
                if(n == 3){
                    addX = 1;
                    addY = 0;
                }
                else if(n == 4){
                    addX = 0;
                    addY = 1;
                }
                else if(n == 1){
                    addX = -1;
                    addY = 0;
                }
                else if(n == 2){
                    addX = 0;
                    addY = -1;
                }
                targetX = tiles[0] + addX;
                targetY = tiles[1] + addY;
            }
        }
        else{
            if(moving == false){
                int[] playerTiles = level.findTile(player.getX(), player.getY());
                int[] ghostTiles = level.findTile(getX(), getY());
                double maximum = 0;
                int _addX = 0;
                int _addY = 0;
                if((rotation != 180 || noLimit) && level.legalMove(ghostTiles[0] + 1, ghostTiles[1]) > 0){
                    double temp = distanceCalc(playerTiles[0], playerTiles[1], ghostTiles[0] + 1, ghostTiles[1]);
                    if(temp > maximum){
                        maximum = temp;
                        _addX = 1;
                        _addY = 0;
                        //rotation = 0;
                    }
                }
                if((rotation != 270 || noLimit) && level.legalMove(ghostTiles[0], ghostTiles[1] + 1) > 0){
                    double temp = distanceCalc(playerTiles[0], playerTiles[1], ghostTiles[0], ghostTiles[1] + 1);
                    if(temp > maximum){
                        maximum = temp;
                        _addX = 0;
                        _addY = 1;
                        //rotation = 90;
                    }
                }
                if((rotation != 0 || noLimit) && level.legalMove(ghostTiles[0] - 1, ghostTiles[1]) > 0){
                    double temp = distanceCalc(playerTiles[0], playerTiles[1], ghostTiles[0] - 1, ghostTiles[1]);
                    if(temp > maximum){
                        maximum = temp;
                        _addX = -1;
                        _addY = 0;
                        //rotation = 180;
                    }
                }
                if((rotation != 90 || noLimit) && level.legalMove(ghostTiles[0], ghostTiles[1] - 1) > 0){
                    double temp = distanceCalc(playerTiles[0], playerTiles[1], ghostTiles[0], ghostTiles[1] - 1);
                    if(temp > maximum){
                        maximum = temp;
                        _addX = 0;
                        _addY = -1;
                        //rotation = 270;
                    }
                }
                
                addX = _addX;
                addY = _addY;
                
                noLimit = false;
                
                
                targetX = ghostTiles[0] + addX;
                targetY = ghostTiles[1] + addY;
            }
        }
    }
    
    private double distanceCalc(int aX, int aY, int bX, int bY){
        return Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
    }
    
    private void rotationSetter(){
        if(rotation == 0){
            addX = 1;
            addY = 0;
        }
        else if(rotation == 90){
            addX = 0;
            addY = 1;
        }
        else if(rotation == 180){
            addX = -1;
            addY = 0;
        }
        else if(rotation == 270){
            addX = 0;
            addY = -1;
        }
        int[] tiles = level.findTile(getX(), getY());
        targetX = tiles[0] + addX;
        targetY = tiles[1] + addY;
    }
    
    private void stepMove(int dist){
        /*
         * Moves player 1-by-1, ensuring he does not pass the tile.
         */
        for(int x = 0; x < dist; x++){
           if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
              setLocation(getX() + (addX), getY() + (addY));
           }
        }
    }
    
    /**
     * Sets this Ghost to an edible state.
     */
    public void setEdible(){
        img = new GifImage("images/eaten.gif");
        edible = true;
        moveSpeed = edibleSpeed;
    }
    
    /**
     * Soft-resets this Ghost.
     */
    public void reset(){
        setImg();
        rotation = 0;
        moving = false;
        addX = 0; 
        addY = 0;
        targetX = 0;
        targetY = 0;
        noLimit = true;
    }
    
    /**
     * Sets an Image according to this Ghost id.
     * @see greenfoot.GreenfootImage
     */
    public void setImg(){
        switch(id){
            case 1:
                img = orange;
                break;
            case 2:
                img = red;
                break;
            case 3:
                img = cyan;
                break;
            case 4:
                img = pink;
                break;
        }
        edible = false;
        moveSpeed = defaultSpeed;
        noLimit = true;
    }
    
    /**
     * Sets this Ghost to blink (indicate that it is about to become un-edible).
     */
    public void setBlink(){
        img = new GifImage("images/ghostblink.gif");
    }
    
    /**
     * Gets the Actor of this Ghost object.
     */
    public Actor getActor(){
        return this;
    }
}
