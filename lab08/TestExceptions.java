import java.util.Objects;

public class TestExceptions {

	public static void main (String [ ] args) {

		try {
			Integer num = null;
			System.out.println (num.byteValue());
		} catch (NullPointerException e) {
			System.out.println ("got null pointer");
		}
		try {
			Object[] o = "abc".split(";");
			o[0] = 42;
		} catch (ArrayStoreException e) {
			System.out.println ("got illegal array store");
		}
		try {
			Object x = new Integer(0);
			System.out.println((String)x);
		} catch (ClassCastException e) {
			System.out.println ("got illegal class cast");
		}
	}

}
