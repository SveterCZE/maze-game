package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Ghost extends Game_Item {
    TETile[][] game_world;
    TETile temp_tile;
    List<Position_v2> path_to_player;
    Position_v2 last_position;
    boolean ghost_active;

    public Ghost(TETile[][] world, Random generator, List<Room_v2> room_list){
        game_world = world;
        Room_v2 radioactive_room = room_list.get(RandomUtils.uniform(generator, room_list.size()));
        item_position = radioactive_room.get_random_position_in_room(generator);
        temp_tile = game_world[item_position.get_x_coord()][item_position.get_y_coord()];
        path_to_player = new LinkedList<>();
        last_position = item_position;
        ghost_active = true;
    }

    public void draw(){
        game_world[item_position.get_x_coord()][item_position.get_y_coord()] = Tileset.GHOST;
    }

    public void draw_temp_tile(){
        game_world[item_position.get_x_coord()][item_position.get_y_coord()] = temp_tile;
    }

    public void set_new_temp_tile(){
        temp_tile = game_world[item_position.get_x_coord()][item_position.get_y_coord()];
    }

    public void chase_player(Game_Properties game_status) {
        if (!ghost_active) return;
        if (avatar_and_ghost_in_rooms(game_status)) find_journey_to_player(game_status);
        if (path_to_player.size() != 0) {
//            draw_ghost_path(path_to_player);
            move_ghost_to_next_location(path_to_player);
        }
    }

    private void find_journey_to_player(Game_Properties game_status) {
        path_to_player = new LinkedList<>();
        Room_v2 ghost_room = get_room_by_position(game_status.get_ghost().get_position(), game_status.get_room_list());
        Room_v2 player_room = get_room_by_position(game_status.get_avatar().get_position(), game_status.get_room_list());
        find_journey_to_player_recursive_helper(game_status, ghost_room, player_room);
    }

    private void find_journey_to_player_recursive_helper(Game_Properties game_status, Room_v2 ghost_room, Room_v2 player_room) {
        // Find a list of rooms between the player and the ghost
        List<Room_v2> ghost_room_journey = game_status.get_room_graph().find_connection_rooms(ghost_room, player_room);

        // BASE CASE --- The Ghost is in the same room as the player
        if (ghost_room_journey.size() == 1) generate_journey_in_room(get_head_of_search_journey(), game_status.get_avatar().get_position());

        // RECURSIVE CASE --- They are in different rooms
        else {
            generate_journey_over_rooms(game_status, ghost_room_journey);
            Room_v2 new_ghost_room = get_room_by_position(get_head_of_search_journey(), game_status.get_room_list());
            find_journey_to_player_recursive_helper(game_status, new_ghost_room, player_room);
        }

    }

    private void move_ghost_to_next_location(List<Position_v2> path_to_player){
        // Generate new position and check that it is not wall
        Position_v2 new_ghost_position = path_to_player.get(0);
        if (new_ghost_position.isValid() && !new_ghost_position.is_it_wall(game_world)) {
            draw_temp_tile();
            set_new_position(new_ghost_position);
            set_new_temp_tile();
            this.draw();
        }
        last_position = path_to_player.get(0);
        path_to_player.remove(0);
    }

    private void generate_journey_in_room (Position_v2 start_position, Position_v2 target_position) {
        int x_offset;
        int y_offset;

        // Generate positions until the player position is reached
        while (!target_position.equals(start_position)) {
            // Set the x axis movement first
            if (start_position.get_x_coord() == target_position.get_x_coord()) x_offset = 0;
            else if (start_position.get_x_coord() > target_position.get_x_coord()) x_offset = -1;
            else x_offset = 1;
            // Set the y axis movement second
            if (start_position.get_y_coord() == target_position.get_y_coord()) y_offset = 0;
            else if (start_position.get_y_coord() > target_position.get_y_coord()) y_offset = -1;
            else y_offset = 1;
            Position_v2 both_axes_movememt = new Position_v2(start_position.get_x_coord() + x_offset, start_position.get_y_coord() + y_offset);
            Position_v2 x_axis_movement = new Position_v2(start_position.get_x_coord() + x_offset, start_position.get_y_coord());
            Position_v2 y_axis_movement = new Position_v2(start_position.get_x_coord(), start_position.get_y_coord() + y_offset);

            if (!x_axis_movement.is_it_wall(game_world) && !x_axis_movement.equals(start_position) && !is_this_last_visited_position(x_axis_movement)){
                start_position = x_axis_movement;
                path_to_player.add(start_position);

            }
            else if (!y_axis_movement.is_it_wall(game_world)  && !y_axis_movement.equals(start_position) && !is_this_last_visited_position(y_axis_movement)){
                start_position = y_axis_movement;
                path_to_player.add(start_position);

            }

            else if (!both_axes_movememt.is_it_wall(game_world)){
                start_position = both_axes_movememt;
                path_to_player.add(start_position);

            }

            else {
                start_position = try_alternative_next_steps(start_position);
                path_to_player.add(start_position);
            }
        }
    }

    private void generate_journey_over_rooms (Game_Properties game_status, List<Room_v2> rooms_journey) {
        // Identify the first and second room of the journey and the corridor that connects them
        Room_v2 current_room = rooms_journey.get(0);
        Room_v2 next_room = rooms_journey.get(1);
        Corridor_v2 connecting_corridor = game_status.find_corridor_connecting_rooms(current_room, next_room);
        // Determine the position in which the corridor joins the room with
        Position_v2 corridor_join_position;
        if (Room_v2.is_point_in_a_room(connecting_corridor.get_first_position(), current_room)) corridor_join_position = connecting_corridor.get_first_position();
        else if (Room_v2.is_point_in_a_room(connecting_corridor.get_last_position(), current_room)) corridor_join_position = connecting_corridor.get_last_position();
        else corridor_join_position = null;
        // Find a journey to that corridor
        generate_journey_in_room(get_head_of_search_journey(), corridor_join_position);
        // Add a journey through that corridor
        generate_journey_through_corridor(connecting_corridor, corridor_join_position);
    }

    private Room_v2 get_room_by_position(Position_v2 checked_position, List<Room_v2> room_list){
        for (Room_v2 checked_room: room_list) {
            if (Room_v2.is_point_in_a_room(checked_position, checked_room)) return checked_room;
        }
        return null;
    }

    private void draw_ghost_path(List<Position_v2> ghost_path){
        for (int i = 1; i < ghost_path.size() - 1; i += 1 ) {
            game_world[ghost_path.get(i).get_x_coord()][ghost_path.get(i).get_y_coord()] = Tileset.GRASS;
        }
    }

    private void generate_journey_through_corridor(Corridor_v2 connecting_corridor, Position_v2 corridor_join_position) {
        if (corridor_join_position.equals(connecting_corridor.get_first_position())) {
            for (int x = 1; x < connecting_corridor.get_corridor_positions_list().size(); x += 1) {
                path_to_player.add(connecting_corridor.get_corridor_positions_list().get(x));
            }
        }
        else if (corridor_join_position.equals(connecting_corridor.get_last_position())) {
            for (int x = connecting_corridor.get_corridor_positions_list().size() - 2; x >= 0; x -= 1) {
                path_to_player.add(connecting_corridor.get_corridor_positions_list().get(x));
            }
        }
    }
    private boolean avatar_and_ghost_in_rooms(Game_Properties game_status){
        boolean avatar_in_room = position_in_any_room(game_status.get_avatar().get_position(), game_status);
        boolean ghost_in_room = position_in_any_room(game_status.get_ghost().get_position(), game_status);
        return avatar_in_room && ghost_in_room;
    }

    private boolean position_in_any_room(Position_v2 checked_position, Game_Properties game_status) {
        for (Corridor_v2 corridor: game_status.get_corridor_list()) {
            if (corridor.get_corridor_positions_list().contains(checked_position)) return false;
        }
        return true;
    }

    private Position_v2 get_head_of_search_journey(){
        if (path_to_player.size() == 0) {
            return this.get_position();
        }
        else return path_to_player.get(path_to_player.size() - 1);
    }

    private Position_v2 try_alternative_next_steps (Position_v2 start_position){
        Position_v2 N = new Position_v2(start_position.get_x_coord(), start_position.get_y_coord() + 1);
        Position_v2 S = new Position_v2(start_position.get_x_coord(), start_position.get_y_coord() - 1);
        Position_v2 W = new Position_v2(start_position.get_x_coord() - 1, start_position.get_y_coord());
        Position_v2 E = new Position_v2(start_position.get_x_coord() + 1, start_position.get_y_coord());
        Position_v2 NW = new Position_v2(start_position.get_x_coord() - 1, start_position.get_y_coord() + 1);
        Position_v2 NE = new Position_v2(start_position.get_x_coord() + 1, start_position.get_y_coord() + 1);
        Position_v2 SE = new Position_v2(start_position.get_x_coord() + 1, start_position.get_y_coord() - 1);
        Position_v2 SW = new Position_v2(start_position.get_x_coord() - 1, start_position.get_y_coord() - 1);

        if (!N.is_it_wall(game_world) && !path_to_player.contains(N) && !is_this_last_visited_position(N)) return N;
        else if (!S.is_it_wall(game_world) && !path_to_player.contains(S) && !is_this_last_visited_position(S)) return S;
        else if (!W.is_it_wall(game_world) && !path_to_player.contains(W) && !is_this_last_visited_position(W)) return W;
        else if (!E.is_it_wall(game_world) && !path_to_player.contains(E) && !is_this_last_visited_position(E)) return E;
        else if (!NW.is_it_wall(game_world) && !path_to_player.contains(NW) && !is_this_last_visited_position(NW)) return NW;
        else if (!NE.is_it_wall(game_world) && !path_to_player.contains(NE) && !is_this_last_visited_position(NE)) return NE;
        else if (!SE.is_it_wall(game_world) && !path_to_player.contains(SE) && !is_this_last_visited_position(SE)) return SE;
        else if (!SW.is_it_wall(game_world) && !path_to_player.contains(SW) && !is_this_last_visited_position(SW)) return SW;
        else return null;
    }

    private boolean is_this_last_visited_position(Position_v2 checked_position) {
        return checked_position.equals(last_position);
    }

    public void kill_ghost(Avatar player_avatar){
        ghost_active = false;
        player_avatar.draw();

    }

    public boolean is_ghost_active(){
        return ghost_active;
    }
}
