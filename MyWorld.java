import greenfoot.*;
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
    private Background bgImg;
    String path = MyWorld.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    public int life = 3;
    private int levelCounter = 1;
    
    private boolean gameover = false;
    
    private String key;
    
    
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(new Background("images/background.jpg").getWidth(), new Background("images/background.jpg").getHeight(), 1);
        
        File parent = new File(path);
        path = parent.getParentFile().getPath();
        
        getHighscore();
        
        bgImg = new Background(level.bg);
        setBackground(bgImg.returnImage());
        
        addPallets();
        
        spawnGhosts();
        addObject(player.returnActor(), level.player[0], level.player[1]);
        
        displayLife();
    }
    
    private void getHighscore(){
        try {
            File file = new File(path + "/highscore.txt");
            
            Scanner scanner = new Scanner(file);
    
            if (scanner.hasNextInt()) {
                highscore = scanner.nextInt();
            }
            else{
                highscore = 0;
            }
            scanner.close();
        } 
        catch (Exception e) {
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
                else if(level.legalMove((int) x, (int) y) == 2){
                    addObject(new EnergyOrb().returnActor(), (int) (startX + tX * x), (int) (startY + tY * y));
                    palletsCreated++;
                }
                
            }
        }
    }
    
    public void act(){
        if(!gameover){
            handleScore();
            handleTunnel();
            ghostEdible();
            handleWin();
        }
        else{
            pressToContinue();
            handleTunnel();
        }
    }
    
    public void pressToContinue(){
        key = Greenfoot.getKey();
        if(key != null){
            if(Greenfoot.isKeyDown(key))
                Greenfoot.setWorld(new MyWorld());
        
        }
    }
    
    private void handleWin(){
        if(player.getPalletsEaten() == palletsCreated){
            try{
                FileWriter fileWriter = new FileWriter(path + "/highscore.txt");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.print(Integer.toString(highscore) + "\n");
                printWriter.close();
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
            levelCounter++;
            
            if(levelCounter == 2){
                level.setLevel2();
            }
            else if(levelCounter == 3){
                level.setLevel3();
            }
            if(levelCounter == 4){
                addObject(new StaticImage("images/youwin.png").getActor(), bgImg.getWidth() / 2, bgImg.getHeight() / 2);
                Greenfoot.stop();
                return;
            }
            
            bgImg = new Background(level.bg);
            setBackground(bgImg.returnImage());
            displayLife();
   
            addPallets();
            hardReset();
            
            System.gc();
        }
    }
    
    public void hardReset(){
        reset();
        
        player.level = level;
        player.reset();
        
        for(int x = 0; x < ghs.length; x++){
            if(ghs[x] != null){
                removeObject(ghs[x]);
            }
            else{
                ghs[x] = new Ghost(x+1, player);
                removedGhs[x] = null;
            }
            ghs[x].level = level;
            ghs[x].reset();
        }
        spawnGhosts();
        
        removeObject(player);
        addObject(player.returnActor(), level.player[0], level.player[1]);
    }
    
    public void handleDeath(){
        if(life <= 0){
            addObject(new StaticImage("images/gameover.png").getActor(), bgImg.getWidth() / 2, bgImg.getHeight() / 2);
            gameover = true;
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
        addObject(player.returnActor(), level.player[0], level.player[1]);
        
        System.gc();
        
    }
    
    public void subtract_life(){
        if(life >= 1)
            life--;
        displayLife();
    }
    
    private void reset(){
        orbEaten = false;
        timer = 0;
    }
    
    private void spawnGhosts(){
        for(int x = 0; x < 4; x++){
            addObject(ghs[x].getActor(), level.ghosts[x][0], level.ghosts[x][1]);
        }
    }
    
    private void displayLife(){
        GreenfootImage lifeImg = new GreenfootImage("life.png");
        lifeImg.scale(40, 40);
        bgImg = new Background(level.bg);
        if(life >= 1)
            bgImg.drawImage(lifeImg, 50, 840);
        if(life >= 2)
            bgImg.drawImage(lifeImg, 75, 840);
        if(life == 3)
            bgImg.drawImage(lifeImg, 100, 840);
        setBackground(bgImg.returnImage());
    }
    
    private void handleScore(){
        String score = Integer.toString(player.getScore());
        String hscore = Integer.toString(highscore);
        showText(score, 90, 130);
        if(player.getScore() > highscore){
            highscore = player.getScore();
            showText(score, 1110, 130);
        }   
        else{
            showText(hscore, 1110, 130);
        }
    }
    
    
    private void handleTunnel(){
        int[][][] tunnelLocations = level.tunnelLocations;
        for(int y = 0; y < tunnelLocations.length; y++){
            int[][] tunnel = tunnelLocations[y]; 
            if(player.getX() == tunnel[0][0] && player.getY() == tunnel[0][1]){
                player.setLocation(tunnel[1][0], tunnel[1][1]);
            }
            else if(player.getX() == tunnel[1][0] && player.getY() == tunnel[1][1]){
                player.setLocation(tunnel[0][0], tunnel[0][1]);
            }
            for(int x = 0; x < ghs.length; x++){
                if(ghs[x] != null){
                    if(ghs[x].getX() == tunnel[0][0] && ghs[x].getY() == tunnel[0][1])
                        ghs[x].setLocation(tunnel[1][0], tunnel[1][1]);
                    else if(ghs[x].getX() == tunnel[1][0] && ghs[x].getY() == tunnel[1][1])
                        ghs[x].setLocation(tunnel[0][0], tunnel[0][1]);
                }
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
            if(timer == frameRate * 8){
                for(int x = 0; x < 4; x++){
                    if(ghs[x] != null){
                        ghs[x].setBlink();
                    }
                }
            }
            if(timer == frameRate * orbTime){
                orbEaten = false;
                player.setGhostEdible(false);
                timer = 0;
                for(int x = 0; x < 4; x++){
                    if(removedGhs[x] != null){
                        ghs[x] = new Ghost(x+1, player);
                        removedGhs[x] = null;
                        ghs[x].level = level;
                        addObject(ghs[x].getActor(), level.ghosts[x][0], level.ghosts[x][1]);
                    }
                    ghs[x].setImg();
                }
                
            }
        }
    }
    
}
