import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class for storing the Background image of a level.
 * 
 * @author  Ainoras Žukauskas
 * @version 2018-02-27
 */

public class Background extends GreenfootImage{

    /**
     * The Constructor of Background.
     * @param bg    path to a background image.
     */
    public Background(String bg)
    {  
       super(bg);
    }
    
    /**
     * Gets the Image of this Background object.
     * @return GreenfootImage of the background.
     * @see greenfoot.GreenfootImage
     */
    public GreenfootImage getImage(){
        return this;
    }
}
