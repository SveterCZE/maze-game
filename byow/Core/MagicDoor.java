package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class MagicDoor extends Game_Item {
    TETile[][] game_world;
    boolean door_open;

    public MagicDoor (TETile[][] world, Random generator, List<Room_v2> room_list) {
        game_world = world;
        Room_v2 door_room = room_list.get(RandomUtils.uniform(generator, room_list.size()));
        item_position = door_room.get_random_corridor_start(RandomUtils.uniform(generator, 4), generator);
        while (item_position == null) {
            item_position = door_room.get_random_corridor_start(RandomUtils.uniform(generator, 4), generator);
        }
        door_open = false;
    }

    public void draw(){
        if (isDoor_open()){
            game_world[item_position.get_x_coord()][item_position.get_y_coord()] = Tileset.UNLOCKED_DOOR;
        }
        else {
            game_world[item_position.get_x_coord()][item_position.get_y_coord()] = Tileset.LOCKED_DOOR;
        }
    }

    public boolean isDoor_open(){
        return door_open;
    }

    public void set_door_open(){
        door_open = true;
        draw();
    }

}
