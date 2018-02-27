import greenfoot.*;

/**
 * Class for displaying static images as actors.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class StaticImage extends Actor
{
    /**
     * The Constructor of StaticImage.
     */
    public StaticImage(String path)
    {  
       GreenfootImage img = new GreenfootImage(path);
       img.scale(500, 500);
       setImage(img);
    }
    
    /**
    * Gets actor of this StaticImage object.
    * @return Actor object of StaticImage
    * @see greenfoot.Actor
    */
    public Actor getActor(){
        return this;
    }
}
