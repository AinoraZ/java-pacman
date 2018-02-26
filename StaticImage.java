import greenfoot.*;

/**
 * Write a description of class GameOver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StaticImage extends Actor
{

    public StaticImage(String path)
    {  
       GreenfootImage img = new GreenfootImage(path);
       img.scale(500, 500);
       setImage(img);
    }
    
    public Actor getActor(){
        return this;
    }
}
