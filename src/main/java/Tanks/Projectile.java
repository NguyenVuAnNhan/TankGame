package Tanks;
public class Projectile extends Game_sprite{
    /**
    * The default explosion radius
    */
    protected static final int explosion_Radius = 15;

    /**
     * Tank object representing the tank this projectile is associated with.
     */
    protected Tank whose;

    /**
     * double representing the velocity in the Ox direction of the projectile.
     */
    protected double x_Velocity;

    /**
     * double representing the velocity in the Oy direction of the projectile.
     */
    protected double y_Velocity;

    /**
     * Constructor, requires the xy coordinates as well as its velocities
     * in the Ox and Oy direction and the tank which shot it.
     * @param x float coord
     * @param y float coord
     * @param vx double Ox-velocity
     * @param vy double Oy-velocity 
     * @param tank Tank
     */
    public Projectile(float x, float y, double vx, double vy, Tank tank){
        super(x, y);
        this.x_Velocity = vx;
        this.y_Velocity = vy;
        this.whose = tank;
    }

    /**
     * Getter method returning the tank which shot this projectile.
     * @return the tank that fired this bullet
     */
    public Tank sender(){
        return this.whose;
    }

    /**
     * Move the bullet according to its current velocities in both directions.
     */
    public void move(){
        this.x += x_Velocity;
        this.y += y_Velocity;
    }

    /**
     * Change the bullet's velocity according to its current acceleration in both directions.
     * @param wind a double representing wind speed.
     */
    public void accelerate(double wind){
        this.y_Velocity += 3.6/30;
        this.x_Velocity += (wind*0.03)/30;
    }

    /**
     * Figure out whether the bullet has collided or not.
     * @param terrain_height float array representing the terrain height.
     * @return boolean if the bullet is colliding or not.
     */
    public boolean collides(float[] terrain_height){
        if (this.x < 0 || this.x > 863){
            return false;
        }
        if (terrain_height[(int)this.x] <= this.y){
            return true;
        }
        return false;
    }

    /**
     * Draw the bullet.
     * @param app App to draw the bullet.
     */
    public void draw(App app){
        int[] colour = this.whose.getColours();
        app.fill(0);
        app.stroke(colour[0], colour[1], colour[2]);
        app.ellipse((float)this.x, (float)this.y, 10, 10);
        this.move();
        this.accelerate(app.wind);
        if (this.collides(app.X_height)){
            Explosion explosion = new Explosion((int)this.get_X(), (float)this.get_Y(), 2*explosion_Radius, this.sender());
            app.explosions.add(explosion);
            app.cur_bullet.remove(this);
        }
    }

}
