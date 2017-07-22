/**
 * Created by yunan on 6/23/17.
 */
public class SieveOfEratosthenes {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("You need to enter an argument!");
        }
        int upperBound = Integer.parseInt(args[0]);
        boolean[] isPrime=new boolean[upperBound+1];

        for(int i=2;i<upperBound;i++){
            isPrime[i]=true;
        }

        for(int divisor=2;divisor*divisor<=upperBound;divisor++){
            if(isPrime[divisor]){
                for(int i=2*divisor;i<=upperBound;i=i+divisor){
                    isPrime[i]=false;
                }

            }
        }
        for(int i=2;i<=upperBound;i++){
            if(isPrime[i]){
                System.out.println(i);
            }
        }

    }
}
