public interface SimpleList extends SimpleCollection {

	// Returns the integer stored at the i-th index in the List
	int get(int i);

	// Adds the integer k to the i-th position in the list,
	// note now this is different from the basic Collection add
	void add(int i, int k);

	// Removes the integer in the i-th position
	// note now this is different from the basic Collection remove
	void removeIndex(int i);
}