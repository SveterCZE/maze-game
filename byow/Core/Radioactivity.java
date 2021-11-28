package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Radioactivity extends Game_Item {
    TETile[][] game_world;

    public Radioactivity(TETile[][] world, Random generator, List<Room_v2> room_list){
        game_world = world;
        Room_v2 radioactive_room = room_list.get(RandomUtils.uniform(generator, room_list.size()));
        item_position = radioactive_room.get_random_position_in_room(generator);
    }

    public void draw(){
        game_world[item_position.get_x_coord()][item_position.get_y_coord()] = Tileset.RADIOACTIVE;
    }
}
