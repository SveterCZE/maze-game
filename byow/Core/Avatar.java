package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.List;
import java.util.Random;

public class Avatar {
    Position_v2 avatar_position;
    TETile[][] game_world;
    Game_Properties game_prop;

    public Avatar (TETile[][] world, Random generator, List<Room_v2> room_list, Game_Properties game_status) {
        game_world = world;
        Room_v2 generation_room = room_list.get(RandomUtils.uniform(generator, room_list.size()));
        avatar_position = generation_room.get_random_position_in_room(generator);
        game_prop = game_status;
    }

    public Position_v2 get_position(){
        return avatar_position;
    }

    public void draw(){
        game_world[avatar_position.get_x_coord()][avatar_position.get_y_coord()] = Tileset.AVATAR;
    }

    public void take_action(String action_instruction){
        if (action_instruction.equals("W") || action_instruction.equals("w")) {
            move_up();
            draw();
        }
        else if (action_instruction.equals("A") || action_instruction.equals("a")) {
            move_left();
            draw();
        }
        else if (action_instruction.equals("S") || action_instruction.equals("s")) {
            move_down();
            draw();
        }
        else if (action_instruction.equals("D") || action_instruction.equals("d")) {
            move_right();
            draw();
        }
    }

    private void move_up() {
        Position_v2 new_position = new Position_v2(avatar_position.get_x_coord(), avatar_position.get_y_coord() + 1);
        if (check_tile_not_inaccessible(new_position)) move_avatar(new_position);
    }

    private void move_left() {
        Position_v2 new_position = new Position_v2(avatar_position.get_x_coord() - 1, avatar_position.get_y_coord());
        if (check_tile_not_inaccessible(new_position)) move_avatar(new_position);
    }

    private void move_down() {
        Position_v2 new_position = new Position_v2(avatar_position.get_x_coord(), avatar_position.get_y_coord() - 1);
        if (check_tile_not_inaccessible(new_position)) move_avatar(new_position);
    }

    private void move_right() {
        Position_v2 new_position = new Position_v2(avatar_position.get_x_coord() + 1, avatar_position.get_y_coord());
        if (check_tile_not_inaccessible(new_position)) move_avatar(new_position);
    }

    private boolean check_tile_not_inaccessible(Position_v2 checked_position) {
        if (game_world[checked_position.get_x_coord()][checked_position.get_y_coord()].equals(Tileset.WALL) || game_world[checked_position.get_x_coord()][checked_position.get_y_coord()].equals(Tileset.LOCKED_DOOR))
            return false;
        else return true;
    }

    private void move_avatar (Position_v2 new_position) {
        if (game_prop.is_marker_equipped()) game_world[avatar_position.get_x_coord()][avatar_position.get_y_coord()] = Tileset.WATER;
        else game_world[avatar_position.get_x_coord()][avatar_position.get_y_coord()] = Tileset.FLOOR;
        avatar_position = new_position;
        draw();
    }
}
