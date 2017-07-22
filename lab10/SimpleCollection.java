public interface SimpleCollection {

	// This method should return the number of items 
	// contained in the collection
	int size();

	// This method should return true if the collection
	// is empty, otherwise return false
	boolean isEmpty();

	// This method adds k to the collection
	void add(int k);

	// This method removes k from the collection
	void remove(int k);

	// This method returns whether or not the collection contains k
	boolean contains(int k);

}