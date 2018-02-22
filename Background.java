import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Background here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Background extends GreenfootImage
{

    /**
     * Constructor for objects of class Background.
     * 
     */
    public Background()
    {  
       //super("./test.jpg");
       super("./background.jpg");
       this.drawOval(200, 200, 100, 100);
    }
    
    public GreenfootImage returnImage(){
        return this;
    }
}
