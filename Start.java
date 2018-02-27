import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The start screen class.
 * 
 * @author Ainoras Žukauskas 
 * @version 2018-02-27
 */
public class Start extends World
{   
    private String key;
    private Background bgImg = new Background("images/start.jpg");
    
    /**
     * The Constructor for Start
     */
    public Start()
    {    
        super(new Background("images/start.jpg").getWidth(), new Background("images/start.jpg").getHeight(), 1);
        setBackground(bgImg.getImage());
        
    }
    
    public void act(){
        pressToContinue();
    }
    
    private void pressToContinue(){
        key = Greenfoot.getKey();
        if(key != null){
            if(Greenfoot.isKeyDown(key))
                Greenfoot.setWorld(new MyWorld());
        
        }
    }
}
