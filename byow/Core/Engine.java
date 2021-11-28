package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

//import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 155;
    public static final int HEIGHT = 80;

    int intro_screen_width = 40;
    int intro_screen_height = 40;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        StdDraw.setCanvasSize(intro_screen_width * 16, intro_screen_height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, intro_screen_width);
        StdDraw.setYscale(0, intro_screen_height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        String output_string = drawMenu();
        StdDraw.clear();
        if (output_string != null) {
            interactWithInputString(output_string);
        }
        return;
    }

    public String drawMenu() {
        int midWidth = intro_screen_width / 2;
        int midHeight = intro_screen_height / 2;

        while (true) {
            StdDraw.clear();
            StdDraw.clear(Color.black);

            // Draw the actual text
            Font bigFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight + 2, "(N)EW GAME");
            StdDraw.text(midWidth, midHeight, "(L)OAD GAME");
            StdDraw.text(midWidth, midHeight - 2, "(Q)UIT GAME");
            StdDraw.show();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (String.valueOf(key).equals("n") || String.valueOf(key).equals("N")){
                return drawInputPrompt();
            }
            else if (String.valueOf(key).equals("l") || String.valueOf(key).equals("L")){
                try {
                    File myObj = new File("output.txt");
                    Scanner myReader = new Scanner(myObj);
                    String data = myReader.nextLine();
                    myReader.close();
                    Engine engine = new Engine();
                    engine.interactWithInputString(data);
                    System.exit(0);

                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred. File not found.");
                }
            }
            else if (String.valueOf(key).equals("q") || String.valueOf(key).equals("Q")){
                System.exit(0);
            }
            StdDraw.pause(100);
        }
    }

    public String drawInputPrompt() {
        int midWidth = intro_screen_width / 2;
        int midHeight = intro_screen_height / 2;
        String input = "";
        while(true) {
            StdDraw.clear();
            StdDraw.clear(Color.black);
            Font bigFont = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(bigFont);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(midWidth, midHeight + 2, "ENTER THE SEED");
            StdDraw.text(midWidth, midHeight, "YOUR SEED: " + input);
            StdDraw.show();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            String temp_key_string = String.valueOf(key);
            if (isNumeric(temp_key_string)) input += String.valueOf(key);
            else if ((temp_key_string.equals("S") || temp_key_string.equals("s")) && input.length() != 0) {
                StdDraw.clear();
                break;
            }
            StdDraw.pause(100);
        }
        return ("N" + input + "S");
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        String seed = exctract_seed(input);
        Game_Properties game_status = new Game_Properties(seed);
        String keys_pressed = "";
        TETile[][] world = initialise_tiles();

        // Create list of rooms
        List<Room_v2> room_list = new LinkedList<>();
        // Crate a list of items creating borders
        Set<Position_v2> border_items_set = new HashSet<>();

        // Create list of corridors
        List<Corridor_v2> corridor_list = new LinkedList<>();

        // Generate and draw random rooms
        Random rand_gen = new Random(convert_seed_to_long(seed));
        generate_random_rooms(world, room_list, border_items_set, rand_gen);
        game_status.connect_room_list(room_list);

        // Create a union data structures - can be done only after the list of rooms is created
        UnionFind room_union = new UnionFind(room_list.size());

        //  Generate corridors and connect them to the game status class
        generate_random_corridors(world, room_list, border_items_set, rand_gen, room_union, corridor_list);
        game_status.connect_corridors(corridor_list);

        // Create the graph and connect it to the game status class
        Graph room_graph = new Graph(room_list, corridor_list);
        game_status.connect_room_graph(room_graph);

        // Generate the door
        MagicDoor exit_door = generate_exit_door(world, room_list, rand_gen);

        // Generate the player avatar
        Avatar player_avatar = generate_player_avatar(world, room_list, game_status, rand_gen);

        // Generate the key
        Door_Key door_key = generate_door_keys(world, room_list, game_status, rand_gen);

        // Generate the Poison
        generate_poison(world, room_list, game_status, rand_gen);

        // Generate the Radioactivity
        generate_radioactivity(world, room_list, game_status, rand_gen);

        // Generate the First Aid Kits
        generate_first_aid(world, room_list, game_status, rand_gen);

        // Generate markers
        generate_markers(world, room_list, game_status, rand_gen);

        // Generate world maps
        generate_world_maps(world, room_list, game_status, rand_gen);

        // Generate the anti-ghost potions
        generate_potions(world, room_list, game_status, rand_gen);

        // Generate the ghost
        generate_ghost(world, room_list, game_status, rand_gen);

        // Extract movement_instructions
        extract_movement_instruction(input, player_avatar, game_status, door_key, exit_door);

        // Render the world
        ter.renderFrame(world, game_status);

        // Collect the input and take corresponding actions
        while (true) {
            game_status.update_mouse_coordinates();
            if (!StdDraw.hasNextKeyTyped()) {
                if (game_status.has_mouse_moved()) ter.renderFrame(world, game_status);
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            // In case of exit, save the current progress into the file
            if (String.valueOf(key).equals("q") || String.valueOf(key).equals("Q")){
                quit_and_save(input, keys_pressed);
                break;
            }
            else {
                keys_pressed = take_player_action(player_avatar, game_status, door_key, exit_door, keys_pressed, key);
            }
            StdDraw.pause(5);
            ter.renderFrame(world, game_status);
            // If health is 0, you died
            if (game_status.get_health() <= 0) {
                render_death(world, game_status, seed);
            }

            // If the door was found, generate the next level
            if (player_avatar.get_position().equals(exit_door.get_position()) && exit_door.isDoor_open()) {
                render_next_level(world, game_status, seed);
            }
        }
        return world;
    }

    private String exctract_seed(String input) {
        StringBuilder seed_build = new StringBuilder();
        for (int i = 1; i < input.length(); i += 1) {
            Boolean flag = Character.isDigit(input.charAt(i));
            if (flag) seed_build.append(input.charAt(i));
            else break;
        }
        return seed_build.toString();
    }

    private List<String> extract_moveset(String[] split_input){
        List<String> move_instruction = new LinkedList<>();
        boolean found_end_initial_seed = false;
        for (int i = 0; i < split_input.length; i += 1){
            if (found_end_initial_seed) {
                move_instruction.add(split_input[i]);
            }
            else if (split_input[i].equals("S") || split_input[i].equals("s")) {
                found_end_initial_seed = true;
            }
        }
        return move_instruction;
    }

    private TETile[][] initialise_tiles() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 3);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }

    private void check_player_actions(Avatar player_avatar, Game_Properties game_status, Door_Key door_key, MagicDoor exit_door) {
        // Check if the avatar is on the same item as key. If so, open the door and add it to inventory.
        if (player_avatar.get_position().equals(door_key.get_position())) {
            exit_door.set_door_open();
            game_status.add_to_inventory(door_key);
        }
        // Check if the player was killed by the Ghost
        check_if_killed_by_ghost(player_avatar, game_status);

        // Check if the player is on the poison
        if (game_status.is_position_poison(player_avatar.get_position())){
            game_status.poison_player();
        }
        // Check if the player is on the medkit
        else if (game_status.is_position_medkit(player_avatar.get_position())) {
            game_status.heal_player();
        }
        // Check if the player is on radioactive ground
        else if (game_status.is_position_radioactive(player_avatar.get_position())) {
            game_status.make_player_radioactive();
        }
        // Check if the player found a marker
        else if (game_status.is_position_marker(player_avatar.get_position())) {
            game_status.equip_marker();
            game_status.add_to_inventory(game_status.get_marker_by_coordinate(player_avatar.get_position()));
            game_status.remove_marker(player_avatar.get_position());
        }
        // Check if the player found a world map
        else if (game_status.is_position_world_map(player_avatar.get_position())) {
            game_status.disable_fog_of_war();
            game_status.add_to_inventory(game_status.get_world_map_by_coordinate(player_avatar.get_position()));
            game_status.remove_world_map(player_avatar.get_position());
        }

        // Check if the player found a potion
        else if (game_status.is_position_potion(player_avatar.get_position())) {
            game_status.add_to_inventory(game_status.get_potion_by_coordinate(player_avatar.get_position()));
            game_status.remove_potion(player_avatar.get_position());
        }

        // Take effects of radioactivity
        game_status.radioactivity_effects();
    }

    private void generate_random_rooms(TETile[][] world, List<Room_v2> room_list, Set<Position_v2> border_items_set, Random rand_gen){
        // Generate the rooms
        for (int i = 0; i < 300; i+= 1){
            Room_v2 random_room = new Room_v2(new Position_v2(RandomUtils.uniform(rand_gen, WIDTH), RandomUtils.uniform(rand_gen, HEIGHT)), RandomUtils.uniform(rand_gen, 4,15), RandomUtils.uniform(rand_gen,4, 15), world);
            if (random_room.is_valid == true) {
                boolean conflict = false;
                for (Room_v2 x: room_list) {
                    if (Room_v2.check_no_room_overlap(x, random_room) == false)
                    {
                        conflict = true;
                        break;
                    }
                }
                if (conflict == false) {
                    if (Room_v2.rooms_do_not_cross(random_room, border_items_set) == false) {
                        conflict = true;
                        break;
                    }
                }
                if (conflict == false) {
                    room_list.add(random_room);
                }
            }
        }
        // Draw the rooms
        draw_rooms(room_list);
    }

    private void draw_rooms(List<Room_v2> room_list){
        for (Room_v2 x: room_list) x.draw_room();
    }

    private void generate_random_corridors(TETile[][] world, List<Room_v2> room_list, Set<Position_v2> border_items_set, Random rand_gen, UnionFind room_union, List<Corridor_v2> corridor_list){
        while (room_union.is_fully_connected() == false) {
            // Select random room
            Room_v2 random_room = room_list.get(RandomUtils.uniform(rand_gen, room_list.size()));
            // Select direction of corridor
            // 0 - Up
            // 1 - Down
            // 2 - Left
            // 3 - Right
            int direction = RandomUtils.uniform(rand_gen, 4);
            Position_v2 start_position = random_room.get_random_corridor_start(direction, rand_gen);
            if (start_position != null) {
                Corridor_v2 random_corridor = new Corridor_v2(start_position, direction, world, border_items_set, room_list, rand_gen, room_union);
                random_corridor.build();
                if (random_corridor.is_buildable()) {
                    random_corridor.draw();
                    corridor_list.add(random_corridor);
                }
            }
        }
    }

    private MagicDoor generate_exit_door(TETile[][] world, List<Room_v2> room_list, Random rand_gen){
        MagicDoor exit_door = new MagicDoor(world, rand_gen, room_list);
        exit_door.draw();
        return exit_door;
    }

    private Avatar generate_player_avatar(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen){
        Avatar player_avatar = new Avatar(world, rand_gen, room_list, game_status);
        game_status.connect_avatar(player_avatar);
        game_status.add_occupied_position(player_avatar.get_position());
        player_avatar.draw();
        return player_avatar;
    }

    private Door_Key generate_door_keys(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen){
        Door_Key door_key = new Door_Key(world, rand_gen, room_list);
        while (game_status.is_position_occupied(door_key.get_position())) {
            door_key = new Door_Key(world, rand_gen, room_list);
        }
        game_status.add_occupied_position(door_key.get_position());
        door_key.draw();
        return door_key;
    }

    private void generate_poison(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen){
        for (int i = 0; i < RandomUtils.uniform(rand_gen, room_list.size() * 3); i += 1) {
            Poison game_poison = new Poison(world, rand_gen, room_list);
            while(game_status.is_position_occupied(game_poison.get_position())) {
                game_poison = new Poison(world, rand_gen, room_list);
            }
            game_status.add_poision_position(game_poison.get_position());
            game_poison.draw();
        }
    }

    private void generate_radioactivity(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen){
        for (int i = 0; i < RandomUtils.uniform(rand_gen, room_list.size() * 3); i += 1) {
            Radioactivity game_radioactivity = new Radioactivity(world, rand_gen, room_list);
            while(game_status.is_position_occupied(game_radioactivity.get_position())) {
                game_radioactivity = new Radioactivity(world, rand_gen, room_list);
            }
            game_status.add_radioactivity_position(game_radioactivity.get_position());
            game_radioactivity.draw();
        }

    }

    private void generate_first_aid(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen) {
        for (int i = 0; i < RandomUtils.uniform(rand_gen, room_list.size() * 3); i += 1) {
            MedKit game_medkit = new MedKit(world, rand_gen, room_list);
            while(game_status.is_position_occupied(game_medkit.get_position())) {
                game_medkit = new MedKit(world, rand_gen, room_list);
            }
            game_status.add_medkit_position(game_medkit.get_position());
            game_medkit.draw();
        }
    }

    private void generate_markers(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen) {
        for (int i = 0; i < RandomUtils.uniform(rand_gen, room_list.size() / 5); i += 1) {
            Marker game_marker = new Marker(world, rand_gen, room_list);
            while(game_status.is_position_occupied(game_marker.get_position())) {
                game_marker = new Marker(world, rand_gen, room_list);
            }
            game_status.add_marker_position(game_marker.get_position(), game_marker);
            game_marker.draw();
        }
    }

    private void generate_world_maps(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen) {
        for (int i = 0; i < 8; i += 1) {
            World_Map world_map = new World_Map(world, rand_gen, room_list);
            while (game_status.is_position_occupied(world_map.get_position())) {
                world_map = new World_Map(world, rand_gen, room_list);
            }
            game_status.add_world_map_position(world_map.get_position(), world_map);
            world_map.draw();
        }
    }

    private void generate_potions(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen) {
        for (int i = 0; i < room_list.size() / 5; i += 1) {
            Potion generated_potion = new Potion(world, rand_gen, room_list);
            while (game_status.is_position_occupied(generated_potion.get_position())) {
                generated_potion = new Potion(world, rand_gen, room_list);
            }
            game_status.add_potion_position(generated_potion.get_position(), generated_potion);
            generated_potion.draw();
        }
    }

    private void generate_ghost(TETile[][] world, List<Room_v2> room_list, Game_Properties game_status, Random rand_gen){
        Ghost my_ghost = new Ghost(world, rand_gen, room_list);
        while (game_status.is_position_occupied(my_ghost.get_position())) {
            my_ghost = new Ghost(world, rand_gen, room_list);
        }
        game_status.connect_ghost(my_ghost);
        my_ghost.draw();
    }

    private void extract_movement_instruction(String input, Avatar player_avatar, Game_Properties game_status, Door_Key door_key, MagicDoor exit_door){
        List<String> move_instruction = extract_moveset(input.split(""));
        for (String x: move_instruction) {
            player_avatar.take_action(x);
            game_status.get_ghost().chase_player(game_status);
            check_player_actions(player_avatar, game_status, door_key, exit_door);

        }
    }

    private void quit_and_save(String input, String keys_pressed){
        Path path = Paths.get("output.txt");
        try {
            Files.writeString(path, input + keys_pressed, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            // Handle exception
            System.out.println(ex);
        }
    }

    private String take_player_action(Avatar player_avatar, Game_Properties game_status, Door_Key door_key, MagicDoor exit_door, String keys_pressed, char key){
        player_avatar.take_action(String.valueOf(key));
        check_if_killed_by_ghost(player_avatar, game_status);
        game_status.get_ghost().chase_player(game_status);
        check_player_actions(player_avatar, game_status, door_key, exit_door);
        keys_pressed += String.valueOf(key);
        return keys_pressed;
    }

    private void render_death(TETile[][] world, Game_Properties game_status, String seed) {
        StdDraw.pause(100);
        ter.renderFrame(world, false, game_status);
        StdDraw.pause(1000);
        Engine new_game_engine = new Engine();
        new_game_engine.interactWithInputString("N" + (convert_seed_to_long(seed)) + "S");
        System.exit(0);
    }

    private void render_next_level(TETile[][] world, Game_Properties game_status, String seed){
        StdDraw.pause(100);
        ter.renderFrame(world, true, game_status);
        StdDraw.pause(1000);
        Engine new_game_engine = new Engine();

        new_game_engine.interactWithInputString("N" + increment_seed((convert_seed_to_long(seed))) + "S");
        System.exit(0);
    }

    private void check_if_killed_by_ghost(Avatar player_avatar, Game_Properties game_status){
        if (player_avatar.get_position().equals(game_status.get_ghost().get_position()) && game_status.get_ghost().is_ghost_active()) {
            if (game_status.has_potion_in_inventory()) {
                game_status.remove_potion_from_inventory();
                game_status.get_ghost().kill_ghost(player_avatar);
            }
            else {
                game_status.kill_player();
            }

        }
    }

    private long convert_seed_to_long (String seed_string){
        long seed_as_long;
        try {
            seed_as_long = Long.parseLong(seed_string);
        }
        catch (java.lang.NumberFormatException ex) {
            seed_as_long = 0;
        }
        return seed_as_long;
    }

    private long increment_seed (long current_seed) {
        if (current_seed == Long.parseLong("9223372036854775807")) return 0;
        else return current_seed + 1;
    }



}
