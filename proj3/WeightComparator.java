import java.util.Comparator;

/**
 * RadiusComparator.java
 */

public class WeightComparator implements Comparator<TSTNode> {

    public WeightComparator() {
    }

    public int compare(TSTNode node1, TSTNode node2) {
        return Double.compare(node1.val, node2.val);
    }
}
