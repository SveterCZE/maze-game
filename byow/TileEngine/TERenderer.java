package byow.TileEngine;

import byow.Core.Game_Properties;
import byow.Core.Position_v2;
import byow.Core.*;

import java.awt.Color;
import java.awt.Font;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);      
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
//    public void renderFrame(TETile[][] world) {
//        renderFrame_helper(world);
//        StdDraw.show();
//    }

    public void renderFrame(TETile[][] world, Game_Properties game_properties) {
        // Determine whether a fog of war should be enabled
        if (game_properties.get_fog_of_war()) renderFrame_helper(world, true, game_properties);
        else renderFrame_helper(world, false, game_properties);

        int numXTiles = world.length;
        int numYTiles = world[0].length;
        // Draw the status information on the top
        StdDraw.setPenColor(Color.white);
        StdDraw.text(7, numYTiles + 2, "Level: " + game_properties.get_level_no());
        // Display the tile name
        try {
            StdDraw.text(25, numYTiles + 2, world[game_properties.get_x_coord()][game_properties.get_y_coord()].description() +
                    "(X: " + game_properties.get_x_coord() + " Y: " + game_properties.get_y_coord() + ")");
        }
        catch  (Exception ignored) {
        }
        //  Display the inventory contents
        StdDraw.text(40, numYTiles + 2, "Health: " + game_properties.get_health());
        StdDraw.text(65, numYTiles + 2, "Inventory: " + game_properties.get_inventory_content_as_string());
        if (game_properties.is_player_radioactive()){
            StdDraw.setPenColor(Color.yellow);
            StdDraw.text(90, numYTiles + 2, "RADIOACTIVE");
        }
        StdDraw.show();
    }

    public void renderFrame(TETile[][] world, boolean victory_achieved, Game_Properties game_properties) {
        renderFrame_helper(world, game_properties.get_fog_of_war(), game_properties);
        Font bigFont = new Font("Monaco", Font.BOLD, 175);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        //  Screen displayed on victory
        if (victory_achieved) {
            StdDraw.text(77, 50, "VICTORY ACHIEVED!!!");
            StdDraw.show();
            StdDraw.pause(1000);
            StdDraw.text(77, 30, "NEXT ROUND!!!");
            StdDraw.show();
        }
        //  Screen displayed on death
        else {
            StdDraw.text(77, 50, "YOU DIED!!!");
            StdDraw.show();
            StdDraw.pause(1000);
            StdDraw.text(77, 30, "TRY AGAIN!!!");
            StdDraw.show();
        }
    }

    private void renderFrame_helper(TETile[][] world, boolean fog_of_war, Game_Properties game_properties){
        if (!fog_of_war) {
            int numXTiles = world.length;
            int numYTiles = world[0].length;
            StdDraw.clear(new Color(0, 0, 0));
            for (int x = 0; x < numXTiles; x += 1) {
                for (int y = 0; y < numYTiles; y += 1) {
                    if (world[x][y] == null) {
                        throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                                + " is null.");
                    }
                    world[x][y].draw(x + xOffset, y + yOffset, false);
                }
            }
        }
        else {
            // Firstly draw everything as black
            int numXTiles = world.length;
            int numYTiles = world[0].length;
            StdDraw.clear(new Color(0, 0, 0));
            for (int x = 0; x < numXTiles; x += 1) {
                for (int y = 0; y < numYTiles; y += 1) {
                    if (world[x][y] == null) {
                        throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                                + " is null.");
                    }
                    world[x][y].draw(x + xOffset, y + yOffset, true);
                }
            }
            // Secondly, draw a field of view around the avatar
            Position_v2 avatar_position = game_properties.get_avatar().get_position();

            // Determine the borders of the field of view
            int bottom_left_x_pos = avatar_position.get_x_coord() - 5;
            if (bottom_left_x_pos < 0) bottom_left_x_pos = 0;

            int bottom_left_y_pos = avatar_position.get_y_coord() - 5;
            if (bottom_left_y_pos < 0) bottom_left_y_pos = 0;

            int top_right_x_pos = avatar_position.get_x_coord() + 5;
            if (top_right_x_pos > world.length) top_right_x_pos = world.length;

            int top_right_y_pos = avatar_position.get_y_coord() + 5;
            if (top_right_y_pos > world[0].length) top_right_y_pos = world[0].length;

            // Draw the tiles
            for (int x = bottom_left_x_pos; x < top_right_x_pos; x += 1) {
                for (int y = bottom_left_y_pos; y < top_right_y_pos; y += 1) {
                    if (world[x][y] == null) {
                        throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                                + " is null. Error when displaying the field of view.");
                    }
                    world[x][y].draw(x + xOffset, y + yOffset, false);
                }
            }
        }




    }
}
