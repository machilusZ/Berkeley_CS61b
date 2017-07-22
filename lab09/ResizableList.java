import java.util.Arrays;

/**
 * Created by qibao on 2017/7/1.
 */
public class ResizableList extends FixedSizeList {

    public ResizableList(int capacity) {
        super(capacity);
    }

    public ResizableList() {
        super(20);
    }


    @Override
    public void add(int k) {
        if (count == values.length) {
            resize();
        }
        super.add(k);
    }

    @Override
    public void add(int i, int k) {
        if (count == values.length) {
            resize();
        }
        super.add(i, k);
    }

    private void resize() {
        int[] newArray = new int[values.length * 2];
        for (int i = 0; i < values.length; i++) {
            newArray[i] = values[i];
        }
        values = newArray;
    }

}
