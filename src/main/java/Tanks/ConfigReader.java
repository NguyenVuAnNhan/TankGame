package Tanks;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import processing.data.JSONArray;
import processing.data.JSONObject;

public class ConfigReader {
    
    /**
    * The height of the window in cells
    */
    private static final int BOARD_HEIGHT = 20;

    /**
    * The height of the window in cells
    */
    private static final int BOARD_WIDTH = 28;

    /**
     * load the JSON file given, update the configuration of all levels in the application provided.
     * @param configPath config path string.
     * @param app the application to load in.
     */
    public static void loadJSON_Levels_Config(String configPath, App app){
        try{
            JSONObject config = app.loadJSONObject(configPath);

            JSONArray levels = config.getJSONArray("levels");

            for(int i = 0; i < levels.size(); i++){
                JSONObject level = levels.getJSONObject(i);
                HashMap<String, String> Level = new HashMap<>();
                Level.put("layout", level.getString("layout"));
                Level.put("background", level.getString("background"));
                Level.put("foreground-colour", level.getString("foreground-colour"));

                if (level.hasKey("trees")){
                    Level.put("trees", level.getString("trees"));
                }
                else{
                    Level.put("trees", "tree1.png");
                }

                app.Levels_Config.put(i + 1, Level);

            }

        }
        catch (Exception e) {
            // Handle other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * load the JSON file given, update the colours configuration.
     * @param configPath config path string.
     * @param app the application to load in.
     */
    public static void loadJSON_Colours(String configPath, App app){
        try{
            JSONObject config = app.loadJSONObject(configPath);
            JSONObject colours = config.getJSONObject("player_colours");
            for (char c = 'A'; c <= 'I'; c++){
                String string = c + "";
                app.player_colours.put(string, colours.getString(string));
            }

            for (char c = '0'; c <= '9'; c++){
                String string = c + "";
                app.player_colours.put(string, colours.getString(string));
            }
            }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Initialise the cell board
     * @return the board, a char[][] 2d array.
     */
    public static char[][] board_initialise(){
        char[][] board = new char[BOARD_HEIGHT][BOARD_WIDTH];

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                board[i][j] = ' ';
            }
        }

        return board;
    }

    /**
     * Clean the cell board read from the .txt file.
     * @param string_array read from the .txt file
     * @param board char 2-d array representing the cell board.
     * @return cleaned char 2-d array representing the cell board.
     */
    public static char[][] board_cleaner(String[] string_array, char[][] board){
        for (int i = 0; i < string_array.length; i++){

            String str = string_array[i];

            if (str == null){
                break;
            }

            for (int s_index = 0; s_index < str.length(); s_index++){
                if (str.charAt(s_index) == '\t'){
                    board[i][s_index] = ' ';
                }
                else{
                    board[i][s_index] = str.charAt(s_index);
                }
            }
        }

        return board;
    }

    /**
     * Read from the .txt file, returning each line in it.
     * @param map .txt file containing the map.
     * @return string_array string 1-d array.
     */
    public static String[] string_array_init(File map){
        try{
            Scanner scan = new Scanner(map);

            String[] string_array = new String[BOARD_HEIGHT];

            int index = 0;

            while(scan.hasNextLine() && index < BOARD_HEIGHT){
                String string = scan.nextLine();
                string_array[index] = string;
                index += 1;
            }

            scan.close();
            
            return string_array;
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
        
    }

    /**
     * Read from the level hashmap, returning its cleaned terrain map.
     * @param level hashmap representation of a level.
     * @return cleaned char 2-d array representing the cell board.
     */
    public static char[][] readLevel(HashMap<String, String> level){
        File map = new File(level.get("layout"));
        
        String[] string_array = string_array_init(map);

        char[][] board = board_initialise();

        board_cleaner(string_array, board);

        return board;
    }
}
