package Tanks;
import java.util.*;

public class Tank extends Game_sprite{
    /**
     * float representing the tank's current health.
     */
    private float hp = 100;

    /**
     * float representing the tank's current power.
     */
    private float power = 50;

    /**
     * float representing the tank's current number of parachutes.
     */
    private int parachutes = 3;

    /**
     * integer representing the tank's current fuel.
     */
    private int fuel = 250;

    /**
     * integer representing the tank's current score.
     */
    private int score = 0;

    /**
     * boolean representing whether the tank has exploded yet.
     */
    private boolean exploded;

    /**
     * tank object representing the tank that hits this tank.
     */
    private Tank hit_by;

    /**
     * boolean representing whether the tank is shielded or not.
     */
    private boolean shield;

    /**
     * string representing the tank's id.
     */
    private String id;

    /**
     * int array representing the tank's colour code in RGB.
     */
    private int[] colour;

    /**
     * float representing the current angle of the turret.
     */
    private float angle = 90;

    /**
     * boolean representing whether the tank is falling.
     */
    public boolean isFalling;

    /**
     * boolean representing whether the tank is parachuting.
     */
    private boolean parachuting;

    /**
     * Constructor, requiring the tank's id, its x coordinate and the string representing its colour.
     * @param id of tank, 
     * @param x coordinate.
     * @param colour the tank's colour as a string
     */
    public Tank(String id, int x, String colour){
        super(x, 0);
        this.id = id;
        if (!colour.equals("random")){
            String[] colours_string = colour.split(",");
            int[] colours = {Integer.parseInt(colours_string[0]), Integer.parseInt(colours_string[1]), Integer.parseInt(colours_string[2])};
            this.colour = colours;
        }
        else{
            //Randomise colour here
            Random random = new Random();
            int[] colours = {random.nextInt(255), random.nextInt(255), random.nextInt(255)};
            this.colour = colours;
        }
    }

    /**
     * Getter method returning the tank's score
     * @return this tank's score
     */
    public int getScore(){
        return this.score;
    }

    /**
     * Method for adding new parachutes
     * @param parachute int representing the number of parachutes to add
     */
    public void newParachute(int parachute){
        this.parachutes += parachute;
    }

    /**
     * Method for adding new fuel
     * @param fuel int representing the amount of fuel to add
     */
    public void refuel(int fuel){
        this.fuel += fuel;
    }

    /**
     * Method for repairing, not allowing health overflow
     * @param hp int representing the amount of health to add
     */
    public void repair(int hp){
        this.hp += hp;
        if (this.hp > 100){
            this.hp = 100;
        }
    }

    /**
     * Method for adding and minusing score
     * @param change int representing the amount of score to add or minus, dictated by sign
     */
    public void changeScore(int change){
        this.score += change;
    }

    /**
     * Method for reseting tank when loadlevel is called.
     * All attributes but score are resetted to their initial values.
     */
    public void reset(){
        this.hp = 100;
        this.power = 50;
        this.fuel = 250;
        this.exploded = false;
        this.hit_by = null;
        this.angle = 90;
        this.isFalling = false;
        this.parachuting = false;
    }

    /**
     * Getter method for getting the tank's id
     * @return String the tank's id.
     */
    public String get_id(){
        return this.id;
    }

    /**
     * Setter method for shield
     */
    public void set_shield(){
        this.shield = true;
    }

    /**
     * Getter method for whether the tank is falling or not.
     * @return boolean whether the tank is falling or not.
     */
    public boolean getFalling(){
        return this.isFalling;
    }

    /**
     * Getter method for whether the tank is parachuting or not.
     * @return boolean whether the tank is parachuting or not.
     */
    public boolean getParachuting(){
        return this.parachuting;
    }

    /**
     * Setter method determining whether the tank should parachute or not.
     * The tank can only parachute when it should and it has atleast 1 parachute.
     * @param answer boolean of whether to consider parachuting
     */
    public void shouldParachute(boolean answer){
        if (answer == true){
            if (this.parachutes > 0){
                this.parachuting = answer; 
            }
        }
        else{
            this.parachuting = answer;
        }
    }

    /**
     * Setter method determining whether the tank should fall or not.
     * @param answer boolean for whether to fall
     */
    public void shouldFall(boolean answer){
        this.isFalling = answer;
    }

    /**
     * Method for discarding parachutes.
     */
    public void discardParachute(){
        this.parachutes -= 1;
    }

    /**
     * Getter method returning the tank that hits this tank.
     * @return the tank that hits this tank, null if not hit yet
     */
    public Tank hit_by_who(){
        return this.hit_by;
    }

    /**
     * Setter method for the tank that hits this tank.
     * @param tank the tank that hits this tank
     */
    public void get_hit_by(Tank tank){
        this.hit_by = tank;
    }

    /**
     * Getter method returning the tank's fuel
     * @return int representing this tank's fuel
     */
    public int getFuel(){
        return this.fuel;
    }

    /**
     * Getter method returning the tank's colour
     * @return int array representing this tank's colour in RGB code
     */
    public int[] getColours(){
        return this.colour;
    }

    /**
     * Getter method returning the tank's exploded status
     * @return boolean representing this tank's exploded status
     */
    public boolean isDead(){
        return this.exploded;
    }

    /**
     * Getter method returning the tank's shield status
     * @return boolean representing this tank's shield status
     */
    public boolean isShielded(){
        return this.shield;
    }

    /**
     * Setter method for the tank's exploded status, spawning an
     * explosion with the specified blast radius.
     * @param app App to draw in, 
     * @param r integer for blast radius.
     */
    public void kill(App app, int r){
        this.exploded = true;
        app.explosions.add(new Explosion(this.x, this.y, r, this));
    }

    /**
     * Method for determining whether a tank should explode.
     * It check whether the tank has exploded yet as 
     * You can't kill what is already dead.
     * @return boolean representing whether this tank should explode
     */
    public boolean shouldDie(){
        return !this.exploded && this.hp <= 0;
    }

    /**
     * Setter method for the tank's power
     * @param sign boolean for whether to increase or decrease
     */
    public void setPower(boolean sign){
        if (sign){
            this.power += 3.6;

            if (this.power > this.hp){
                this.power = this.hp;
            }
        }
        else{
            this.power -= 3.6;

            if (this.power < 0){
                this.power = 0;
            }

        }
    }

    /**
     * Method for damaging the tank through explosion.
     * If the tank has shield, remove shield and take no damage
     * Else, add score to the shooter and take the damage.
     * @param damage float representing the damage taken.
     */
    public void damageTank(float damage){
        if (shield != true){

            if (this.hit_by != null && this.hit_by != this && !(this.hp <= 0)){
                this.hit_by.changeScore((int)(Math.min(damage, this.hp)));
            }

            this.hp -= damage;
            if (this.hp <= 0){
                this.hp = 0;
            }

            if (this.power > this.hp){
                this.power = this.hp;
            }
        }
        else{
            if (damage > 0){
                this.shield = false;
            }
        }
        
    }

    /**
     * Method for a tank to fall
     */
    public void fall(){
        if (parachuting){
            this.y += 2;
        }
        else{
            this.y += 4;
            this.hp -= 4;
            if(this.hit_by != null && this.hit_by != this && !(this.hp <= 0)){
                this.hit_by.changeScore(4);
            }
            if (this.hp <= 0){
                this.hp = 0;
            }
        }
    }

    /**
     * Setter method for the tank's X coordinate
     * @param x int tank's coordinate
     */
    public void setTank_X(int x){
        this.x = x;
    }

    /**
     * Setter method for the tank's Y coordinate
     * @param y float tank's coordinate
     */
    public void setTank_Y(float y){
        this.y = y;
    }

    /**
     * Method for the tank to move.
     * The tank should move while it still has fuel and
     * move by exactly the amount of fuel left when
     * it empties its tank.
     * @param speed int tank's speed
     */
    public void move(int speed){
        if (this.x + speed > 0 && this.x + speed < 864 && this.fuel != 0){
            if (this.fuel > speed){
                this.x += speed;
                this.fuel -= Math.abs(speed);
            }
            else{
                this.x += this.fuel;
                this.fuel = 0;
            }
        }
    }

    /**
     * Method for the tank to rotate.
     * @param angle float the angle the tank should rotate by
     */
    public void rotate(float angle){
        if (this.angle >= 0 && this.angle <= 180){
            this.angle += angle/3;
        }
        
        if (this.angle < 0){
            this.angle = 0;
        }

        if (this.angle > 180){
            this.angle = 180;
        }

    }

    /**
     * Getter method for the tank's health
     * @return float representing the tank's current health.
     */
    public float getHP(){
        return this.hp;
    }

    /**
     * Getter method for the tank's power
     * @return float representing the tank's current power.
     */
    public float getPower(){
        return this.power;
    }

    /**
     * Getter method for the tank's angle
     * @return float representing the tank's current angle.
     */
    public float getAngle(){
        return this.angle;
    }

    /**
     * Getter method for the tank's parachutes
     * @return int representing the tank's current parachutes.
     */
    public int num_Para(){
        return this.parachutes;
    }

    /**
     * Method for drawing the tank.
     * Included drawing the shield if there is one.
     * @param app App to draw in.
     */
    public void draw(App app){
        if (this.y > 640){
            this.kill(app, 30);
        }

        float turret_X = this.x + (float) Math.cos((this.angle/180)*Math.PI)*(15);
        float turret_Y = this.y - (float) Math.sin((this.angle/180)*Math.PI)*(15);
        app.stroke(0);
        app.strokeWeight(5);
        app.line(this.x, this.y, turret_X, turret_Y);
        app.strokeWeight(2);
        app.stroke(colour[0], colour[1], colour[2]);
        app.fill(colour[0], colour[1], colour[2]);
        app.rect(this.x - 10, this.y, 20, 4);
        app.rect(this.x - 8, this.y - 4, 16, 4);
        if(this.parachuting){
            app.image(app.parachuteImage, this.x - 32, this.y - 64);
        }
        if(this.shield){
            app.noFill();
            app.stroke(0, 0, 255);
            app.ellipse(this.x, this.y, 48, 48);
        }
    }

    /**
     * Method for firing the tank's cannon. The projectile
     * should be spawned from the turret with the appropriate
     * velocity set by the power and angle.
     * @param app to draw in.
     * @param i int representing type of bullet
     */
    public void fire(App app, int i){
        float turret_X = this.x + (float) Math.cos((this.angle/180)*Math.PI)*(15);
        float turret_Y = this.y - (float) Math.sin((this.angle/180)*Math.PI)*(15);
        if (i == 0){
            Projectile b = new Projectile(turret_X, turret_Y, 1 + 8*(power/100)*Math.cos((this.angle/180)*Math.PI) , -(1 + 8*(power/100)*Math.sin((this.angle/180)*Math.PI)), this);
            app.cur_bullet.add(b);
        }
        else if(i == 1){
            Projectile b = new Big_projectile(turret_X, turret_Y, 1 + 8*(power/100)*Math.cos((this.angle/180)*Math.PI) , -(1 + 8*(power/100)*Math.sin((this.angle/180)*Math.PI)), this);
            app.cur_bullet.add(b);
        }
        else if(i == 2){
            for(int num = 0; num < 5; num++){
                Projectile b = new Shrapnel((float)(turret_X + 5*num*Math.sin((this.angle/180)*Math.PI)), (float)(turret_Y + 5*num*Math.cos((this.angle/180)*Math.PI)), 1.3*(1 + 8*(power/100)*Math.cos((this.angle/180)*Math.PI)) , 1.3*(-(1 + 8*(power/100)*Math.sin((this.angle/180)*Math.PI))), this);
                app.cur_bullet.add(b);
            }
        }
    }
}