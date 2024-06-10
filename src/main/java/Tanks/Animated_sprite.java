package Tanks;

public abstract class Animated_sprite extends Game_sprite {
    /**
    * 1 second in the number of frames
    */
    protected static final int one_Sec = 30;
    
    /**
     * Represents the current frame the animation is at.
     */
    protected int frame = 0;

    /**
     * Constructor of an animated sprite, call the constructor of a game sprite. Requires the x and y coordinates.
     * @param x float coordinates
     * @param y float coordinates
     */
    public Animated_sprite(float x, float y){
        super(x, y);
    }

    /**
     * Dictates what changes should be made for the next frame
     */
    public abstract void nextFrame();
}
