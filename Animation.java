import greenfoot.*; 

/**
 * Class for handling play-once animations.
 * 
 * @author Ainoras Žukauskas
 * @version 2018-02-27
 */
public class Animation{
    private GifImage animation;
    private GreenfootImage firstImg = null;
    private boolean playedOnce = false;
    private boolean firstImageCycled = false;
    
    /**
     * The Constructor of Animation
     * @param file  path to a .gif image.
     */
    public Animation(String file){
        animation = new GifImage(file);
        animation.pause();
    }
    
    /**
     * Plays a gif image until it runs out of frames.
     * <p>
     * After there are no frames left, the Method returns null.
     * @return A GreenfootImage which represents a single frame in the animation. Returns null after first loop.
     */
    public GreenfootImage playOnce(){
        if(!playedOnce){
            if(!animation.isRunning()){
                animation.resume();
            }
            GreenfootImage tempImg = animation.getCurrentImage();
            if(firstImg == null){
                firstImg = tempImg;
                return tempImg;
            }
            else if(firstImg.toString().equals(tempImg.toString()) && !firstImageCycled){
                return tempImg;
            }
            else{
                firstImageCycled = true;
                if(firstImg.toString().equals(tempImg.toString())){
                    playedOnce = true;
                }
                else{
                    return tempImg;
                }
            }
        }
        return null;
    }
}
