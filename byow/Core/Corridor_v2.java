package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Corridor_v2 {
    Position_v2 start_position;
    boolean bended_corridor;
    boolean start_point_valid;
    boolean buildable;
    List<Position_v2> list_of_positions;
    int build_direction;
    TETile[][] world;
    Set<Position_v2> border_items_set;
    List<Room_v2> room_list;
    Room_v2 start_room;
    Room_v2 end_room;
    Random ran_gen;
    int start_room_id;
    UnionFind room_union;

    public Corridor_v2(Position_v2 start, int direction, TETile[][] my_world, Set<Position_v2> border_items, List<Room_v2> rooms, Random generator, UnionFind union_find_rooms) {
        start_position = start;
        start_point_valid = start.isValid();
        build_direction = direction;
        world = my_world;
        border_items_set = border_items;
        room_list = rooms;
        start_room = identify_reached_room(start_position, room_list);
        ran_gen = generator;
        bended_corridor = RandomUtils.bernoulli(ran_gen, 0.33);
        start_room_id = get_room_id(start_room, room_list);
        room_union = union_find_rooms;
    }

    public void build(){
        list_of_positions = new ArrayList<>();
        list_of_positions.add(start_position);
        boolean next_step_available = true;
        while (next_step_available) {
            Position_v2 next_step_position = Position_v2.get_next_step_position(build_direction, list_of_positions.get(list_of_positions.size() - 1));
            // Break if the position is not valid
            if (next_step_position.isValid() == false) {
                buildable = false;
                next_step_available = false;
            }
            // If your next step is grass - you are touching a neighbouring corridor
            else if (world[next_step_position.get_x_coord()][next_step_position.get_y_coord()] == Tileset.FLOOR){
                buildable = false;
                next_step_available = false;
            }
            // If you hit a wall
            else if (world[next_step_position.get_x_coord()][next_step_position.get_y_coord()] == Tileset.WALL) {
                // If you hit a wall of a room
                if (border_items_set.contains(next_step_position)) {
                    list_of_positions.add(next_step_position);
                    // Identify the room you are touching
                    Room_v2 reached_room = identify_reached_room(next_step_position, room_list);
                    // Identify if you can connect it - you must not connect a corner or a position next to other corridor
                    boolean not_corner = !reached_room.is_corner_coord(next_step_position);
                    boolean not_blocked = !reached_room.is_blocked(next_step_position, build_direction);
                    int reached_room_id = get_room_id(reached_room, room_list);
                    boolean not_already_connected = !room_union.connected(start_room_id, reached_room_id);

                    if (not_corner && not_blocked && not_already_connected) {
                        buildable = true;
                        // If you can build it, block the neighbouring two tiles on both connected nodes
                        end_room = reached_room;
                        start_room.block_neigbouring_tiles(start_position, build_direction);
                        end_room.block_neigbouring_tiles(next_step_position, build_direction);
                        // Reflect this in union data structure
                        room_union.union(start_room_id, reached_room_id);
                    }
                    next_step_available = false;
                }
                // If you hit a wall of a corridor
                // TODO - How to connect it to the rest
                else {
                    buildable = false;
                    next_step_available = false;
                }
            }
            // If it is an empty space
            else list_of_positions.add(next_step_position);
            // Check if the corridor should bend
            if (bended_corridor) {
                boolean change_direction = RandomUtils.bernoulli(ran_gen, 0.15);
                if (change_direction) {
                    bended_corridor = false;
                    this.change_direction();
                }
            }
        }
    }

    public void draw(){
        if (buildable) {
            for (Position_v2 x: list_of_positions){
                world[x.get_x_coord()][x.get_y_coord()] = Tileset.FLOOR;
            }
            for (Position_v2 x: list_of_positions){
                this.draw_borders_corridor(x);
            }
        }
    }

    private Room_v2 identify_reached_room(Position_v2 checked_position, List<Room_v2> room_list) {
        for (Room_v2 tried_room: room_list){
            if (tried_room.bordering_tiles.contains(checked_position)) return tried_room;
        }
        return null;
    }

    private void draw_borders_corridor(Position_v2 checked_position) {
        List<Position_v2> neighbouring_positions = Position_v2.get_neighbouring_positions(checked_position);
        for (Position_v2 possible_position: neighbouring_positions) {
            if (world[possible_position.get_x_coord()][possible_position.get_y_coord()] == Tileset.NOTHING) {
                world[possible_position.get_x_coord()][possible_position.get_y_coord()] = Tileset.WALL;
            }
        }
    }

    private int get_room_id(Room_v2 checked_room, List<Room_v2> room_list) {
        for (int i = 0; i < room_list.size(); i += 1) {
            if (room_list.get(i).equals(checked_room)) return i;
        }
        return -1;
    }

    private void change_direction(){
        int option = RandomUtils.uniform(ran_gen, 2);
        if (this.build_direction == 0 || this.build_direction == 1){
            if (option == 0) this.build_direction = 2;
            else this.build_direction = 3;
        }
        else {
            if (option == 0) this.build_direction = 0;
            else this.build_direction = 1;
        }
    }

    public boolean is_buildable(){
        return buildable;
    }

    public Room_v2 get_start_room(){
        return start_room;
    }

    public Room_v2 get_end_room() {
        return end_room;
    }

    public Position_v2 get_first_position(){
        return list_of_positions.get(0);
    }

    public Position_v2 get_last_position(){
        return list_of_positions.get(list_of_positions.size() - 1);
    }

    public List<Position_v2> get_corridor_positions_list(){
        return list_of_positions;
    }

}
