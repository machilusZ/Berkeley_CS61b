/**
 * Created by yunan on 6/22/17.
 */
public class TriangleDrawer {
    public static void main(String[] args)
    {
        int col=0;
        int row=0;
        int size = 10;
        while (row<size){
            col=0;
            while(col<=row)
            {
                System.out.print('*');
                col += 1;
            }
            System.out.println();
            row+=1;
        }
    }
}


