/** An Edge class representing an edge between two integer vertices. The weight
 *  of the vertex is also an int because I'm lazy and didn't want to deal with
 *  generics.
 *
 *  @author Antares Chen
 *  @since  2016-7-17
 */
public class Edge implements Comparable<Edge> {

    /** The index of the source node */
    private int src;
    /** The index of the destination node */
    private int dest;
    /** The label for the edge. This could represent weight */
    private int label;

    /** Creates an edge object representing (SRC, DEST) with edge weight
     *  LABEL. */
    Edge(int src, int dest, int label) {
        this.src = src;
        this.dest = dest;
        this.label = label;
    }

    /** Returns the edge's source node. */
    public int getSource() {
        return src;
    }

    /** Returns the edge's destination node. */
    public int getDest() {
        return dest;
    }

    /** Returns the weight of the edge. */
    public int getLabel() {
        return label;
    }

    /** Returns n > 0 if this.label > other.label, else n < 0 if this.
     *  label < other.label, else n = 0. */
    public int compareTo(Edge other) {
        return label - other.label == 0 ? 1 : label - other.label;
    }

    /** Two edges are equal if they have the same source, destination, and
     *  weight. */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edge other = (Edge) o;
        return (src == other.src && dest == other.dest && label == other.label) ||
                (src == other.dest && dest == other.src && label == other.label);
    }

    /** Returns the hashcode for this instance. */
    public int hashCode() {
        int hash = src ^ (src >>> 32);
        hash = 31 * hash + dest ^ (dest >>> 32);
        hash = 31 * hash + label ^ (label >>> 32);
        return hash;
    }

    /** Returns the string representation of an edge. */
    public String toString() {
        return "{" + src + ", " + dest + "} -> " + label;
    }
}

