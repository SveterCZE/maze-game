package byow.Core;

import byow.TileEngine.TETile;

public class Game_Item {

    Position_v2 item_position;

    public Position_v2 get_position() {
        return item_position;
    }

    public void set_new_position(Position_v2 new_position){
        item_position = new_position;
    }

    public String get_item_name() {
        return "";
    }

}
