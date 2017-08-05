import java.lang.Math;

/** A simple implementation of the UnionFind abstract data structure with path
 *  compression. This UnionFind structure only holds integer and there are two
 *  critical operations: union and find. When unioning two elements, the element
 *  contained in a tree of smaller size is placed as a subtree to the root
 *  vertex of the larger tree. Meanwhile finding an element implements path
 *  compression. When a vertex an element is traversed, it is automatically
 *   connected to the root of that tree.
 *
 *  Using the union find data structure allows for a fast implementation of
 *  Kruskal's algorithm as well as other set based operations.
 *
 *  @author
 *  @since
 */
public class UnionFind {

    /** Instance variables go here? */
    int[] djs;
    int capacity;


    /** Returns a UnionFind data structure holding N vertices. Initially, all
     *  vertices are in disjoint sets. */
    public UnionFind(int n) {
        // TODO implement
        capacity = n + 1;
        djs = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            djs[i] = -1;
        }
    }

    /** Returns the size of the set V1 belongs to. */
    public int sizeOf(int v1) {
        // TODO implement
        return -djs[find(v1)];
    }

    /** Returns true if nodes V1 and V2 are connected. */
    public boolean isConnected(int v1, int v2) {
        // TODO implement
        return find(v1) == find(v2);
    }

    /** Remember that each disjoint set is represented as a tree. Find returns
     *  the root of the set VERTEX belongs to. Path-compression, where the
     *  vertices along the search path from VERTEX to its root are linked
     *  directly to the root, is employed allowing for fast search-time. */
    public int find(int vertex) {
        // TODO implement
        if (vertex < 0 || vertex >= capacity) {
            throw new IllegalArgumentException();
        }
        if (djs[vertex] < 0) {
            return vertex;
        }
        int parent = djs[vertex];
        if (djs[parent] >= 0) {
            djs[vertex] = find(parent);
        }
        return djs[vertex];
    }

    /** Connects two elements V1 and V2 together in the UnionFind structure. V1
     *  and V2 can be any element and a union-by-size heurisitic is used. */
    public void union(int v1, int v2) {
        // TODO implement
        if (!isConnected(v1, v2)) {
            int longerOne = sizeOf(v1) > sizeOf(v2) ? v1: v2;
            int shorterOne = longerOne == v1 ? v2: v1;
            djs[find(longerOne)] -= sizeOf(shorterOne);
            djs[find(shorterOne)] = find(longerOne);
        }
        /*int reprMax = djs[v1] >= djs[v2] ? v1: v2;
        int reprMin = reprMax == v1 ? v2: v1;
        if (djs[reprMax] < 0) {
            djs[reprMin] -= sizeOf(reprMax);
            djs[find(reprMax)] = reprMin;
        } else
        if (djs[reprMin] >= 0) {
            int longerOne = sizeOf(v1) > sizeOf(v2) ? v1: v2;
            int shorterOne = longerOne == v1 ? v2: v1;
            djs[find(longerOne)] -= sizeOf(shorterOne);
            djs[find(shorterOne)] = find(longerOne);
        } else {
            djs[reprMin] -= sizeOf(reprMax);
            djs[find(reprMax)] = reprMin;
        }*/
    }
}

