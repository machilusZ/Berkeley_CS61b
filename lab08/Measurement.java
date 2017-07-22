public class Measurement {
	private int _inches;
	private int _feets;

	/**
	 * Constructor: initialize this object to be a measurement of 0 feet, 0
	 * inches
	 */
	public Measurement() {
		_inches = 0;
		_feets = 0;
	}

	/**
	 * Constructor: takes a number of feet as its single argument, using 0 as
	 * the number of inches
	 */
	public Measurement(int feet) {
		_feets = feet;
		_inches = 0;
	}

	/**
	 * Constructor: takes the number of feet in the measurement and the number
	 * of inches as arguments (in that order), and does the appropriate
	 * initialization
	 */
	public Measurement(int feet, int inches) {
		_feets = feet;
		_inches = inches;
	}

	/**
	 * Returns the number of feet in in this Measurement. For example, if the
	 * Measurement has 1 foot and 6 inches, this method should return 1.
	 */
	public int getFeet() {
		return _feets; // provided to allow the file to compile
	}

	/**
	 * Returns the number of inches in this Measurement. For example, if the
	 * Measurement has 1 foot and 6 inches, this method should return 6.
	 */
	public int getInches() {
		return _inches; // provided to allow the file to compile
	}

	/** Adds the argument m2 to the current measurement */
	public Measurement plus(Measurement m2) {
		Measurement a = new Measurement(this._feets,this._inches);
		//a._feets += m2._feets;
		//a._inches += m2._inches;
		int curr = 12*a._feets+a._inches;
		int temp = 12*m2._feets+m2._inches;
		curr+=temp;
		a._feets = curr / 12;
		a._inches = curr % 12;
		return a; // provided to allow the file to compile
	}

	/**
	 * Subtracts the argument m2 from the current measurement. You may assume
	 * that m2 will always be smaller than the current measurement.
	 */
	public Measurement minus(Measurement m2) {
		Measurement a = new Measurement(this._feets,this._inches);
		//a._feets -= m2._feets;
		//a._inches -= m2._inches;
		int curr = 12*a._feets+a._inches;
		int temp = 12*m2._feets+m2._inches;
		curr-=temp;
		a._feets = curr / 12;
		a._inches = curr % 12;
		return a; // provided to allow the file to compile
	}

	/**
	 * Takes a nonnegative integer argument n, and returns a new object that
	 * represents the result of multiplying this object's measurement by n. For
	 * example, if this object represents a measurement of 7 inches, multiple
	 * (3) should return an object that represents 1 foot, 9 inches.
	 */
	public Measurement multiple(int multipleAmount) {
		Measurement a = new Measurement(this._feets,this._inches);
		int temp = a._inches + 12*a._feets;
		temp *= multipleAmount;
		a._feets = temp / 12;
		a._inches = temp % 12;
		return a; // provided to allow the file to compile
	}

	/**
	 * toString should return the String representation of this object in the
	 * form f'i" that is, a number of feet followed by a single quote followed
	 * by a number of inches less than 12 followed by a double quote (with no
	 * blanks).
	 */
	@Override
	public String toString() {
		String a = "";
		a+=_feets;
		a+="\'";
		a+=_inches;
		a+="\"";
		return a; // provided to allow the file to compile
	}

}
