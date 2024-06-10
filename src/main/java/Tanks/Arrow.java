package Tanks;

public class Arrow extends Animated_sprite{
    /**
    * The width of a cell
    */
    private static final int CELLSIZE = 32;

    /**
     * The tank associated with this arrow.
     */
    private Tank tank;

    /**
     * Constructor for an arrow, requires the associated tank.
     * @param tank, the tank the arrow is associated with.
     */
    public Arrow(Tank tank){
        super(tank.get_X(), tank.get_Y() - 3*CELLSIZE);
        this.tank = tank;
    }

    /**
     * Getter method for retrieving the associated tank object.
     * @return Tank tank
     */
    public Tank getTank(){
        return this.tank;
    }

    /**
     * Dictates the next frame of drawing the arrow, updating its
     * coordinates and incrementing the frame counter by 1.
     */
    public void nextFrame(){
        this.x = tank.get_X();
        this.y = tank.get_Y() - 3*CELLSIZE;
        this.frame++;
    }
    
    /**
     * Draw the arrow on screen, handles the next frame.
     */
    public void draw(App a){
        a.fill(0);
        a.stroke(0);
        if (this.frame < 3*one_Sec/4){
            a.line(this.x, this.y, this.x, (float)(this.y + (1.5)*CELLSIZE));
            a.line(this.x, (float)(this.y + (1.5)*CELLSIZE), this.x - CELLSIZE/2, (float)(this.y + CELLSIZE));
            a.line(this.x, (float)(this.y + (1.5)*CELLSIZE), this.x + CELLSIZE/2, (float)(this.y + CELLSIZE));
            this.nextFrame();
        }
    }
}
