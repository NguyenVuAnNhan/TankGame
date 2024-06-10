package Tanks;

public class HUD{

    /**
     * Helper method for writing the turn text
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void turn_text(App app, Tank cur_tank){
        // turn text
        String turn_text = "Player " + cur_tank.get_id() + "'s turn";
        app.fill(0);
        app.textSize(18);
        app.text(turn_text, 16, 25);        
    }

    /**
     * Helper method for writing the fuel text
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void fuel_text(App app, Tank cur_tank){
        // fuel text
        app.image(app.fuel, 160, 0, 32, 32);
        app.text(String.format("%.0f", (float) cur_tank.getFuel()), 200, 25);    
    }

    /**
     * Helper method for writing the health text
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void health_text(App app, Tank cur_tank){
        // health text
        app.text("Health:", 384, 25);
        app.text(String.format("%.0f", cur_tank.getHP()), 624, 25);
        app.stroke(0);
        app.noFill();
        app.rect(448, 7, 160, 25);
        app.fill(cur_tank.getColours()[0], cur_tank.getColours()[1], cur_tank.getColours()[2]);
        app.rect(448, 7, (float) 160/100*(cur_tank.getHP()), 25);      
    }

    /**
     * Helper method for writing the power text
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void power_text(App app, Tank cur_tank){
        // power text
        app.fill(0);
        String power_text = "Power: " + String.format("%.0f", cur_tank.getPower());
        app.text(power_text, 384, 57);
        app.stroke(128);
        app.noFill();
        app.rect(448, 7, (float) 160/100*(cur_tank.getPower()), 25);
        app.stroke(255, 0, 0);
        app.strokeWeight(1);
        app.line(448 + (float) 160/100*(cur_tank.getPower()), 2, 448 + (float) 160/100*(cur_tank.getPower()), 38);
        app.strokeWeight(4);   
    }

    /**
     * Helper method for writing the wind text
     * @param app App to draw in.
     */
    public static void wind_text(App app){
        // wind text
        app.text(String.format("%.0f", Math.abs(app.wind)), 800, 32);
        if (app.wind > 0){
            app.image(app.wind_right, 752, 0, 48, 48);
        }
        else{
            app.image(app.wind_left, 752,0, 48, 48);
        }    
    }

    /**
     * Helper method for writing the parachute text
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void displayParachute(App app, Tank cur_tank) {
        app.image(app.parachuteImage, 160, 35, 32, 32);
        app.text(String.format("%.0f", (float) cur_tank.num_Para()), 200, 50);
    }

    /**
     * Method for drawing the HUD
     * @param app App to draw in.
     * @param cur_tank Tank to get info.
     */
    public static void hud(App app, Tank cur_tank){
        turn_text(app, cur_tank);

        fuel_text(app, cur_tank);

        health_text(app, cur_tank);

        power_text(app, cur_tank);

        displayParachute(app, cur_tank);

        wind_text(app);
    }
    
    /**
     * Method for drawing the scoreboard
     * @param app App to draw in.
     */
    public static void scoreboard(App app){
        app.stroke(0);
        app.noFill();
        app.rect(696, 48, 160, 24);
        app.rect(696, 48, 160, 32 + 16*app.tanks_on_field.size());
        app.fill(0);
        app.text("Score", 700, 68);
        for (int i = 0; i < app.tanks_on_field.size(); i++){
            int[] colours = app.tanks_on_field.get(i).getColours();
            app.fill(colours[0], colours[1], colours[2]);
            String text = "Player " + app.tanks_on_field.get(i).get_id();
            app.text(text, 700, i*16 + 92);
            app.fill(0);
            app.text(app.tanks_on_field.get(i).getScore(), 824, i*16 + 92);
        }
    }
}
