import java.util.Arrays;

public class DistributionSorts {

	/**
	 * Modify arr to be sorted. Assume arr only contains 0, 1, ..., 9
	 */
	public static void countingSort(int[] arr) {
		// TODO your code here!
		int[] myArr = new int[10];
        for (int i: arr) {
            myArr[i]++;
        }
        int base = 0;
        for (int i = 0; i < myArr.length; i++) {
            for (int j = 0; j < myArr[i]; j++) {
                arr[base] = i;
                base++;
            }
        }
	}

	/**
	 * Sorts the given array using MSD radix sort. 
	 */
	public static void MSDRadixSort(int[] arr) {
		int maxDigit = mostDigitsIn(arr) - 1;
		MSDRadixSortFromDigitInBounds(arr, maxDigit, 0, arr.length);
	}

	/**
	 * Radix sorts the input array only between the indices start and end. Only
	 * considers digits from the input digit on down. This method is recursive.
	 */
	public static void MSDRadixSortFromDigitInBounds(int[] arr, int digit,
			int start, int end) {
		// TODO your code here! Make sure to use the countingSortByDigitInBounds
		// helper method, given below.
		if (end <= start + 1) {
			return;
		}
		if (digit < 0) {
			return;
		}
		else if (end == start + 2 && arr[start] == arr[start + 1]) {
			return;
		}
		else if (arr[start] == arr[end-1] && arr[start + 1] == arr[end-1] && end == start + 3) {
			return;
		} else {
			int[] bounds = countingSortByDigitInBounds(arr, digit, start, end);
			for (int i = 0; i < bounds.length - 1; i+=1) {
				MSDRadixSortFromDigitInBounds(arr, digit-1, bounds[i], bounds[i+1]);
			}
		}
	}

	/**
	 * A helper method for radix sort. Modifies arr to be sorted according to
	 * digit. Only sorts the portion of the arr between the indices start
	 * (inclusive) and end (exclusive).
	 * 
	 * Does NOT return the sorted array. Returns an array containing the
	 * boundary of each same-digit bucket in the array. This will be useful for
	 * radix sort.
	 */
	public static int getNthDig(int number, int n) {
		return (int) ((number / Math.pow(10, n)) % 10);
	}

	private static int[] countingSortByDigitInBounds(int[] arr, int digit,
			int start, int end) {
		// TODO your code here!
		int[] count = new int[10];
		for (int i = start; i < end; i++) {
			count[getNthDig(arr[i], digit)]++;
		}
		int[] starts = new int[10];
		for (int j = -1; j < count.length - 1; j++) {
			if (j == -1) {
				starts[j+1] = 0;
			} else {
				starts[j+1] = count[j] + starts[j];
			}
		}
		int []sorted = new int[end - start];
		for (int n = 0; n < end - start; n++) {
			int m = getNthDig(arr[n + start], digit);
			sorted[starts[m]] = arr[n + start];
			starts[m]++;
		}
		for (int k = start; k < end; k++) {
			arr[k] = sorted[k - start];
		}
		int[] result = new int[starts.length + 1];
		result[0] = start;
		for (int g= 0; g < starts.length; g ++) {
			result[g + 1] = starts[g] + start;
		}
		return result;
	}

	/**
	 * Returns the highest number of digits that any integer in arr happens to
	 * have.
	 */
	private static int mostDigitsIn(int[] arr) {
		int maxDigitsSoFar = 0;
		for (int num : arr) {
			int numDigits = (int) (Math.log10(num) + 1);
			if (numDigits > maxDigitsSoFar) {
				maxDigitsSoFar = numDigits;
			}
		}
		return maxDigitsSoFar;
	}

	/**
	 * Returns a random integer between 0 and 9999.
	 */
	private static int randomInt() {
		return (int) (10000 * Math.random());
	}

	/**
	 * Returns a random integer between 0 and 9.
	 */
	private static int randomDigit() {
		return (int) (10 * Math.random());
	}

	/**
	 * Runs some very basic tests of counting sort and radix sort.
	 */
	public static void main(String[] args) {
		int[] arr1 = new int[20];
		for (int i = 0; i < arr1.length; i++) {
			arr1[i] = randomDigit();
		}
		System.out.println("Original array: " + Arrays.toString(arr1));
		countingSort(arr1);
		if (arr1 != null) {
			System.out.println("Should be sorted: " + Arrays.toString(arr1));
		}

		int[] arr2 = new int[3];
		for (int i = 0; i < arr2.length; i++) {
			arr2[i] = randomDigit();
		}
		System.out.println("Original array: " + Arrays.toString(arr2));
		MSDRadixSort(arr2);
		System.out.println("Should be sorted: " + Arrays.toString(arr2));

		int[] arr3 = new int[30];
		for (int i = 0; i < arr3.length; i++) {
			arr3[i] = randomInt();
		}
		System.out.println("Original array: " + Arrays.toString(arr3));
		MSDRadixSort(arr3);
		System.out.println("Should be sorted: " + Arrays.toString(arr3));
	}
}

