package byow.Core;

public class UnionFind {

    // TODO - Add instance variables?
    static int[] parents_list;
    static int[] sizes_list;
    static int list_length;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        // TODO
        parents_list = new int[n];
        sizes_list = new int[n];
        list_length = n;
        for (int i = 0; i < n; i++) {
            parents_list[i] = -1;
            sizes_list[i] = 1;
        }
    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // TODO
        if (vertex < 0 || vertex >= list_length) {
            throw new IllegalArgumentException();
        }
        return;
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        // TODO
        return sizes_list[find(v1)];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // TODO
        int parent_id = parents_list[v1];
        if (parent_id == -1) {
            return -sizes_list[parent_id];
        }
        else {
            return parents_list[v1];
        }
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO
        if (find(v1) == find(v2)) {
            return true;
        }
        return false;
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // TODO
        if (sizeOf(v1) >= sizeOf(v2)) {
            if (!connected(v1, v2)) {
                sizes_list[find(v1)] += sizeOf(v2);
            }
            parents_list[find(v2)] = find(v1);
//            parents_list[v2] = v1;

        }
        else {
            if (!connected(v1, v2)) {
                sizes_list[find(v2)] += sizeOf(v1);
            }
            parents_list[find(v1)] = find(v2);
//            parents_list[v1] = v2;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        // TODO - IMPLEMENT PATH COMPRESSION
        if (parents_list[vertex] < 0) {
            return vertex;
        }
        else {
            // int ultimate_parent = find(parents_list[vertex]);
            // Check if this works - if I put everything under the ultimate parent, the size of other leaves shall be zero
            // sizes_list[vertex] = 1;

//            New attempt
            int ultimate_parent = find(parent(vertex));
            parents_list[vertex] = ultimate_parent;
            return ultimate_parent;

            // Original
//            return find(parent(vertex));
        }
    }

    public boolean is_fully_connected(){
        if (this.sizeOf(0) == list_length) {
            return true;
        }
        else return false;
    }

}
