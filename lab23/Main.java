import java.io.File;

/** This class holds a timing suite for the two implementations of Kruskal's
 *  algorithm: one that calculates connectivity by BFS, and the other by using
 *  the UnionFind data structure.
 *
 *  @author Antares Chen
 *  @since  2016-7-17
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("                   Kruskal Timer v1.0");
        System.out.println("============================================================");

        int count = 1000;
        if (args.length > 0) {
            if (args[0].equals("--all")) {
                File dir = new File("inputs/");
                for (File inFile : dir.listFiles()) {
                    Graph input = Graph.loadFromText(inFile.getAbsolutePath());
                    System.out.println(">> Now processing file: " + inFile.getPath());
                    long timeSpent = time1(input);
                    System.out.println(">> Processing time: " + timeSpent + " milliseconds\n");
                }
                return;
            } else {
                try {
                    count = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Bad argument! Cannot parse \"" + args[0] + "\"");
                    return;
                }
            }
        }
        Graph input = Graph.randomGraph(count, count * 3, 100);
//        for (Edge e:input.getAllEdges()) {
//            System.out.println(e);
//            System.out.println(e.getSource());
//            System.out.println(e.getDest());
//            System.out.println(e.getLabel());
//            System.out.println();
//        }
//        System.out.println();
//        for (int i = 0; i < count; i++) {
//            System.out.println(i + "\'s neighbors are " + input.getNeighbors(i));
//        }
//        System.out.println();
//        for (int a = 0; a < count; a++) {
//            for (int b = a + 1; b < count; b++) {
//                System.out.println(a + " and " + b + " is " + Kruskal.isConnected(input, a, b));
//            }
//        }
//        UnionFind test = new UnionFind(4);
//        test.union(0, 1);
//        for (int i:test.djs) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        test.union(2, 3);
//        for (int i:test.djs) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        test.union(0, 2);
//        for (int i:test.djs) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        test.union(1, 3);
//        for (int i:test.djs) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//        System.out.println(test.djs[0] + " "+ test.sizeOf(0) + " " + test.djs[0]);

        System.out.println(">> Now processing number of vertices = " + count);
        long timeSpent2 = time2(input);
        System.out.println(">> Processing time2: " + timeSpent2 + " milliseconds\n");
        long timeSpent1 = time1(input);
        System.out.println(">> Processing time1: " + timeSpent1 + " milliseconds\n");
    }

    /** Returns the elapsed time on running Kruskal's algorithm with the
     *  INPUT graph and CONNECTED - the function that determines if two
     *  vertices are connected. */
    public static long time1(Graph input) {
        long startTime = System.currentTimeMillis();
        Graph mst = Kruskal.minSpanTree(input);
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
    public static long time2(Graph input) {
        long startTime = System.currentTimeMillis();
        Graph mst = Kruskal.minSpanTreeFast(input);
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}

