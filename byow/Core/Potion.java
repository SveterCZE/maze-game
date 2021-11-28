package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Potion extends Game_Item {
    TETile[][] game_world;

    public Potion(TETile[][] world, Random generator, List<Room_v2> room_list) {
        game_world = world;
        Room_v2 map_room = room_list.get(RandomUtils.uniform(generator, room_list.size()));
        item_position = map_room.get_random_position_in_room(generator);
    }

    public void draw(){
        game_world[item_position.get_x_coord()][item_position.get_y_coord()] = Tileset.POTION;
    }

    public String get_item_name() {
        return "Anti-ghost potion";
    }
}
