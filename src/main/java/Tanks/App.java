package Tanks;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;


public class App extends PApplet {

    /**
    * The width of a cell
    */
    public static final int CELLSIZE = 32; 

    /**
    * The height of a cell
    */
    public static final int CELLHEIGHT = 32;

    /**
    * The size of the moving average window
    */
    public static final int CELLAVG = 32;

    /**
    * The location of the topbar
    */
    public static final int TOPBAR = 0;

    /**
    * The width of the window in px
    */
    public static int WIDTH = 864;


    /**
    * The height of the window in px
    */
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;


    /**
    * The width of the window in cells
    */
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;


    /**
    * The height of the window in cells
    */
    public static final int BOARD_HEIGHT = 20;


    /**
    * The numer of initial parachutes
    */
    public static final int INITIAL_PARACHUTES = 1;

    /**
    * The number of FPS set
    */
    public static final int FPS = 30;

    /**
    * The default explosion radius
    */
    public static final int explosion_Radius = 15;

    /**
    * The config path string
    */
    public String configPath;

    /**
    * Representation of a level, a hashmap between string name of resources and said resources
    */
    public HashMap<String, String> Level;


    /**
    * Representation of all levels, a hashmap between the level number and said level
    */
    public HashMap<Integer, HashMap<String, String>> Levels_Config = new HashMap<>();


    /**
    * Representation of players' colours, a hashmap between players' id and their colours
    */
    public HashMap<String, String> player_colours = new HashMap<>();


    /**
    * Representation of the terrain's height at each X coordinate
    */
    public float[] X_height = new float[WIDTH];


    /**
    * The location of every tree's X coordinate
    */
    public ArrayList<Integer> Tree_X = new ArrayList<>();

    /**
    * The image of the background
    */
    public PImage backgroundImage;

    /**
    * The image of the tree
    */
    public PImage treeImage;

    /**
    * The image of the parachute resource
    */
    public PImage parachuteImage;

    /**
    * The image of the fuel resource
    */
    public PImage fuel;

    /**
    * The image of the wind to the right
    */
    public PImage wind_right;

    /**
    * The image of the wind to the left
    */
    public PImage wind_left;

    /**
    * The list of bullets currently on screen to draw
    */
    public ArrayList<Projectile> cur_bullet = new ArrayList<>();

    /**
    * The turn number of this match
    */
    public int turn = 0;

    /**
    * Starting wind condition
    */
    public float wind = 0;

    /**
    * The list of tanks currently in the game
    */
    public ArrayList<Tank> tanks_on_field = new ArrayList<>();

    /**
    * The array containing string representation of the foreground color
    */
    public String[] colorStrings;

    /**
    * The list of explosions currently on screen to draw
    */
    public ArrayList<Explosion> explosions = new ArrayList<>();

    /**
    * The current arrow on screen to draw
    */
    public Arrow cur_arrow;

    /**
    * The current level number
    */
    public int level_num = 1;

    /**
    * The timer for getting the next level
    */
    public int timer = 0; 

    /**
    * The final score board to draw at the end of a game
    */
    public Final_score_board final_score_board;

    /**
    * The current tank chosen this turn
    */
    public Tank cur_tank;

    /**
    * 1 second in the number of frames
    */
    static final int one_Sec = 30;

    /**
    * Set config path
    */
    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
    * Load the level config from the JSON file
    * @param configPath string of the config path
    */
    public void loadJSON_Levels_Config(String configPath){
        ConfigReader.loadJSON_Levels_Config(configPath, this);
    }

    /**
    * Load the colours config from the JSON file
    * @param configPath string of the config path
    */
    public void loadJSON_Colours(String configPath){
        ConfigReader.loadJSON_Colours(configPath, this);
    }

    /**
     * Load all resources such as images. Initialise level elements and players or reset players tank
     * @param level HashMap representation of a level.
     */
    public void loadLevel(HashMap<String, String> level){
        this.Tree_X.clear();
        this.cur_bullet.clear();
        this.cur_arrow = null;
        this.explosions.clear();

        this.level_num += 1;
        this.timer = 0;
        this.Level = level;
        this.turn = 0;

        // Load background image
        try{
            backgroundImage = loadImage(this.getClass().getResource(this.Level.get("background")).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            this.treeImage = loadImage(this.getClass().getResource(this.Level.get("trees")).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            
            //Try to only load this once
            if (this.parachuteImage == null){
                this.parachuteImage = loadImage(this.getClass().getResource("parachute.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
                this.fuel = loadImage(this.getClass().getResource("fuel.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
                this.wind_left = loadImage(this.getClass().getResource("wind-1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
                this.wind_right = loadImage(this.getClass().getResource("wind.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            }
        }
        catch (Exception e) {
            // Handle other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
        }

        char[][] board = ConfigReader.readLevel(this.Level);

        Terrain.map_setup(board, this);

        background(backgroundImage);

        this.colorStrings = this.Level.get("foreground-colour").split(",");

        this.cur_tank = this.tanks_on_field.get(turn);
    }

    /**
    * set up a new config path
    * @param config string of config path
    */
    public void setConfigPath(String config){
        this.configPath = config;
    }

    /**
    * set up a new game
    */
	@Override
    public void setup() {

        frameRate(FPS);
        
        // Reset level to level 1
        this.level_num = 1;

        this.tanks_on_field = new ArrayList<>();

        this.final_score_board = null;

        this.wind = 0;

        this.wind += random(-35, 35);

        // Load JSONObject - Levels_Config
        this.loadJSON_Levels_Config(this.configPath);

        // Load colours of each this.players
        this.loadJSON_Colours(this.configPath);

        if (this.Levels_Config.isEmpty()){
            this.dispose();
        }

        // Load information of first level
        HashMap<String, String> first_level = this.Levels_Config.get(1);

        // Load the first level into the game
        loadLevel(first_level);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        int UP = 38;
        int DOWN = 40;
        int LEFT = 37;
        int RIGHT = 39;
        Tank cur_tank = this.tanks_on_field.get(turn % tanks_on_field.size());

        if (cur_tank != null){
            if (keyCode == RIGHT){
                cur_tank.move(2);
                if (!cur_tank.getFalling()){
                    cur_tank.setTank_Y(this.X_height[(int) cur_tank.get_X()]);
                }
            }
    
            else if(keyCode == LEFT){
                cur_tank.move(-2);
                if (!cur_tank.getFalling()){
                    cur_tank.setTank_Y(X_height[(int) cur_tank.get_X()]);
                }
            }
    
            else if(key == ' '){
                // Fire bullet
                cur_tank.fire(this, 0);

                this.wind += random(-5, 5);

                int num_dead = 0;

                do{
                    if (num_dead >= tanks_on_field.size() - 1){
                        this.timer = one_Sec;
                        break;
                    }
                    this.turn += 1;
                    num_dead += 1;
                }
                while(tanks_on_field.get(turn % tanks_on_field.size()).isDead());
                
            }

            else if(key == '1'){
                if (cur_tank.getScore() >= 30){
                    cur_tank.changeScore(-30);
                    // Fire bullet
                    cur_tank.fire(this, 1);

                    this.wind += random(-5, 5);

                    int num_dead = 0;

                    do{
                        if (num_dead >= tanks_on_field.size() - 1){
                            this.timer = 30;
                            break;
                        }
                        this.turn += 1;
                        num_dead += 1;
                    }
                    while(tanks_on_field.get(turn % tanks_on_field.size()).isDead());
                }
            }

            else if(key == '2'){
                if (cur_tank.getScore() >= 30){
                    cur_tank.changeScore(-30);
                    // Fire bullet
                    cur_tank.fire(this, 2);

                    this.wind += random(-5, 5);

                    int num_dead = 0;

                    do{
                        if (num_dead >= tanks_on_field.size() - 1){
                            this.timer = one_Sec;
                            break;
                        }
                        this.turn += 1;
                        num_dead += 1;
                    }
                    while(tanks_on_field.get(turn % tanks_on_field.size()).isDead());
                }
            }
    
            else if(keyCode == UP){
                cur_tank.rotate(3*180/one_Sec);
            }
    
            else if(keyCode == DOWN){
                cur_tank.rotate(-3*180/one_Sec);
            }

            else if(key == 'w'){
                cur_tank.setPower(true);
            }
    
            else if(key == 's'){
                cur_tank.setPower(false);
            }

            else if(key == 'r' && this.level_num <= this.Levels_Config.size()){
                if (cur_tank.getScore() >= 20){
                    cur_tank.changeScore(-20);
                    cur_tank.repair(20);
                }
            }

            else if(key == 'f'){
                if (cur_tank.getScore() >= 10){
                    cur_tank.changeScore(-10);
                    cur_tank.refuel(200);
                }
            }

            else if(key == 'p'){
                if (cur_tank.getScore() >= 15){
                    cur_tank.changeScore(-15);
                    cur_tank.newParachute(1);
                }
            }

            else if(key == 'h'){
                if (cur_tank.getScore() >= 20){
                    cur_tank.changeScore(-20);
                    cur_tank.set_shield();
                }
            }

            else if(key == 'r' && this.level_num > this.Levels_Config.size()){
                this.setup();
            }
        }
    }



    /**
     * Draw the final scoreboard when needed.
     */
    public void final_Scoreboard(){
        if (this.final_score_board == null){
            this.final_score_board = new Final_score_board(this);
        }
        this.final_score_board.scoreboard(this);
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        // System.out.println(frameRate);

        //----------------------------------
        //display Enviroment:
        //----------------------------------
        
        // Ground
        background(backgroundImage);
        stroke(parseInt(this.colorStrings[0]), parseInt(this.colorStrings[1]), parseInt(this.colorStrings[2]));
        for (int i = 0; i < this.X_height.length; i++){
            rect(i, (float)this.X_height[i], 1, HEIGHT);
        }

        //----------------------------------
        //display Trees:
        //----------------------------------
        
        // Tree
        for (int i = 0; i < this.Tree_X.size(); i++){
            image(this.treeImage, (float)this.Tree_X.get(i) - CELLSIZE/2, (float)this.X_height[this.Tree_X.get(i)] - CELLSIZE, CELLSIZE, CELLSIZE);
        }

        //----------------------------------
        //display Tanks:
        //----------------------------------
        for (int i = 0; i < this.tanks_on_field.size(); i++){
            // Get each tank
            Tank tank = this.tanks_on_field.get(i);

            if (tank.shouldDie()){
                tank.kill(this, explosion_Radius);
            }

            // Processing tank falling
            if(tank.get_Y() < this.X_height[(int) tank.get_X()]){ // Continue falling
                tank.shouldFall(true);
                tank.shouldParachute(true);
            }
            else{ // Stop falling
                if (tank.getParachuting()){
                    tank.discardParachute(); 
                    tank.shouldParachute(false);
                }
                tank.shouldFall(false);
                tank.damageTank(-(this.X_height[(int) tank.get_X()] - tank.get_Y())); //fall damage accounted for terrain diff
                tank.setTank_Y(this.X_height[(int) tank.get_X()]);
            }

            // I let it fall
            if(tank.getFalling()){
                tank.fall();
            }

            if(tank.isDead()){
                continue;
            }
            
            tank.draw(this);
        }

        //----------------------------------
        //display HUD:
        //----------------------------------

        HUD.hud(this, tanks_on_field.get(turn % tanks_on_field.size()));

        //----------------------------------
        //display scoreboard:
        //----------------------------------

        HUD.scoreboard(this);

		//----------------------------------
        //check player action and animation
        //----------------------------------

        // Bullets
        for (int i = 0; i < this.cur_bullet.size(); i++){
            Projectile bullet = this.cur_bullet.get(i);
            if (bullet.get_X() >= WIDTH || bullet.get_X() <= 0){
                this.cur_bullet.remove(i);
                bullet = null;
            }
            else{
            bullet.draw(this);
            }
        }

        // Explosions
        for (int i = 0; i < this.explosions.size(); i++){
            Explosion explosion = this.explosions.get(i);
            explosion.draw(this);
            if (explosion.frame >= (int)(one_Sec/4)){
                explosion.explode(this.X_height);
                this.explosions.remove(i);
                explosion = null;
            }
        } 

        // Check win
        int num_alive = 0;

        for (Tank tank : this.tanks_on_field){
            if(!tank.isDead()){
                num_alive += 1;
            }
        }

        boolean win = num_alive <= 1;

        if (win){
            if (this.level_num <= this.Levels_Config.size()){
                this.timer += 1;
                if (this.timer > one_Sec){
                    loadLevel(this.Levels_Config.get(this.level_num));
                }
            }
            else{
                final_Scoreboard();
            }
        }
        else{
            if(this.cur_tank.isDead()){
                turn += 1;
            }
    
            if(this.cur_tank != tanks_on_field.get(turn % tanks_on_field.size())){
                this.cur_tank = tanks_on_field.get(turn % tanks_on_field.size());
                this.cur_arrow = null;
            }
    
            if (this.cur_arrow == null){
                this.cur_arrow = new Arrow(this.cur_tank);
            }
    
            //Arrow
            if (this.cur_arrow != null){
                this.cur_arrow.draw(this);
            }
        }

        return;
    }

    /**
     * Main method. Executes the program.
     * @param args Command line args
     */
    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}
