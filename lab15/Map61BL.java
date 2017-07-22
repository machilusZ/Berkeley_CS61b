/**
 * Your Map61BL implementations shoud include "implements Map61BL<K, V>" at the
 * end of your "public class..." declaration, though you can use other formal
 * type parameters if you'd like.
 */
public interface Map61BL<K, V> extends Iterable<K> {

    /** Removes all of the mappings from this map. */
    void clear();

    /** Returns true if this map contains a mapping for the specified key. */
    boolean containsKey(K key);

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    V get(K key);

    /** Associates the specified value with the specified key in this map. */
    void put(K key, V value);

    /**
     * Remove and return a key and its associated value. If you don't
     * implement this, throw an UnsupportedOperationException.
     */
    V remove(K key);

    /**
     * Remove a particular key-value mapping and return true if successful.
     * If you don't implement this, throw an UnsupportedOperationException.
     */
    boolean remove(K key, V value);

    /** Returns the number of key-value mappings in this map. */
    int size();

}
