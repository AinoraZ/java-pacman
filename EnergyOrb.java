import greenfoot.*;

/**
 * Class for spawning energy orbs in game.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class EnergyOrb extends Actor
{
    /**
     * The Constructor of EnergyOrb.
     */
    public EnergyOrb(){
        GreenfootImage playerImg = new GreenfootImage("images/pacman.gif");
        playerImg.scale(12, 12);
        setImage(playerImg);
    }
    
    /**
     * Gets actor of this EnergyOrb object.
     * @return Actor object of EnergyOrb
     * @see greenfoot.Actor
     */
     public Actor getActor(){
        return this;
    }
}
