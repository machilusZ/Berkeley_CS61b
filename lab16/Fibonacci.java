import java.util.HashMap;

public class Fibonacci {
	int callsToFib;
	int result;
	HashMap<Integer, Integer> myMap;

	public Fibonacci(int n){
		this.callsToFib = 0;
		myMap = new HashMap(n);
		this.result = fib(n);
	}

	private int fib(int n) {
		/*callsToFib++;
		if (n == 0) {
			return 0;
		} else if (n == 1) {
			return 1;
		} else {
			myMap.put(n - 1, fib(n - 1));
			int returnValue = myMap.get(n - 1) + myMap.get(n - 2);
			//System.out.println(callsToFib);
			return returnValue;
		}*/
		callsToFib++;
		if (n == 0) {
			return 0;
		} else if (n == 1) {
			return 1;
		} else if (myMap.containsKey(n)) {
			return myMap.get(n);
		}else {
			int returnValue = fib(n - 1) + fib(n - 2);
			myMap.put(n, returnValue);
			return returnValue;
		}
	}

}
