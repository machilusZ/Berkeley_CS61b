public interface SimpleCollection {

	/** Returns the number of items in the collection. */
	int size();

	/** Returns if the collection is empty. */
	boolean isEmpty();

	/** Adds k into the collection. */
	void add(int k);

	/** Removes k from the collection. */
	void remove(int k);

	/** Returns if the collection contains k. */
	boolean contains(int k);
}
