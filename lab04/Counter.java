public class Counter {

    private int count;

    public Counter() {
        count = 0;
    }

    public void increment() {
        count++;
    }

    public void reset() {
        count = 0;
    }
    
    public int value() {
        return count;
    }



}