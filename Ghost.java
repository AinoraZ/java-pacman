import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Random;

/**
 * Write a description of class Ghost here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ghost extends Actor
{
    private GifImage img;
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
    
    
    private Player player;
    
    public Ghost(int _id, Player _player){
        id = _id;
        setImg();
        player = _player;
    }
    
    public void act() 
    {
        GreenfootImage ghImg = img.getCurrentImage();
        ghImg.scale(40, 40);
        setImage(ghImg);
        
        keyPressed();
        moveToTarget();
        handlePlayer();
    }
    
    public boolean touchingPlayer(){
        return isTouching(Player.class);
    }
    
    public void handlePlayer(){
        if(touchingPlayer() && !edible && !player.getDeath()){
            player.handleGhostTouch();
        }
    }
    
    
    public void moveToTarget(){
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
   
    public void keyPressed(){
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
    
    public void rotationSetter(){
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
    
    public void stepMove(int dist){
        /*
         * Moves player 1-by-1, ensuring he does not pass the tile.
         */
        for(int x = 0; x < dist; x++){
           if(getX() != level.getX(targetX) || getY() != level.getY(targetY)){
              setLocation(getX() + (addX), getY() + (addY));
           }
        }
    }
    
    public void setEdible(){
        img = new GifImage("images/eaten.gif");
        edible = true;
        moveSpeed = edibleSpeed;
    }
    
    public void reset(){
        setImg();
        rotation = 0;
        moving = false;
        addX = 0; 
        addY = 0;
        targetX = 0;
        targetY = 0;
    }
    
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
    }
    
    
    public void setBlink(){
        img = new GifImage("images/ghostblink.gif");
    }
    
    public Actor getActor(){
        return this;
    }
}
