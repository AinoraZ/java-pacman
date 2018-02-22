import greenfoot.*;
/**
 * Write a description of class EnergyOrb here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EnergyOrb extends Actor
{
    
    public EnergyOrb(){
        GreenfootImage playerImg = new GreenfootImage("./pacman.gif");
        playerImg.scale(12, 12);
        setImage(playerImg);
    }
    public void act() 
    {

    }
    
     public Actor returnActor(){
        return this;
    }
}
