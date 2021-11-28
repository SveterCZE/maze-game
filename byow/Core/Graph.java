package byow.Core;

import java.util.*;

public class Graph {

    Map<Room_v2, List<Room_v2>> graph_database;

    public Graph (List<Room_v2> added_rooms, List<Corridor_v2> added_corridors) {
        graph_database = new HashMap<>(added_rooms.size());
        import_rooms(added_rooms);
        import_vertices(added_corridors);
    }

    private void import_rooms(List<Room_v2> added_rooms) {
        for (Room_v2 room: added_rooms) add_edge(room);
    }

    private void add_edge (Room_v2 added_room) {
        graph_database.put(added_room, new LinkedList<Room_v2>());
    }

    private void import_vertices(List<Corridor_v2> added_corridors) {
        for (Corridor_v2 corridor: added_corridors) add_corridor(corridor);
    }

    private void add_corridor(Corridor_v2 added_corridor){
        Room_v2 start_room = added_corridor.get_start_room();
        Room_v2 end_room = added_corridor.get_end_room();

        // Add the connections to the database
        List<Room_v2> temp_db_start = graph_database.get(start_room);
        temp_db_start.add(end_room);

        List<Room_v2> temp_db_end = graph_database.get(end_room);
        temp_db_end.add(start_room);
    }

    public List<Room_v2> find_connection_rooms (Room_v2 start_room, Room_v2 end_room) {
        List<Room_v2> room_connection = new LinkedList<>();
        List<Room_v2> final_connections = new LinkedList<>();
        find_connection_rooms_recursive(start_room, end_room, room_connection, final_connections);
        return final_connections;
    }

    private void find_connection_rooms_recursive(Room_v2 start_room, Room_v2 end_room, List<Room_v2> room_connection, List<Room_v2> final_connections) {
        // BASE CASE -- The rooms are the same
        if (start_room.equals(end_room)) {
            room_connection.add(start_room);
            clone_list(room_connection, final_connections);
        }
        // RECURSIVE CASE -- The rooms are different
        else {
            room_connection.add(start_room);
            List<Room_v2> rooms_connected_to_start = graph_database.get(start_room);
            for (Room_v2 checked_room: rooms_connected_to_start) {
                // Check that the room is not in the list of visited rooms
                if (!room_connection.contains(checked_room)) {
                    List<Room_v2> temp_room_connection = clone_list(room_connection);
                    find_connection_rooms_recursive(checked_room, end_room, temp_room_connection, final_connections);
                }
            }
        }
    }

    private List<Room_v2> clone_list (List<Room_v2> original_list) {
        List<Room_v2> cloned_list = new LinkedList<>();
        for (Room_v2 room: original_list) {
            cloned_list.add(room);
        }
        return cloned_list;
    }

    private void clone_list (List<Room_v2> original_list, List<Room_v2> destination_list) {
        for (Room_v2 room: original_list) {
            destination_list.add(room);
        }
    }




}
