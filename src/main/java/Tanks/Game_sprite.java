package Tanks;

public abstract class Game_sprite {
    /**
     * float representing the x coordinate
     */
    protected float x;

    /**
     * float representing the y coordinate
     */
    protected float y;

    /**
     * Constrictor, requires the x and y coordinates.
     * @param x coordinates, float
     * @param y coordinates, float
     */
    public Game_sprite(float x, float y){
        this.x = x;
        this.y = y;
    };

    /**
     * Getter method, returning the x coordinate
     * @return x coordinate, float
     */
    public float get_X(){
        return this.x;
    };

    /**
     * Getter method, returning the y coordinate
     * @return y coordinate, float
     */
    public float get_Y(){
        return this.y;
    };

    /**
     * Draw method
     * @param app to draw in.
     */
    public abstract void draw(App app);

}
