import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class for spawning pallets in game.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class Pallets extends Actor
{   
    /**
     * The Constructor of Pallets.
     */
    public Pallets(){
        GreenfootImage playerImg = new GreenfootImage("images/pacman.gif");
        playerImg.scale(6, 6);
        setImage(playerImg);
    }

    /**
     * Gets actor of this Pallets object.
     * @return Actor object of Pallets.
     * @see greenfoot.Actor
     */
    public Actor getActor(){
        return this;
    }
}
