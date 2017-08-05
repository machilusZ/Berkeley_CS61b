import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/** A class that runs Kruskal's algorithm on a Graph. Given a graph G, Kruskal's
 *  algorithm constructs a new graph T such T is a spanning tree of G and the
 *  sum of its edge weights is less than the sum of the edge weights for
 *  every possible spanning tree T* of G. This is called the Minimum Spanning
 *  Tree (MST).
 *
 *  @author
 */
public class Kruskal {

    /** Returns the MST of INPUT using a naive isConnected implementation. */
    public static Graph minSpanTree(Graph input) {
        // TODO implement!
        Graph result = new Graph();
        for (int vertice: input.getAllVertices()) {
            result.addVertex(vertice);
        }
        for (Edge e: input.getAllEdges()) {
            if (!isConnected(result, e.getSource(), e.getDest())) {
                result.addEdge(e);
            }
        }
        return result;
    }

    /** Returns the MST of INPUT using the Union Find datastructure. */
    public static Graph minSpanTreeFast(Graph input) {
        // TODO implement!
        Graph result = new Graph();
        TreeSet<Integer> vertices = input.getAllVertices();
        UnionFind sets = new UnionFind(vertices.size());
        for (int vertice: vertices) {
            result.addVertex(vertice);
        }
        for (Edge e: input.getAllEdges()) {
            int v1 = e.getSource();
            int v2 = e.getDest();
            if (!sets.isConnected(v1, v2)) {
                sets.union(v1, v2);
                result.addEdge(e);
            }
        }
        return result;
    }

    /** A naive implementation of BFS to check if two nodes are connected. */
    public static boolean isConnected(Graph g, int v1, int v2) {
        // TODO implement!
        if (!g.containsVertex(v1) || !g.containsVertex(v2)) {
            return false;
        }
        LinkedList<Integer> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        fringe.add(v1);
        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            TreeSet<Integer> neighbors = g.getNeighbors(v);
            if (neighbors.contains(v2)) {
                return true;
            }
            if (! visited.contains(v)) {
                visited.add(v);
                for (int neighbor : neighbors) {
                    fringe.add(neighbor);
                }
            }
        }
        return false;
    }
}

