package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Room_v2 {
    Position_v2 top_left_coord;
    Position_v2 top_right_coord;
    Position_v2 bottom_left_coord;
    Position_v2 bottom_right_coord;
    int width;
    int height;
    TETile[][] world;
    boolean is_valid;

    Set<Position_v2> bordering_tiles;
    Set<Position_v2> blocked_tiles;

    public Room_v2(Position_v2 initial_position, int w, int h, TETile[][] my_world) {
        top_left_coord = initial_position;
        width = w;
        height = h;
        world = my_world;
        top_right_coord = new Position_v2(top_left_coord.get_x_coord() + (width - 1), top_left_coord.get_y_coord());
        bottom_left_coord = new Position_v2(top_left_coord.get_x_coord(), top_left_coord.get_y_coord() - (height - 1));
        bottom_right_coord = new Position_v2(top_left_coord.get_x_coord() + (width - 1), top_left_coord.get_y_coord() - (height - 1));
        is_valid = check_valid_room();
        bordering_tiles = generate_room_borders(this);
        blocked_tiles = new HashSet<>();
    }

    private boolean check_valid_room() {
        if (top_left_coord.isValid() && top_right_coord.isValid() && bottom_left_coord.isValid() && bottom_right_coord.isValid()) return true;
        else return false;
    }

    public static boolean check_no_room_overlap(Room_v2 room, Room_v2 other_room) {
        // Check first combination
        boolean first_combination = check_two_rooms_not_overlap(room, other_room);
        // Check second combination
        boolean second_combination = check_two_rooms_not_overlap(other_room, room);
        return (first_combination && second_combination);
    }

    private static boolean check_two_rooms_not_overlap (Room_v2 room, Room_v2 extended_room) {
        boolean top_left = is_point_in_a_room(room.top_left_coord, extended_room);
        boolean top_right = is_point_in_a_room(room.top_right_coord, extended_room);
        boolean bottom_left = is_point_in_a_room(room.bottom_left_coord, extended_room);
        boolean bottom_right = is_point_in_a_room(room.bottom_right_coord, extended_room);
        if (top_left == true || top_right == true || bottom_left == true || bottom_right == true) return false;
        else return true;
    }

    public static boolean rooms_do_not_cross(Room_v2 room, Set borders_point_set) {
        for (Position_v2 checked_position: room.bordering_tiles) {
            if (borders_point_set.contains(checked_position)) return false;
        }
        for (Position_v2 checked_position: room.bordering_tiles) borders_point_set.add(checked_position);
        return true;
    }

    private static Set<Position_v2> generate_room_borders(Room_v2 room) {
        Set<Position_v2> room_borders_list = new HashSet<>();
        // Add positions from top border
        for (int i = 0; i < room.width; i += 1) {
            room_borders_list.add(new Position_v2(room.top_left_coord.get_x_coord() + i, room.top_left_coord.get_y_coord()));
        }
        // Add positions from bottom border
        for (int i = 0; i < room.width; i += 1) {
            room_borders_list.add(new Position_v2(room.top_left_coord.get_x_coord() + i,(room.top_left_coord.get_y_coord() - room.height) + 1));
        }
        // Add positions from left border
        for (int i = 0; i < room.height; i += 1) {
            room_borders_list.add(new Position_v2(room.top_left_coord.get_x_coord(), room.top_left_coord.get_y_coord() - i));
        }

        // Add positions from right border
        for (int i = 0; i < room.height; i += 1) {
            room_borders_list.add(new Position_v2(room.top_left_coord.get_x_coord() + (room.width - 1), room.top_left_coord.get_y_coord() - i));
        }
        return room_borders_list;
    }

    // Check if I should remove minus one and plus one here
    public static boolean is_point_in_a_room (Position_v2 posit, Room_v2 room) {
        if ((posit.get_x_coord() >= room.top_left_coord.get_x_coord() - 1) && (posit.get_y_coord() <= room.top_left_coord.get_y_coord() + 1) &&
                (posit.get_x_coord() <= room.bottom_right_coord.get_x_coord() + 1) && (posit.get_y_coord() >= room.bottom_right_coord.get_y_coord() - 1)) return true;
        else return false;
    }

    public boolean is_corner_coord(Position_v2 checked_position) {
        if ((top_left_coord.equals(checked_position)) || (top_right_coord.equals(checked_position))
                || (bottom_left_coord.equals(checked_position)) || (bottom_right_coord.equals(checked_position))) return true;
        return false;
    }

    public boolean is_blocked (Position_v2 checked_position, int build_direction) {
        Position_v2 position_1;
        Position_v2 position_2;
        if ((build_direction == 0) || (build_direction == 1)) {
            position_1 = new Position_v2(checked_position.get_x_coord() + 1, checked_position.get_y_coord());
            position_2 = new Position_v2(checked_position.get_x_coord() - 1, checked_position.get_y_coord());
        }
        else {
            position_1 = new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() + 1);
            position_2 = new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() - 1);
        }
        if (blocked_tiles.contains(position_1) || blocked_tiles.contains(position_2)) return true;
        else return false;
    }

    public void block_neigbouring_tiles(Position_v2 checked_position, int build_direction) {
        if ((build_direction == 0) || (build_direction == 1)) {
            blocked_tiles.add(new Position_v2(checked_position.get_x_coord() + 1, checked_position.get_y_coord()));
            blocked_tiles.add(new Position_v2(checked_position.get_x_coord() - 1, checked_position.get_y_coord()));
            blocked_tiles.add(checked_position);
        }
        else {
            blocked_tiles.add(new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() + 1));
            blocked_tiles.add(new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() - 1));
            blocked_tiles.add(checked_position);
        }
    }

    public void draw_room() {
        if (is_valid) {
            // Draw room borders
            draw_top_border();
            draw_bottom_border();
            draw_left_border();
            draw_right_border();
            // Draw room interior
            draw_room_interior();
        }
    }

    private void draw_top_border() {
        for (int i = 0; i < width; i += 1) {
            world[top_left_coord.get_x_coord() + i][top_left_coord.get_y_coord()] = Tileset.WALL;
        }
    }

    private void draw_bottom_border() {
        for (int i = 0; i < width; i += 1) {
            world[top_left_coord.get_x_coord() + i][(top_left_coord.get_y_coord() - height) + 1] = Tileset.WALL;
        }
    }

    private void draw_left_border() {
        for (int i = 0; i < height; i += 1) {
            world[top_left_coord.get_x_coord()][top_left_coord.get_y_coord() - i] = Tileset.WALL;
        }
    }

    private void draw_right_border() {
        for (int i = 0; i < height; i += 1) {
            world[top_left_coord.get_x_coord() + (width - 1)][top_left_coord.get_y_coord() - i] = Tileset.WALL;
        }
    }

    private void draw_room_interior() {
        for (int i = 1; i < height - 1; i += 1) {
            for (int j = 1; j < width - 1; j += 1) {
                world[top_left_coord.get_x_coord() + j][top_left_coord.get_y_coord() - i] = Tileset.FLOOR;
            }
        }
    }

    public Position_v2 get_random_corridor_start(int direction, Random rand_gen) {
        Position_v2 start_position;
        // Select the starting point
        if (direction == 0) {
            int offset = RandomUtils.uniform (rand_gen, 1, this.width - 1);
            start_position = new Position_v2(this.top_left_coord.get_x_coord() + offset, this.top_left_coord.get_y_coord());
        }
        else if (direction == 1) {
            int offset = RandomUtils.uniform (rand_gen, 1, this.width - 1);
            start_position = new Position_v2(this.bottom_left_coord.get_x_coord() + offset, this.bottom_left_coord.get_y_coord());
        }
        else if (direction == 2) {
            int offset = RandomUtils.uniform (rand_gen, 1, this.height - 1);
            start_position = new Position_v2(this.top_left_coord.get_x_coord(), this.top_left_coord.get_y_coord() - offset);
        }
        else {
            int offset = RandomUtils.uniform (rand_gen, 1, this.height - 1);
            start_position = new Position_v2(this.top_right_coord.get_x_coord(), this.top_right_coord.get_y_coord() - offset);
        }
        if (blocked_tiles.contains(start_position)) return null;
        else return start_position;
    }

    public Position_v2 get_random_position_in_room(Random rand_gen){
        int x_coordinate = RandomUtils.uniform(rand_gen, this.top_left_coord.get_x_coord() + 1, this.top_right_coord.get_x_coord() - 1);
        int y_coordinate = RandomUtils.uniform(rand_gen, this.bottom_left_coord.get_y_coord() + 1, this.top_left_coord.get_y_coord() - 1);
        return new Position_v2(x_coordinate, y_coordinate);
    }

}
