package Tanks;

public class Big_projectile extends Projectile{
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
    public Big_projectile(float x, float y, double vx, double vy, Tank tank){
        super(x, y, vx, vy, tank);
    }

    /**
     * Override draw function of projectiles, explodes in a larger radius and create 5 shrapnels upwards
     */
    @Override
    public void draw(App app){
        int[] colour = this.whose.getColours();
        app.fill(0);
        app.stroke(colour[0], colour[1], colour[2]);
        app.ellipse((float)this.x, (float)this.y, 15, 15);
        this.move();
        this.accelerate(app.wind);
        if (this.collides(app.X_height)){
            Explosion explosion = new Explosion((int)this.get_X(), (float)this.get_Y(), 3*explosion_Radius, this.sender());
            app.explosions.add(explosion);
            app.cur_bullet.remove(this);
            for (int i = -2; i < 3; i++){
                Projectile shrapnel = new Shrapnel(this.get_X(), this.get_Y(), i*1, -6, whose);
                app.cur_bullet.add(shrapnel);
            }
        }
    }
}