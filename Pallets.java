import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Pallets here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pallets extends Actor
{   
    
    LevelInfo level = new LevelInfo();
    /**
     * Act - do whatever the Pallets wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public Pallets(){
        GreenfootImage playerImg = new GreenfootImage("images/pacman.gif");
        playerImg.scale(6, 6);
        setImage(playerImg);
    }
    public void act() 
    {

    }
    
     public Actor returnActor(){
        return this;
    }
}
