package Tanks;

import java.util.Collections;
import java.util.Comparator;

public class Final_score_board{
    /**
    * The width of a cell
    */
    private static final int CELLSIZE = 32; 

    /**
    * 1 second in the number of frames
    */
    private static final int one_Sec = 30;

    /**
     * integer representing the timer attribute
     */
    private int timer = 0;

    /**
     * Constructor, sort the tanks by their score in a descending order before showing
     * @param app App to draw
     */
    public Final_score_board(App app){
        Collections.sort(app.tanks_on_field, new Comparator<Tank>() {
            public int compare(Tank a, Tank b){
                if (a.getScore() < b.getScore()){
                    return 1;
                }
                else if (a.getScore() > b.getScore()){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });
    }

    /**
     * Draw the scoreboard, increment the timer attribute to delay showing.
     * @param app App to draw
     */
    public void scoreboard(App app){
        
        this.timer += 1;

        app.stroke(0);
        app.fill(255);

        app.rect((float) 6.75*CELLSIZE, (float) 5*CELLSIZE, (float) 13.5*CELLSIZE, (float) 10*CELLSIZE);

        app.fill(0);

        app.text("Score", (float) 7*CELLSIZE, (float) 6*CELLSIZE);

        app.noFill();

        app.rect((float) 6.75*CELLSIZE, (float) 5*CELLSIZE, (float) 13.5*CELLSIZE, (float) 2.5*CELLSIZE);

        app.fill(0);

        for (int i = 0; i < app.tanks_on_field.size(); i++){
            if (this.timer > (3*one_Sec/4)*i){
                String text = "Player " + app.tanks_on_field.get(i).get_id();
                app.text(text, (float) 7.25*CELLSIZE, (float)(i*2*CELLSIZE + 8.8125*CELLSIZE));
                app.text(app.tanks_on_field.get(i).getScore(), 15*CELLSIZE, (float)(i*2*CELLSIZE + 8.8125*CELLSIZE));
            }
        }
    }
}
