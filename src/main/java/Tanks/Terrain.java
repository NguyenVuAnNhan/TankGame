package Tanks;

import java.util.*;

public class Terrain {
    /**
    * The width of a cell
    */
    private static final int CELLSIZE = 32;

    /**
     * Method for calculating the moving average. Requires
     * a float array representing the height of the terrain.
     * @param X_height float array to change.
     */
    public static void movingAverage(float[] X_height){
        for (int i = 0; i < 864; i++){
            double sum  = 0;
            for (int j = 0; j < CELLSIZE; j++){
                sum += X_height[i+j];
            }
            X_height[i] = (float) sum/CELLSIZE;
        }
    }

    /**
     * Method for finding trees in a board. Requires
     * a char 2d array as the board.
     * @param board char 2d array of the board.
     * @return the list of location for tree
     */
    public static ArrayList<Integer> find_Tree(char[][] board){
        ArrayList<Integer> Tree_X = new ArrayList<>();

        Random random = new Random();

        for (int j = 0; j < board[0].length; j++){
            for (int i = 0; i < board.length; i++){
                if (board[i][j] == 'T'){
                    int x = (j*CELLSIZE + 1 + random.nextInt(30));
                    if (x < 0){
                        x = 0;
                    }
                    else if (x > 864){
                        x = 864;
                    }
                    Tree_X.add(x);
                }
            }
        }

        return Tree_X;
    }


    /**
     * Method for finding tanks in a board. Requires
     * a char 2d array as the board.
     * @param board char 2d array of the board.
     * @param player_colours a hashamp representing the colour of each player
     * @param app App to set up.
     */
    public static void find_Tank(char[][] board, HashMap<String, String> player_colours, App app){
        for (int j = 0; j < board[0].length; j++){
            for (int i = 0; i < board.length; i++){
                if (player_colours.containsKey(Character.toString(board[i][j]))){
                    int x = (j*CELLSIZE + CELLSIZE/2);
                    boolean status = false;

                    for (Tank tank: app.tanks_on_field){
                        if (tank.get_id().equals(Character.toString(board[i][j]))){
                            tank.setTank_X(x);
                            status = true;
                        }
                    }

                    if (!status){
                        Tank new_tank = new Tank(Character.toString(board[i][j]), x, app.player_colours.get(Character.toString(board[i][j])));
                        app.tanks_on_field.add(new_tank);
                    }                    
                }
                Collections.sort(app.tanks_on_field, new Comparator<Tank>() {
                    public int compare(Tank a, Tank b){
                        if (a.get_id().charAt(0) > b.get_id().charAt(0)){
                            return 1;
                        }
                        else if (a.get_id().charAt(0) < b.get_id().charAt(0)){
                            return -1;
                        }
                        else{
                            return 0;
                        }
                    }
                });
            }
        }
    }

    /**
     * Method for initialising terrain height from a board.
     * Requires a char 2d array as the board.
     * @param board char 2d array of the board.
     * @return X_height, the float array representing the height of the terrain
     */

    public static float[] init_X_height(char[][] board){
        float[] X_height = new float[896];

        int[] cur_X_height = new int[28];
        for (int j = 0; j < board[0].length; j++){
            for (int i = 0; i < board.length; i++){
                if (board[i][j] == 'X'){
                    cur_X_height[j] = i;
                }
            }
        }

        for (int i = 0; i < 896; i++){
            X_height[i] = cur_X_height[i/CELLSIZE]*CELLSIZE;
        }

        movingAverage(X_height);
        movingAverage(X_height);

        return X_height;
    }

    /**
     * Method for setting up the map given the
     * board and an App to set up.
     * @param board char 2d array of the board.
     * @param app App to set up.
     */
    public static void map_setup(char[][] board, App app){
        app.Tree_X = find_Tree(board);
        app.X_height = init_X_height(board);
        find_Tank(board, app.player_colours, app);
        for (Tank tank : app.tanks_on_field){
            tank.setTank_Y(app.X_height[(int) tank.get_X()]);
            tank.reset();
        }
    }
}
