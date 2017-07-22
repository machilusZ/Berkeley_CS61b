import java.util.*;

public class AddingMachine {

	public static void main (String[] args) {

		Scanner scanner = new Scanner(System.in);
		boolean isPreviousZero = false;
		int total = 0;
		int subtotal = 0;
		int input;
		int last = 1;
		int MAXIMUM_NUMBER_OF_INPUTS = 100;
    	int[] seq=new int[100];
		while (last<MAXIMUM_NUMBER_OF_INPUTS) {
			input = scanner.nextInt();
			if (input == 0) {
				if (isPreviousZero) {
					System.out.println("total " + total);
					for(int i=0;i<last-1;i++){
						System.out.println(seq[i]);
					}
					return;
				} else {
					System.out.println("subtotal " + subtotal);
					total += subtotal;
					subtotal = 0;
					isPreviousZero = true;

				}
			}

			if (input != 0) {
				isPreviousZero = false;
				subtotal+=input;
				seq[last-1]=input;
				last++;
			}

		   
		}
	}

}
