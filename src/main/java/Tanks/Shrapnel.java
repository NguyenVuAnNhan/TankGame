package Tanks;

public class Shrapnel extends Projectile{
    /**
     * Constructor, requires the xy coordinates as well as its velocities
     * in the Ox and Oy direction and the tank which shot it.
     * Calls the projectile superconstructor
     * @param x float coord
     * @param y float coord
     * @param vx double Ox-velocity
     * @param vy double Oy-velocity 
     * @param tank Tank
     */
    public Shrapnel(float x, float y, double vx, double vy, Tank tank){
        super(x, y, vx, vy, tank);
    }

    /**
     * Override draw function of projectiles, explodes in a smaller radius
     */
    @Override
    public void draw(App app){
        int[] colour = this.whose.getColours();
        app.fill(0);
        app.stroke(colour[0], colour[1], colour[2]);
        app.ellipse((float)this.x, (float)this.y, 5, 5);
        this.move();
        this.accelerate(app.wind);
        if (this.collides(app.X_height)){
            Explosion explosion = new Explosion((int)this.get_X(), (float)this.get_Y(), explosion_Radius, this.sender());
            app.explosions.add(explosion);
            app.cur_bullet.remove(this);
        }
    }
}
