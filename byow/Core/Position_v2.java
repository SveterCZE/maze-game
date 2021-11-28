package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Position_v2 {
    int x_coord;
    int y_coord;
    private static final int WIDTH = 155;
    private static final int HEIGHT = 80;

    public Position_v2(int x, int y) {
        this.x_coord = x;
        this.y_coord = y;
    }

    public int get_x_coord() {
        return x_coord;
    }

    public int get_y_coord() {
        return y_coord;
    }

    public boolean isValid() {
        // top left corner check
        if (get_x_coord() < 0) return false;
        // top right corer check
        if (get_x_coord() > (WIDTH - 1)) return false;
        // bottom left corner check
        if (get_y_coord() < 0) return false;
        // bottom right corner check
        if (get_y_coord() > (HEIGHT - 1)) return false;
        return true;
    }

    public boolean is_it_wall(TETile[][] world){
        return world[this.get_x_coord()][this.get_y_coord()] == Tileset.WALL;
    }

    public static Position_v2 get_next_step_position(int build_direction, Position_v2 start_position){
        if (build_direction == 0) {
            return new Position_v2(start_position.get_x_coord(), start_position.get_y_coord() + 1);
        }
        else if (build_direction == 1) {
            return new Position_v2(start_position.get_x_coord(), start_position.get_y_coord() - 1);
        }
        else if (build_direction == 2) {
            return new Position_v2(start_position.get_x_coord() - 1, start_position.get_y_coord());
        }
        else {
            return new Position_v2(start_position.get_x_coord() + 1, start_position.get_y_coord());
        }
    }

    public static List<Position_v2> get_neighbouring_positions(Position_v2 checked_position) {
        List<Position_v2> neighbouring_position_list = new LinkedList<>();
        // NW
        Position_v2 NW = new Position_v2(checked_position.get_x_coord() - 1, checked_position.get_y_coord() + 1);
        if (NW.isValid()) neighbouring_position_list.add(NW);
        // N
        Position_v2 N = new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() + 1);
        if (N.isValid()) neighbouring_position_list.add(N);
        // NE
        Position_v2 NE = new Position_v2(checked_position.get_x_coord() + 1, checked_position.get_y_coord() + 1);
        if (NE.isValid()) neighbouring_position_list.add(NE);
        // W
        Position_v2 W = new Position_v2(checked_position.get_x_coord() - 1, checked_position.get_y_coord());
        if (W.isValid()) neighbouring_position_list.add(W);
        // E
        Position_v2 E = new Position_v2(checked_position.get_x_coord() + 1, checked_position.get_y_coord());
        if (E.isValid()) neighbouring_position_list.add(E);
        // SW
        Position_v2 SW = new Position_v2(checked_position.get_x_coord() - 1, checked_position.get_y_coord() - 1);
        if (SW.isValid()) neighbouring_position_list.add(SW);
        // S
        Position_v2 S = new Position_v2(checked_position.get_x_coord(), checked_position.get_y_coord() - 1);
        if (S.isValid()) neighbouring_position_list.add(S);
        // SE
        Position_v2 SE = new Position_v2(checked_position.get_x_coord() + 1, checked_position.get_y_coord() - 1);
        if (SE.isValid()) neighbouring_position_list.add(SE);
        return neighbouring_position_list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.get_x_coord(), this.get_y_coord());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Position_v2 other = (Position_v2) obj;
        if ((this.get_x_coord() == other.get_x_coord())  &&  (this.get_y_coord() == other.get_y_coord())) return true;
        else return false;
    }

}
