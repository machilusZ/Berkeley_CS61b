/**
 * Created by yunan on 6/22/17.
 */
public class Unshuffled {
    public static void main(String[] args){
        int x=3;
        while(x>0){
            if(x>2){
                System.out.print("a");
                System.out.print("-");
            }
            if (x == 2) {
                System.out.print("b c");
                System.out.print("-");
            }
            if (x == 1) {
                System.out.print("d");
                x = x - 1;
            }
            x = x - 1;

        }
    }
}
