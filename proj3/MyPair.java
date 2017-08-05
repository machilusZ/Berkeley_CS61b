public class MyPair implements Comparable{
    double weight;
    String term;
    public MyPair(double weight, String term) {
        this.weight = weight;
        this.term = term;
    }
    public int compareTo(Object o) {
        MyPair that = (MyPair) o;
        return Double.compare(this.weight, that.weight);    
    }
}

