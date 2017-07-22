/**
 * Created by yunan on 6/22/17.
 */
public class TriangleDrawer2 {
    public static void main(String[] args)
    {
        int col=0;
        int row=0;
        int size = 10;
        for(row=0;row<size;row++){
            for(col=0;col<=row;col++)
            {
                System.out.print('*');
            }
            System.out.println();
        }
    }
}


