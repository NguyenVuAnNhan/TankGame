package Tanks;

import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SampleTest {

    App app;

    /*
     * Settings
     */

    @BeforeEach
    public void start(){
        app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.delay(2000);
    }

    @AfterEach
    public void end(){
        app.noLoop();
        app.dispose();
    }

    /*
     * Setup tests
     */

    @Test
    public void setup_successful() {
        assertNotEquals(app, null);

        // 3 levels
        assertTrue(app.Levels_Config.size() == 3);

        // Extract 19 colours
        assertTrue(app.player_colours.size() == 19);

        // next level is 2
        assertTrue(app.level_num == 2);

        // number of tanks is 4
        assertTrue(app.tanks_on_field.size() == 4);

        // start turn is 0
        assertTrue(app.turn == 0);

        // wind is < 35 and > -35
        assertTrue(app.wind >= -35 && app.wind <= 35);

        // final score board is null
        assertTrue(app.final_score_board == null);
    }

    @Test
    public void setup_unsuccessful() {
        assertNotEquals(app, null);
        try{
            app.setConfigPath("nothing");
            app.setup();
            //This should throw an error
            assertTrue(false);
        }
        catch(Error e){
            assertTrue(true);
        }
    }

    @Test
    public void Terrain_setup() {
        assertNotEquals(app, null);
        for(float x : app.X_height){
            //Check height is made accurately
            assertTrue(x >= 0 && x <= 680);
        }
        //Check length is correct
        assertTrue(app.X_height.length == 896);
    }

    @Test
    public void Tree_setup() {
        assertNotEquals(app, null);
        //Check number of trees is 9
        assertTrue(app.Tree_X.size() == 9);
        ArrayList<Integer> True_tree_X = new ArrayList<>();     

        True_tree_X.add(3*32);
        True_tree_X.add(4*32);
        True_tree_X.add(6*32);
        True_tree_X.add(8*32);
        True_tree_X.add(9*32);
        True_tree_X.add(15*32);
        True_tree_X.add(16*32);
        True_tree_X.add(19*32);
        True_tree_X.add(23*32);

        for (int i = 0; i < app.Tree_X.size(); i++){
            int diff = app.Tree_X.get(i) - True_tree_X.get(i);
            //Check position of each tree
            assertTrue(diff > 0 && diff <= 32);
        }
    }

    @Test
    public void Tank_setup() {
        assertNotEquals(app, null);
        Tank test_Tank = new Tank("A", 80, "0,0,255");
        Tank actual_Tank = app.tanks_on_field.get(0);
        //Correct ID
        assertTrue(actual_Tank.get_id().equals(test_Tank.get_id()));
        //Correct X coords
        assertTrue(actual_Tank.get_X() == test_Tank.get_X());
        for (int i = 0; i < actual_Tank.getColours().length; i++){
            //Correct colour component
            assertTrue(actual_Tank.getColours()[i] == test_Tank.getColours()[i]);
        }
        //Correct Y coords
        assertTrue(actual_Tank.get_Y() == app.X_height[(int) test_Tank.get_X()]);
    }

    @Test
    public void Tank_setup_random() {
        assertNotEquals(app, null);
        Tank test_Tank = new Tank("A", 80, "random");
        Tank test_Tank_2 = new Tank("A", 80, "random");
        for(int i = 0; i < 3; i++){
            //Valid colour code
            assertTrue(test_Tank.getColours()[i] < 256 && test_Tank.getColours()[i] >= 0);
        }
        for(int i = 0; i < 3; i++){
            //Valid colour code
            assertTrue(test_Tank_2.getColours()[i] < 256 && test_Tank_2.getColours()[i] > 0);
        }

        int unlucky = 0;

        for(int i = 0; i < 3; i++){
            if (test_Tank_2.getColours()[i] == test_Tank.getColours()[i]){
                unlucky++;
            }
        }
        if (unlucky == 3){
            System.out.println("these two random tanks have the same colour code, that's like a (1/256)^3 probability. I must be the unluckiest person alive yo.");
        }
        //Colours are randomised and therefore different
        assertTrue(unlucky < 3);
    }

    /*
     * Explosion test
     */
    @Test
    public void Basic_explosion() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Explosion test_Explosion = new Explosion(0, 0, 30, shooter_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);

        //There's an explosion in the list of current explosions
        assertFalse(app.explosions.isEmpty());

        app.loop();
        app.delay(1000);

        //The explosion has ended
        assertTrue(app.explosions.isEmpty());
    }


    @Test
    public void Tank_damage_score() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Tank target_tank = app.tanks_on_field.get(1);

        Explosion test_Explosion = new Explosion(target_tank.get_X(), target_tank.get_Y(), 30, shooter_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);
        app.loop();
        app.delay(1000);
        //Damage is 60
        assertTrue(target_tank.getHP() == 40);
        //Score allocated correctly
        assertTrue(shooter_tank.getScore() == 60);
    }

    @Test
    public void Tank_damage_distance_score() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Tank target_tank = app.tanks_on_field.get(1);

        Explosion test_Explosion = new Explosion(target_tank.get_X() + 15, target_tank.get_Y(), 30, shooter_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);
        app.loop();
        app.delay(1000);
        // Float arithmetic brings slight errors
        //Damage is half of 60, hence health should be 70
        float hp_diff = Math.abs(target_tank.getHP() - 70);
        //Score should be 30
        float score_diff = Math.abs(shooter_tank.getScore() - 30);

        assertTrue(hp_diff < 1);
        assertTrue(score_diff < 1);
    }

    @Test
    public void Terrain_damage() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Tank target_tank = app.tanks_on_field.get(1);

        int x = (int) target_tank.get_X();
        float y = target_tank.get_Y();
        int r = 30;

        float[] crater = new float[r*2 + 1];

        for (int i = 0; i < r*2 + 1 ; i++){
            int x_cur = x - r + i;
            if (x_cur >= 864 || x_cur < 0){
                continue;
            }
            double e_low = y + Math.sqrt(r*r - (x - x_cur)*(x - x_cur));
            double e_high = y - Math.sqrt(r*r - (x - x_cur)*(x - x_cur));
            
            double t_high = app.X_height[x_cur];

            if (e_low < t_high){
                continue;
            }
            else if (e_high <= t_high){
                double dest = (e_low - t_high);
                crater[i] = (float)(app.X_height[x_cur] + dest);
            }
            else{
                double dest = (e_low - e_high);
                crater[i] = (float)(app.X_height[x_cur] + dest);
            }
        }

        Explosion test_Explosion = new Explosion(target_tank.get_X(), target_tank.get_Y(), 30, shooter_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);
        app.loop();
        app.delay(1000);

        for (int i = 0; i < crater.length; i++){
            //Check height similarity
            float Terrain_diff = Math.abs(crater[i] - app.X_height[(int)(i + target_tank.get_X() - r)]);
            assertTrue(Terrain_diff < 1);
        }
    }

    /*
     * Projectile and weapons tests
     */
    @Test
    public void Basic_bullet() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Projectile test_bullet = new Projectile(shooter_tank.get_X(), shooter_tank.get_Y(), 0, 0, shooter_tank);
        app.cur_bullet.add(test_bullet);
        app.noLoop();
        //Check bullet is created
        assertFalse(app.cur_bullet.isEmpty());
        app.loop();
        app.delay(100);
        //Check bullet has exploded
        assertTrue(app.cur_bullet.isEmpty());
        //Explosion is created successfully
        assertFalse(app.explosions.isEmpty());
    }

    @Test
    public void Basic_big_bullet() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Projectile test_bullet = new Big_projectile(shooter_tank.get_X(), shooter_tank.get_Y(), 0, 0, shooter_tank);
        app.cur_bullet.add(test_bullet);
        app.noLoop();
        //Check bullet is created
        assertFalse(app.cur_bullet.isEmpty());
        app.loop();
        app.delay(100);
        //Check that 5 shrapnels have been created
        assertTrue(app.cur_bullet.size() == 5);
        //Explosion is created successfully
        assertFalse(app.explosions.isEmpty());
    }

    @Test
    public void Basic_shrapnel() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Projectile test_bullet = new Shrapnel(shooter_tank.get_X(), shooter_tank.get_Y(), 0, 0, shooter_tank);
        app.cur_bullet.add(test_bullet);
        app.noLoop();
        //Shrapnel is created
        assertFalse(app.cur_bullet.isEmpty());
        app.loop();
        app.delay(100);
        //Shrapnel has exploded
        assertTrue(app.cur_bullet.isEmpty());
        //Explosion created successfully
        assertFalse(app.explosions.isEmpty());
    }

    @Test
    public void Basic_bullet_border() {
        assertNotEquals(app, null);
        Tank shooter_tank = app.tanks_on_field.get(0);
        Projectile test_bullet = new Projectile(0, 0, 0, 0, shooter_tank);
        app.cur_bullet.add(test_bullet);
        Projectile test_bullet_2 = new Projectile(864, 0, 0, 0, shooter_tank);
        app.cur_bullet.add(test_bullet_2);
        app.noLoop();
        //Bullet is created
        assertFalse(app.cur_bullet.isEmpty());
        app.loop();
        app.delay(100);
        //Bullet is deleted
        assertTrue(app.cur_bullet.isEmpty());
    }



    /*
     * Tank tests - Movement
     */

    @Test
    public void Tank_movements_lower() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        float old_x = test_tank.get_X();
        float old_y = test_tank.get_Y();
        app.keyCode = 37;
        app.keyPressed();
        app.delay(100);
        //X coords and Y coords has changed
        assertTrue(test_tank.get_X() < old_x);
        assertTrue(test_tank.get_Y() > old_y);
    }

    @Test
    public void Tank_movements_border() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        for (int i = 0; i < 200; i++){
            app.keyCode = 37;
            app.keyPressed();
        }
        app.delay(100);
        float old_x = test_tank.get_X();
        for (int i = 0; i < 200; i++){
            app.keyCode = 37;
            app.keyPressed();
        }
        //X coords doesn't change due to the tank reaching the border
        assertTrue(test_tank.get_X() == old_x);
    }

    @Test
    public void Tank_movements_higher() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        float old_x = test_tank.get_X();
        float old_y = test_tank.get_Y();
        app.keyCode = 39;
        app.keyPressed();
        app.delay(100);
        //X coords and Y coords has changed
        assertTrue(test_tank.get_X() > old_x);
        assertTrue(test_tank.get_Y() < old_y);
    }

    @Test
    public void Tank_movements_parachuting() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        float old_x = test_tank.get_X();

        Explosion test_Explosion = new Explosion(test_tank.get_X(), test_tank.get_Y(), 300, test_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);
        app.loop();

        app.keyCode = 37;
        app.keyPressed();
        app.delay(500);

        //Check parachuting and XY coords
        assertTrue(test_tank.getFalling());
        assertTrue(test_tank.getParachuting());
        assertTrue(test_tank.get_X() < old_x);
        assertTrue(test_tank.get_Y() < app.X_height[(int) test_tank.get_X()]);
    }

    @Test
    public void Tank_movements_no_fuel() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        float old_x = test_tank.get_X();
        float old_y = test_tank.get_Y();
        test_tank.refuel(-250);
        app.keyCode = 39;
        app.keyPressed();
        app.delay(100);
        //Check no movement is made when there's no fuel
        assertTrue(test_tank.get_X() == old_x);
        assertTrue(test_tank.get_Y() == old_y);
    }

    @Test
    public void Tank_movements_death_by_fall() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        Explosion test_Explosion = new Explosion(test_tank.get_X(), 640, 300, test_tank);
        app.noLoop();
        app.explosions.add(test_Explosion);
        app.loop();
        app.delay(5000);
        //Death due to falling below y = 640
        assertTrue(test_tank.isDead());
    }

    /*
     * Tank tests - weapons and abilities
     */
    @Test
    public void Tank_abilities_broke() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        int test_turn = app.turn;

        app.key = '1';
        app.keyPressed();
        app.key = '2';
        app.keyPressed();
        app.key = 'r';
        app.keyPressed();
        app.key = 'f';
        app.keyPressed();
        app.key = 'p';
        app.keyPressed();
        app.key = 'h';
        app.keyPressed();

        app.delay(1000);
        //No money, no abilities
        assertTrue(test_tank.getHP() == 100);
        assertFalse(test_tank.isShielded());
        assertTrue(test_tank.num_Para() == 3);
        assertTrue(test_tank.getFuel() == 250);
        assertTrue(test_tank.getScore() == 0);
        assertTrue(app.turn == test_turn);
    }

    @Test
    public void Tank_abilities_fire() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        int test_turn = app.turn;

        app.key = ' ';
        app.keyPressed();

        app.delay(1000);
        //Testing fire change turns and bullet creation
        assertTrue(app.turn != test_turn);
        assertTrue(app.cur_bullet.size() == 1);
        assertTrue(app.cur_bullet.get(0).whose == test_tank);
    }

    @Test
    public void Tank_abilities_big_bullet() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        int test_turn = app.turn;

        test_tank.changeScore(420);

        app.key = '1';
        app.keyPressed();

        app.delay(1000);
        //Checking turn change and creation of bullet and cost of ability
        assertTrue(app.turn != test_turn);
        assertTrue(app.cur_bullet.size() == 1);
        assertTrue(app.cur_bullet.get(0).whose == test_tank);
        assertTrue(test_tank.getScore() == 420 - 30);
    }

    @Test
    public void Tank_abilities_shotgun() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);
        int test_turn = app.turn;

        test_tank.changeScore(420);

        app.key = '2';
        app.keyPressed();

        app.delay(1000);
        //Checking turn change and creation of bullet and cost of ability
        assertTrue(app.turn != test_turn);
        assertTrue(app.cur_bullet.size() == 5);
        assertTrue(app.cur_bullet.get(0).whose == test_tank);
        assertTrue(test_tank.getScore() == 420 - 30);
    }

    @Test
    public void Tank_abilities_shield() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        test_tank.changeScore(420);

        app.key = 'h';
        app.keyPressed();

        app.delay(1000);
        //Checking shields and cost of ability
        assertTrue(test_tank.isShielded());
        assertTrue(test_tank.getScore() == 420 - 20);
    }

    @Test
    public void Tank_abilities_extra_para() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        test_tank.changeScore(420);

        app.key = 'p';
        app.keyPressed();

        app.delay(1000);
        // Check number of parachute is now 2
        assertTrue(test_tank.num_Para() == 4);
        // Check cost of ability is 15
        assertTrue(test_tank.getScore() == 420 - 15);
    }

    @Test
    public void Tank_abilities_extra_fuel() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        test_tank.changeScore(420);

        app.key = 'f';
        app.keyPressed();

        app.delay(1000);

        // Check fuel is now 450
        assertTrue(test_tank.getFuel() == 450);
        // Check cost of ability is 10
        assertTrue(test_tank.getScore() == 420 - 10);
    }

    @Test
    public void Tank_abilities_rotate_left() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        for(int i = 0; i < 100; i++){
            app.keyCode = 38;
            app.keyPressed();
        }

        app.delay(1000);
        // Check angle = 180
        assertTrue(test_tank.getAngle() == 180);
    }

    @Test
    public void Tank_abilities_rotate_right() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        for(int i = 0; i < 100; i++){
            app.keyCode = 40;
            app.keyPressed();
        }

        app.delay(1000);
        // Check angle = 0
        assertTrue(test_tank.getAngle() == 0);
    }

    @Test
    public void Tank_abilities_power_up() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        for(int i = 0; i < 100; i++){
            app.key = 'w';
            app.keyPressed();
        }

        app.delay(1000);
        // Check power = 100
        assertTrue(test_tank.getPower() == 100);
    }

    @Test
    public void Tank_abilities_power_down() {
        assertNotEquals(app, null);
        Tank test_tank = app.tanks_on_field.get(0);

        for(int i = 0; i < 100; i++){
            app.key = 's';
            app.keyPressed();
        }

        app.delay(1000);
        // Check power = 0
        assertTrue(test_tank.getPower() == 0);
    }

    /*
    * Gameplay (turn, next level, final scoreboard, reset,...)
    */

    @Test
    public void next_level_test() {
        assertNotEquals(app, null);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).kill(app, 15);
        }

        app.delay(1500);

        // 3 levels
        assertTrue(app.Levels_Config.size() == 3);

        // Extract 19 colours
        assertTrue(app.player_colours.size() == 19);

        // next level is 3
        assertTrue(app.level_num == 3);

        // number of tanks is 4
        assertTrue(app.tanks_on_field.size() == 4);

        // start turn is 0
        assertTrue(app.turn == 0);

        // final score board is null
        assertTrue(app.final_score_board == null);
    }

    @Test
    public void final_score_board_test() {
        assertNotEquals(app, null);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).kill(app, 15);
        }

        app.delay(1500);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).kill(app, 15);
        }

        app.delay(1500);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).changeScore(i*50);
            app.tanks_on_field.get(i).kill(app, 15);
        }
        app.key = ' ';
        app.keyPressed();
        app.delay(3000);

        // next level is 4
        assertTrue(app.level_num == 4);

        // final score board is not null
        assertTrue(app.final_score_board != null);
    }

    @Test
    public void reset_test() {
        assertNotEquals(app, null);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).kill(app, 15);
        }

        app.delay(1500);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).kill(app, 15);
        }

        app.delay(1500);

        for(int i = 0; i < 3; i++){
            app.tanks_on_field.get(i).changeScore(i*50);
            app.tanks_on_field.get(i).kill(app, 15);
        }
        app.key = ' ';
        app.keyPressed();
        app.delay(1500);

        app.key = 'r';
        app.keyPressed();
        app.delay(3000);

        // 3 levels
        assertTrue(app.Levels_Config.size() == 3);

        // Extract 19 colours
        assertTrue(app.player_colours.size() == 19);

        // next level is 2
        assertTrue(app.level_num == 2);

        // number of tanks is 4
        assertTrue(app.tanks_on_field.size() == 4);

        // start turn is 0
        assertTrue(app.turn == 0);

        // wind is < 35 and > -35
        assertTrue(app.wind >= -35 && app.wind <= 35);

        // final score board is null
        assertTrue(app.final_score_board == null);
    }

}
