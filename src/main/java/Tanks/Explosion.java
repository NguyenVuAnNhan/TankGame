package Tanks;

public class Explosion extends Animated_sprite{
    /**
     * integer representing the radius of the explosion
     */
    private int r;

    /**
     * integer representing the damage at the center of the explosion
     */
    static final int damage = 60;

    /**
     * Tank object associated with the explosion for scoring purposes.
     */
    private Tank whose;

    /**
     * Constructor for an explosion, requires the xy coordinates, the blast radius
     * and the associated Tank object.
     * @param x float X_coord.
     * @param y float Y_coord.
     * @param r int blast radius. 
     * @param tank Tank object.
     */
    public Explosion(float x, float y, int r, Tank tank){
        super(x, y);
        this.r = r;
        this.whose = tank;
    }

    /**
     * Next frame method, increment the frame counter attribute
     */
    public void nextFrame(){
        this.frame++;
    }

    /**
     * Calculate the amount of terrain needs to be destroyed.
     * @param X_height float array representing terrain height at each x coordinate.
     */
    public void explode(float[] X_height){
        int x = (int) this.x;
        float y = this.y;
        int r = this.r;

        for (int i = 0; i < r*2 + 1 ; i++){
            int x_cur = x - r + i;
            if (x_cur >= 864 || x_cur < 0){
                continue;
            }
            double e_low = y + Math.sqrt(r*r - (x - x_cur)*(x - x_cur));
            double e_high = y - Math.sqrt(r*r - (x - x_cur)*(x - x_cur));
            
            double t_high = X_height[x_cur];

            if (e_low < t_high){
                continue;
            }
            else if (e_high <= t_high){
                double dest = (e_low - t_high);
                X_height[x_cur] = (float)(X_height[x_cur] + dest);
            }
            else{
                double dest = (e_low - e_high);
                X_height[x_cur] = (float)(X_height[x_cur] + dest);
            }
        }
    }
    
    /**
     * Draw the explosion, calculate and distribute the damage done in the first frame.
     * @param a App to draw.
     */
    public void draw(App a){
        if (this.frame == 0){
            for (Tank tank : a.tanks_on_field){
                double dist = Math.sqrt((tank.get_X() - this.x)*(tank.get_X() - this.x) + (tank.get_Y() - this.y)*(tank.get_Y() - this.y));

                if (dist < r){
                    tank.get_hit_by(whose);
                    tank.damageTank((float) ((r - dist)/r)*damage);
                    tank.get_hit_by(whose);
                }

                if (Math.abs(tank.get_X() - this.x) < r){
                    tank.get_hit_by(whose);
                }
            }
        }
        
        a.noStroke();
        a.fill(255,0,0);
        a.ellipse(this.x, this.y, 2*this.r*this.frame/6, 2*this.r*this.frame/6);
        a.fill(255,255,0);
        a.ellipse(this.x, this.y, this.r*this.frame/6, this.r*this.frame/6);
        a.fill(255,255,255);
        a.ellipse(this.x, this.y, this.r*this.frame/12, this.r*this.frame/12);
        this.nextFrame();
    }
}
