import jh61b.junit.In;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.*;

public class Graph implements Iterable<Integer>{

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;
    private static final int MAX = 1000;

    // Initialize a graph with the given number of vertices and no edges.
    public Graph(int numVertices) {
        adjLists = new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    // Add to the graph a directed edge from vertex v1 to vertex v2.
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, null);
    }

    // Add to the graph an undirected edge from vertex v1 to vertex v2.
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, null);
    }

    // Add to the graph a directed edge from vertex v1 to vertex v2,
    // with the given edge information.
    public void addEdge(int v1, int v2, Object edgeInfo) {
        //your code here
        adjLists[v1].add(new Edge(v1, v2, edgeInfo));
    }

    // Add to the graph an undirected edge from vertex v1 to vertex v2,
    // with the given edge information.
    public void addUndirectedEdge(int v1, int v2, Object edgeInfo) {
        //your code here
        addEdge(v1, v2, edgeInfo);
        addEdge(v2, v1, edgeInfo);
    }

    // Return true if there is an edge from vertex "from" to vertex "to";
    // return false otherwise.
    public boolean isAdjacent(int from, int to) {
        //your code here
        Iterator<Edge> adjIterator = adjLists[from].iterator();
        while (adjIterator.hasNext()) {
            Edge curEdge = adjIterator.next();
            if (curEdge.to.equals(to)) {
                return true;
            }
        }
        return false;
    }

    // Returns a list of all the neighboring  vertices 'u'
    // such that the edge (VERTEX, 'u') exists in this graph.
    public List neighbors(int vertex) {
        // your code here
        List result = new LinkedList<>();
        Iterator<Edge> neiIterator = adjLists[vertex].iterator();
        while (neiIterator.hasNext()) {
            Edge curEdge = neiIterator.next();
            result.add(curEdge.to);
        }
        return result;
    }

    // Return the number of incoming vertices for the given vertex,
    // i.e. the number of vertices v such that (v, vertex) is an edge.
    public int inDegree(int vertex) {
        int count = 0;
        //your code here
        for (int i = 0; i < vertexCount; i++) {
            if (isAdjacent(i, vertex)) {
                count++;
            }
        }
        return count;
    }

    public Iterator<Integer> iterator(){
        return new TopologicalIterator();
    }

    // A class that iterates through the vertices of this graph, starting with a given vertex.
    // Does not necessarily iterate through all vertices in the graph: if the iteration starts
    // at a vertex v, and there is no path from v to a vertex w, then the iteration will not
    // include w
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            //your code here
            fringe = new Stack<>();
            fringe.push(start);
            visited = new HashSet();
            visited.add(start);
        }

        public boolean hasNext() {
            //your code here
            if (fringe.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        public Integer next() {
            //your code here
                Integer result = fringe.pop();
                Iterator<Edge> dfsIterator = adjLists[result].iterator();
                while (dfsIterator.hasNext()) {
                    Edge e = dfsIterator.next();
                    if (!visited.contains(e.to)) {
                        fringe.push(e.to);
                        visited.add(e.to);
                    }
                }
                visited.add(result);
                return result;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    // Return the collected result of iterating through this graph's
    // vertices as an ArrayList.
    public ArrayList<Integer> visitAll(int startVertex) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(startVertex);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    // Returns true iff there exists a path from STARVETEX to
    // STOPVERTEX. Assumes both STARTVERTEX and STOPVERTEX are
    // in this graph. If STARVERTEX == STOPVERTEX, returns true.
    public boolean pathExists(int startVertex, int stopVertex) {
        // your code here
        if (startVertex == stopVertex) {
            return true;
        }
        ArrayList<Integer> result = visitAll(startVertex);
        for (int i = 0 ; i < result.size(); i++) {
            if (result.get(i) == stopVertex) {
                return true;
            }
        }
        return false;
    }


    // Returns the path from startVertex to stopVertex.
    // If no path exists, returns an empty arrayList.
    // If startVertex == stopVertex, returns a one element arrayList.
    public ArrayList<Integer> path(int startVertex, int stopVertex) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        // you supply the body of this method
        if (startVertex == stopVertex) {
            result.add(startVertex);
            return result;
        } else if (!pathExists(startVertex, stopVertex)) {
            return result;
        } else {
            ArrayList<Integer> visited = new ArrayList<>();
            Iterator<Integer> iter = new DFSIterator(startVertex);
            while (iter.hasNext()) {
                int vertex = iter.next();
                if (vertex == stopVertex) {
                    break;
                }
                visited.add(vertex);
            }
            int end = stopVertex;
            result.add(end);
            for(int i = visited.size() - 1; i >= 0;i--) {
                if(visited.get(i) == startVertex && isAdjacent(startVertex, end)) {
                    result.add(visited.get(i));
                    break;
                } else if(isAdjacent(visited.get(i), end)) {
                    result.add(visited.get(i));
                    end = visited.get(i);
                }
            }
            Collections.reverse(result);
            return result;
        }
    }

    public ArrayList<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private Integer[] currentInDegree;
        private HashSet<Integer> visited;

        // more instance variables go here

        public TopologicalIterator() {
            fringe = new Stack();
            // more statements go here
            visited = new HashSet();
            currentInDegree = new Integer[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                currentInDegree[i] = inDegree(i);
                if (inDegree(i) == 0) {
                    fringe.push(i);
                }
            }
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            // you supply the real body of this method
                Integer result = fringe.pop();
                for (Edge e : adjLists[result]) {
                    currentInDegree[e.to]--;
                }
                visited.add(result);
                for (int i = 0; i < vertexCount; i++) {
                    if (!visited.contains(i) && !fringe.contains(i) && currentInDegree[i] == 0) {
                        fringe.push(i);
                    }
                }
                return result;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    public Object weight(int vertex, int neighbor) {
        // your code here   
        List result = new LinkedList<>();
        Iterator<Edge> neiIterator = adjLists[vertex].iterator();
        while (neiIterator.hasNext()) {
            Edge curEdge = neiIterator.next();
            if (curEdge.to == neighbor) {
                return curEdge.edgeInfo;
            }
        }
        return null;
    }

    public ArrayList<Integer> shortestPath (int startVertex, int endVertex){
        //your code here...
        LinkedList<Integer> fringe = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, Integer> distance = new HashMap<>();
        HashMap<Integer, Integer> predecessor = new HashMap<>();

        for (int i = 0; i < vertexCount; i++) {
            distance.put(i, MAX);
        }
        distance.put(startVertex, 0);
        fringe.add(startVertex);

        while (!fringe.isEmpty()) {
            int v = fringe.poll();
            if (! visited.contains(v)) {
                visited.add(v);
                for (Object neighbor : neighbors(v)) {
                    int neigh = (int) neighbor;
                    fringe.add(neigh);
                    int weight = (int) weight(v, neigh);
                    if (distance.get(neigh) > distance.get(v) + weight) {
                        distance.put(neigh, distance.get(v) + weight);
                        predecessor.put(neigh, v);
                    }
                }
            }
        }
        ArrayList<Integer> result = new ArrayList<>();
        result.add(endVertex);
        int search = predecessor.get(endVertex);
        while (search != startVertex) {
            result.add(search);
            search = predecessor.get(search);
        }
        result.add(startVertex);
        Collections.reverse(result);
        return result;
    }

    private class Edge {

        private Integer from;
        private Integer to;
        private Object edgeInfo;

        public Edge(int from, int to, Object info) {
            this.from = new Integer(from);
            this.to = new Integer(to);
            this.edgeInfo = info;
        }

        public Integer to() {
            return to;
        }

        public Object info() {
            return edgeInfo;
        }

        public String toString() {
            return "(" + from + "," + to + ",dist=" + edgeInfo + ")";
        }

    }

    public static void main(String[] args) {
        Graph g = new Graph(8);

        g.addUndirectedEdge(0, 1, 4);
        g.addUndirectedEdge(0, 4, 1);
        g.addUndirectedEdge(0, 6, 2);
        g.addUndirectedEdge(0, 7, 6);
        g.addUndirectedEdge(1, 2, 1);
        g.addUndirectedEdge(1, 3, 1);
        g.addUndirectedEdge(1, 4, 3);
        g.addUndirectedEdge(2, 3, 0);
        g.addUndirectedEdge(3, 4, 2);
        g.addUndirectedEdge(3, 5, 1);
        g.addUndirectedEdge(3, 6, 3);
        g.addUndirectedEdge(4, 6, 4);
        g.addUndirectedEdge(5, 6, 5);
        g.addUndirectedEdge(5, 7, 0);
        g.addUndirectedEdge(6, 7, 2);

        System.out.println(g.shortestPath(0, 5));
    }
   /* public static void main(String[] args) {
        ArrayList<Integer> result;

        Graph g1 = new Graph(5);
        g1.addEdge(0, 1);
        g1.addEdge(0, 2);
        g1.addEdge(0, 4);
        g1.addEdge(1, 2);
        g1.addEdge(2, 0);
        g1.addEdge(2, 3);
        g1.addEdge(4, 3);
        System.out.println("Traversal starting at 0");
        result = g1.visitAll(0);
        Iterator<Integer> iter;
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Traversal starting at 2");
        result = g1.visitAll(2);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Traversal starting at 3");
        result = g1.visitAll(3);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Traversal starting at 4");
        result = g1.visitAll(4);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Path from 0 to 3");
        result = g1.path(0, 3);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Path from 0 to 4");
        result = g1.path(0, 4);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Path from 1 to 3");
        result = g1.path(1, 3);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Path from 1 to 4");
        result = g1.path(1, 4);
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("Path from 4 to 0");
        result = g1.path(4, 0);
        if (result.size() != 0) {
            System.out.println("*** should be no path!");
        }

        Graph g2 = new Graph(5);
        g2.addEdge(0, 1);
        g2.addEdge(0, 2);
        g2.addEdge(0, 4);
        g2.addEdge(1, 2);
        g2.addEdge(2, 3);
        g2.addEdge(4, 3);
        System.out.println();
        System.out.println();
        System.out.println("Topological sort");
        result = g2.topologicalSort();
        iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }*/

}

