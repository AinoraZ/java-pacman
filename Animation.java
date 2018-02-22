import greenfoot.*; 

/**
 * Write a description of class Animation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Animation{
    private GifImage animation;
    private GreenfootImage firstImg = null;
    private boolean playedOnce = false;
    private boolean firstImageCycled = false;
    
    public Animation(String file){
        animation = new GifImage(file);
        animation.pause();
    }
    
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
    
    public void resetPlayOnce(){
        playedOnce = false;
        firstImg = null;
        firstImageCycled = false;
    }
    
    public GreenfootImage loop(){
        if(!animation.isRunning()){
                animation.resume();
        }
        return animation.getCurrentImage();
    }
}
