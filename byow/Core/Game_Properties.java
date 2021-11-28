package byow.Core;

import java.util.*;

public class Game_Properties {
    int health;
    String level;
    int x_coord_mouse;
    int y_coord_mouse;
    boolean position_changed;
    List<Game_Item> inventory;
    List<Room_v2> room_list;
    List<Corridor_v2> corridor_list;
    HashSet<Position_v2> poison_positions;
    HashSet<Position_v2> medkit_positions;
    HashSet<Position_v2> radioactivity_positions;
    HashMap<Position_v2, Marker> marker_positions;
    HashMap<Position_v2, World_Map> map_positions;
    HashMap<Position_v2, Potion> potion_positions;
    HashSet<Position_v2> occupied_positions;
    Avatar player_avatar;
    Ghost ghost;
    Graph room_graph;
    boolean radioactive;
    boolean marker_equipped;
    boolean fog_of_war;

    public Game_Properties(String seed){
        health = 100;
        level = seed;
        x_coord_mouse = (int) StdDraw.mouseX();
        y_coord_mouse = (int) StdDraw.mouseY();
        position_changed = true;
        inventory = new LinkedList<>();
        poison_positions = new HashSet<>();
        medkit_positions = new HashSet<>();
        occupied_positions = new HashSet<>();
        marker_positions = new HashMap<>();
        map_positions = new HashMap<>();
        potion_positions = new HashMap<>();
        radioactivity_positions = new HashSet<>();
        radioactive = false;
        marker_equipped = false;
        fog_of_war = true;
    }

    public String get_level_no(){
        return level;
    }

    public int get_health(){
        return health;
    }

    public void connect_avatar(Avatar new_avatar){
        player_avatar = new_avatar;
    }

    public void connect_ghost(Ghost new_ghost){
        ghost = new_ghost;
    }

    public void connect_room_list(List<Room_v2> imported_room_list){
        room_list = imported_room_list;
    }

    public void connect_room_graph(Graph imported_room_graph) {
        room_graph = imported_room_graph;
    }

    public void connect_corridors(List<Corridor_v2> connected_corridors){
        corridor_list = connected_corridors;
    }

    public Avatar get_avatar(){
        return player_avatar;
    }

    public Ghost get_ghost(){
        return ghost;
    }

    public Graph get_room_graph(){
        return room_graph;
    }

    public List<Corridor_v2> get_corridor_list() {
        return corridor_list;
    }

    public List<Room_v2> get_room_list(){
        return room_list;
    }

    public void update_mouse_coordinates(){
        int temp_x_coord_mouse = (int) StdDraw.mouseX();
        int temp_y_coord_mouse = (int) StdDraw.mouseY();
        if ((temp_x_coord_mouse == x_coord_mouse) && (temp_y_coord_mouse == y_coord_mouse)) {
            position_changed = false;
        }
        else {
            x_coord_mouse = temp_x_coord_mouse;
            y_coord_mouse = temp_y_coord_mouse;
            position_changed = true;
        }
    }

    public int get_x_coord() {
        return x_coord_mouse;
    }

    public int get_y_coord() {
        return y_coord_mouse;
    }

    public boolean has_mouse_moved() {
        return position_changed;
    }

    public void add_to_inventory(Game_Item new_inventory_object){
        inventory.add(new_inventory_object);
    }

    public List<Game_Item> get_inventory(){
        return inventory;
    }

    public Set<String> get_inventory_content(){
        Set<String> inventory_content = new HashSet<>();
        for (Game_Item inventory_item: get_inventory()) {
            inventory_content.add(inventory_item.get_item_name());
        }
        return inventory_content;
    }

    public String get_inventory_content_as_string(){
        StringBuilder inventory_string = new StringBuilder();
        for (String item_string: get_inventory_content()){
            inventory_string.append(item_string).append(", ");
        }
        return inventory_string.toString();
    }

    public void add_occupied_position(Position_v2 added_position) {
        occupied_positions.add(added_position);
    }

    public boolean is_position_occupied(Position_v2 checked_position) {
        return occupied_positions.contains(checked_position);
    }

    public void add_poision_position(Position_v2 added_position) {
        poison_positions.add(added_position);
        occupied_positions.add(added_position);
    }

    public void add_medkit_position(Position_v2 added_position) {
        medkit_positions.add(added_position);
        occupied_positions.add(added_position);
    }

    public void add_radioactivity_position(Position_v2 added_position) {
        radioactivity_positions.add(added_position);
        occupied_positions.add(added_position);
    }

    public void add_marker_position(Position_v2 added_position, Marker added_marker) {
        marker_positions.put(added_position, added_marker);
        occupied_positions.add(added_position);
    }

    public void add_world_map_position(Position_v2 added_position, World_Map added_map) {
        map_positions.put(added_position, added_map);
        occupied_positions.add(added_position);
    }

    public void add_potion_position(Position_v2 added_position, Potion added_potion) {
        potion_positions.put(added_position, added_potion);
        occupied_positions.add(added_position);
    }

    public boolean is_position_poison(Position_v2 checked_position) {
        boolean answer = poison_positions.contains(checked_position);
        if (answer) poison_positions.remove(checked_position);
        return answer;
    }

    public boolean is_position_medkit(Position_v2 checked_position) {
        boolean answer = medkit_positions.contains(checked_position);
        if (answer) medkit_positions.remove(checked_position);
        return answer;
    }

    public boolean is_position_radioactive(Position_v2 checked_position) {
        boolean answer = radioactivity_positions.contains(checked_position);
        if (answer) radioactivity_positions.remove(checked_position);
        return answer;
    }

    public boolean is_position_marker(Position_v2 checked_position) {
        boolean answer = marker_positions.containsKey(checked_position);
        return answer;
    }

    public Marker get_marker_by_coordinate(Position_v2 checked_position) {
        return marker_positions.get(checked_position);
    }

    public void remove_marker(Position_v2 removed_position) {
        marker_positions.remove(removed_position);
    }

    public boolean is_position_world_map(Position_v2 checked_position) {
        boolean answer = map_positions.containsKey(checked_position);
        return answer;
    }

    public World_Map get_world_map_by_coordinate(Position_v2 checked_position) {
        return map_positions.get(checked_position);
    }

    public boolean is_position_potion(Position_v2 checked_position){
        boolean answer = potion_positions.containsKey(checked_position);
        return answer;
    }

    public Potion get_potion_by_coordinate(Position_v2 checked_position) {
        return potion_positions.get(checked_position);
    }

    public void remove_potion(Position_v2 checked_position){
        potion_positions.remove(checked_position);
    }

    public boolean has_potion_in_inventory(){
        for (Game_Item inventory_piece: inventory) {
            if (inventory_piece instanceof Potion) return true;
        }
        return false;
    }

    public void remove_potion_from_inventory(){
        inventory.removeIf(inventory_piece -> inventory_piece instanceof Potion);
    }

    public void remove_world_map(Position_v2 removed_position) {
        map_positions.remove(removed_position);
    }

    public void poison_player(){
        health -= 35;
    }

    public void kill_player(){
        health = 0;
    }

    public void make_player_radioactive() {
        health -= 24;
        radioactive = true;
    }

    public void radioactivity_effects() {
        if (radioactive) health -= 1;
    }

    public void heal_player(){
        if (radioactive) radioactive = false;
        else {
            health += 30;
            if (health > 100) health = 100;
        }
    }

    public boolean is_player_radioactive(){
        return radioactive;
    }

    public void equip_marker(){
        marker_equipped = true;
    }

    public boolean is_marker_equipped(){
        return marker_equipped;
    }

    public void disable_fog_of_war(){
        fog_of_war = false;
    }

    public boolean get_fog_of_war() {
        return fog_of_war;
    }

    public Corridor_v2 find_corridor_connecting_rooms(Room_v2 current_room, Room_v2 next_room) {
        Corridor_v2 connecting_corridor = null;
        for (Corridor_v2 checked_corridor: corridor_list){
            Room_v2 start_room = checked_corridor.get_start_room();
            Room_v2 end_room = checked_corridor.get_end_room();
            if (start_room.equals(current_room) && end_room.equals(next_room)) connecting_corridor = checked_corridor;
            else if (start_room.equals(next_room) && end_room.equals(current_room)) connecting_corridor = checked_corridor;
        }
        return connecting_corridor;
    }
}
