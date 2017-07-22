public class RuntimeQuiz {
    /** Asymptotic notations **/
    public enum Asymptotic { BIG_O, BIG_THETA, BIG_OMEGA }
    /** Functions describing runtime **/
    public enum Runtime { // Assuming n is the length of the input
        CONSTANT,     // 1
        LOG_N,        // log(n)
        LINEAR,       // n
        LINEARITHMIC, // n * log(n)
        QUADRATIC,    // n^2
        CUBIC,        // n^3
        EXPONENTIAL   // a^n where a is any constant
    }

    /**
     * Fill out the missing Asymptotic and Runtime values according to the
     * asymptotic behavior of each method. Give the tightest bound you can.
     * f1() is given as an example - with notation and function together, it is in Theta(N),
     * where N is the length of the input array.
     */
    public static Asymptotic f1_notation = Asymptotic.BIG_THETA;
    public static Runtime f1_runtime = Runtime.LINEAR;
    public void f1(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < Integer.MAX_VALUE; j++) {
                System.out.println("Hi!");
            }
        }
    }

    public static Asymptotic f2_notation=Asymptotic.BIG_THETA;
    public static Runtime f2_runtime=Runtime.QUADRATIC;
    public int f2(int n) {
        if (n <= 1) return n;
        f1(new int[n]);
        return n + n * f2(n - 1) + n * n * f2(1);
    }

    public static Asymptotic f3_notation=Asymptotic.BIG_THETA;
    public static Runtime f3_runtime=Runtime.LINEAR;
    /* When f3 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f3(char[] array, int start, int end) {
        if (array.length <= 1 || end <= start) return 1;
        int mid = start + ((end - start) / 2);
        return f3(array, start, mid) + f3(array, mid + 1, end);
    }

    public static Asymptotic f4_notation=Asymptotic.BIG_THETA;
    public static Runtime f4_runtime=Runtime.LINEARITHMIC;
    /* When f4 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f4(char[] array, int start, int end) {
        if (array.length <= 1 || end <= start) return 1;
        int counter = 0;
        for (int i = start; i < end; i++) {
            if (array[i] == 'a') counter++;
        }
        int mid = start + ((end - start) / 2);
        return counter + f4(array, start, mid) + f4(array, mid + 1, end);
    }

    public static Asymptotic f5_notation=Asymptotic.BIG_THETA;
    public static Runtime f5_runtime=Runtime.LOG_N;
    public void f5(int n) {
        int[] array = {1, 2, 3};
        while (n > 0) {
            f1(array);
            n = n / 2;
        }
    }

    public static Asymptotic f6_notation=Asymptotic.BIG_O;
    public static Runtime f6_runtime=Runtime.LINEAR;
    public void f6(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] == array[i-1]) {
                System.out.println("Sarah is the potatoest");
                return;
            }
        }
    }

    public static Asymptotic f7_notation=Asymptotic.BIG_O;
    public static Runtime f7_runtime=Runtime.EXPONENTIAL;
    public static Asymptotic f7_omega_notation = Asymptotic.BIG_OMEGA;
    public static Runtime f7_omega_runtime=Runtime.LINEAR;
    /* When f7 is first called, start will be 0 and end will be the length of the array - 1 */
    public int f7(int[] array, int start, int end) {
        if (array.length <= 1 || end <= start) {
            return 1;
        } else if (array[start] <= array[end]) {
            return f7(array, start + 1, end - 1);
        } else {
            int tmp = array[start];
            array[start] = array[end];
            array[end] = tmp;
            return f7(array, start + 1, end) + f7(array, start, end - 1)
                    + f7(array, start + 1, end - 1);
        }
    }

    /** Below, fill out the runtimes for the specified methods of your DLList **/

    /* insert(Object o, int position) */
    public static Asymptotic insert_notation=Asymptotic.BIG_O;
    public static Runtime insert_runtime=Runtime.LINEAR;

    /* insertBack(Object o) */
    public static Asymptotic insert_back_notation=Asymptotic.BIG_THETA;
    public static Runtime insert_back_runtime=Runtime.CONSTANT;

    /* get(int position) */
    public static Asymptotic get_notation=Asymptotic.BIG_O;
    public static Runtime get_runtime=Runtime.LINEAR;

    /* remove(Object o) */
    public static Asymptotic remove_object_notation=Asymptotic.BIG_THETA;
    public static Runtime remove_object_runtime=Runtime.LINEAR;

    /* remove(DLNode n) */
    public static Asymptotic remove_node_notation=Asymptotic.BIG_THETA;
    public static Runtime remove_node_runtime=Runtime.CONSTANT;

    /* reverse() */
    public static Asymptotic reverse_notation=Asymptotic.BIG_THETA;
    public static Runtime reverse_runtime=Runtime.LINEAR;

}
