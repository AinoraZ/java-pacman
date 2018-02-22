import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Scanner;
import java.io.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    
    private LevelInfo level = new LevelInfo();
    private Player player = new Player();
    private Ghost[] ghs = {new Ghost(1, player), 
                           new Ghost(2, player), 
                           new Ghost(3, player), 
                           new Ghost(4, player)};
    
    
    public boolean orbEaten = false;
    private Ghost[] removedGhs = new Ghost[4];
    
    private int highscore = 0;
    
    private int timer = 0;
    private int orbTime = 10;
    private int frameRate = 60;
    private int palletsCreated = 0;
    private Background bgImg = new Background();
    
    private int life = 3;
    
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(new Background().getWidth(), new Background().getHeight(), 1);
        setBackground(bgImg.returnImage());
        
        getHighscore();
        
        //addObject(player.returnActor(), 300, 240);
        addPallets();
        
        spawnGhosts();
        addObject(player.returnActor(), 610, 635);
        
        displayLife();
    }
    
    private void getHighscore(){
        File file = new File("./highscore.txt");
        try {

            Scanner scanner = new Scanner(file);
    
            if (scanner.hasNextInt()) {
                highscore = scanner.nextInt();
            }
            else{
                highscore = 0;
            }
            scanner.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void addPallets(){
        double startX = level.startX;
        double startY = level.startY;
        double tX = level.tX;
        double tY = level.tY;
        
        for(double y = 0; y < 29; y++){
            for(double x = 0; x < 26; x++){
                if(level.legalMove((int) x, (int) y) == 1){
                    addObject(new Pallets().returnActor(), (int) (startX + tX * x), (int) (startY + tY * y));
                    palletsCreated++;
                }
                if(level.legalMove((int) x, (int) y) == 2){
                    addObject(new EnergyOrb().returnActor(), (int) (startX + tX * x), (int) (startY + tY * y));
                    palletsCreated++;
                }
            }
        }
    }
    
    public void act(){
        handleScore();
        handleTunnel();
        ghostEdible();
        handleWin();
    }
    
    private void handleWin(){
        if(player.getPalletsEaten() == palletsCreated){
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
              new FileOutputStream("highscore.txt"), "utf-8"))) {
                  writer.write(Integer.toString(highscore));
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Greenfoot.stop();
        }
    }
    
    public void handleDeath(){
        if(life <= 0){
           System.out.println("Setting new world");
           Greenfoot.setWorld(new MyWorld());
           return;
        }
        for(int x = 0; x < 4; x++){
            if(ghs[x] != null){
                removeObject(ghs[x]);
            }   
        }
        spawnGhosts();
        reset();
        
        for(int x = 0; x < 4; x++){
            ghs[x].reset(); 
        }
        
        player.reset();
        
        removeObject(player);
        addObject(player.returnActor(), 610, 635);
        
    }
    
    public void subtract_life(){
        if(life > 0)
            life--;
        displayLife();
    }
    
    private void reset(){
        orbEaten = false;
        timer = 0;
    }
    
    private void spawnGhosts(){
        addObject(ghs[0].returnActor(), 730, 316);
        addObject(ghs[1].returnActor(), 730, 475);
        addObject(ghs[2].returnActor(), 460, 316);
        addObject(ghs[3].returnActor(), 460, 475);
    }
    
    private void displayLife(){
        GreenfootImage lifeImg = new GreenfootImage("life.png");
        lifeImg.scale(40, 40);
        bgImg = new Background();
        bgImg.drawImage(lifeImg, 50, 840);
        if(life >= 2)
            bgImg.drawImage(lifeImg, 75, 840);
        if(life == 3)
            bgImg.drawImage(lifeImg, 100, 840);
        setBackground(bgImg.returnImage());
    }
    
    private void handleScore(){
        String score = Integer.toString(player.returnScore());
        String hscore = Integer.toString(highscore);
        showText(score, 90, 130);
        if(player.returnScore() > highscore){
            highscore = player.returnScore();
            showText(score, 1110, 130);
        }   
        else{
            showText(hscore, 1110, 130);
        }
    }
    
    
    private void handleTunnel(){
        if(player.getX() == 970 && player.getY() == 395){
            player.setLocation(220, 395);
        }
        else if(player.getX() == 220 && player.getY() == 395){
            player.setLocation(970, 395);
        }
        for(int x = 0; x < 4; x++){
            if(ghs[x] != null){
                if(ghs[x].getX() == 970 && ghs[x].getY() == 395)
                    ghs[x].setLocation(220, 395);
                else if(ghs[x].getX() == 220 && ghs[x].getY() == 395)
                    ghs[x].setLocation(970, 395);
            }
        }
    }
    
    public void handleEat(){
        for(int x = 0; x < 4; x++){
            if(ghs[x] != null){
                if(ghs[x].touchingPlayer()){
                    removeObject(ghs[x]);
                    removedGhs[x] = ghs[x];
                    ghs[x] = null;
                }
            }
        }
    }
    
    public void handleOrbEat(){
        orbEaten = true;
        timer = 0;
    }
    
    public void ghostEdible(){
        if(orbEaten){
            if(timer == 0){
                for(int x = 0; x < 4; x++){
                    if(ghs[x] != null){
                        ghs[x].setEdible();
                    }
                }
                timer += 1;
                player.setGhostEdible(true);
            }
            else{
                timer += 1;
            }
            if(timer == frameRate * orbTime){
                orbEaten = false;
                player.setGhostEdible(false);
                timer = 0;
                for(int x = 0; x < 4; x++){
                    if(removedGhs[x] != null){
                        ghs[x] = new Ghost(x+1, player);
                        removedGhs[x] = null;
                        addObject(ghs[x].returnActor(), 460, 316);
                    }
                    ghs[x].setImg();
                }
                
            }
        }
    }
    
}
