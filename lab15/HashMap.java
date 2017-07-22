import  java.util.LinkedList;
import java.util.Iterator;
/**
 * Created by yunan on 7/20/17.
 */
public class HashMap<K,V> implements Map61BL<K,V> {

    /** A wrapper class for holding each (KEY, VALUE) pair. */
    LinkedList<Entry<K, V>>[]  mapArray;
    int capacity;
    int size;
    float loadFactor;

    HashMap() {
        capacity = 16;
        mapArray = new LinkedList[capacity];
        size = 0;
        loadFactor = (float)0.5;
    }

    HashMap(int initialCapacity) {
        capacity = initialCapacity;
        mapArray = new LinkedList[capacity];
        size = 0;
        loadFactor = (float)0.5;
    }

    HashMap(int initialCapacity, float loadFactor) {
        capacity = initialCapacity;
        mapArray = new LinkedList[capacity];
        size = 0;
        this.loadFactor = (float)loadFactor;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            int index = 0;
            Iterator<Entry<K, V>> myIterator;

            @Override
            public boolean hasNext() {
                if (mapArray[index] == null || !myIterator.hasNext()) {
                    index ++;
                    while (mapArray[index] == null) {
                        if (index == capacity - 1){
                            return false;
                        }
                        index ++;
                    }
                    myIterator = mapArray[index].iterator();
                }
                if (index == capacity - 1 && !myIterator.hasNext()) {
                    return false;
                }
                return true;
            }

            @Override
            public K next() {
                return (K)myIterator.next()._key;
            }
        };
    }


    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            mapArray[i] = null;
        }
        size = 0;
    }

    /** Returns true if the map contains the KEY. */
    @Override
    public boolean containsKey(K key) {
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        if (mapArray[index] == null) {
            return false;
        }
        for (Entry e: mapArray[index]) {
            if (e._key.equals(key)) {
                return true;
            }
        }
        return false;

    }

    /** Returns the value for the specified KEY. */
    @Override
    public V get(K key) {
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        for (Entry e: mapArray[index]) {
            if (e._key.equals(key)) {
                return (V)e._value;
            }
        }
        return null;
    }

    /** Put a (KEY, VALUE) pair into this map. */
    @Override
    public void put(K key, V value) {
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        if (containsKey(key)) {
            int count = 0;
            for (Entry<K, V> e: mapArray[index]) {
                if (e._key.equals(key)) {
                    mapArray[index].set(count, new Entry<K, V>(key, value));
                }
                count ++;
            }
        } else {
            if (mapArray[index] != null) {
                if ((float)size / (float)capacity < loadFactor) {
                    mapArray[index].add(new Entry(key, value));
                    size++;
                } else {
                    resize();
                    index = (key.hashCode() & 0x7FFFFFFF) % capacity;
                    mapArray[index].add(new Entry(key, value));
                    size++;
                }
            } else {
                LinkedList<Entry<K, V>> tmp = new LinkedList<>();
                tmp.add(new Entry(key, value));
                if ((float)size / (float)capacity < loadFactor) {
                    mapArray[index] = tmp;
                    size++;
                } else {
                    resize();
                    index = (key.hashCode() & 0x7FFFFFFF) % capacity;
                    mapArray[index] = tmp;
                    size++;
                }
            }



        }
    }

    void resize() {
        LinkedList<Entry<K, V>>[] oldMapArray = new LinkedList[capacity];
        for (int i = 0; i< capacity; i++) {
            if (mapArray[i] == null) {
                oldMapArray[i] = null;
            } else {
                oldMapArray[i] = new LinkedList(mapArray[i]);
            }
        }
        capacity *= 2;
        size = 0;
        mapArray = new LinkedList[capacity];
        for (LinkedList<Entry<K, V>> l: oldMapArray) {
            if (l != null) {
                for (Entry<K, V> e : l) {
                    this.put(e._key, e._value);
                }
            }
        }
    }

    @Override
    public boolean remove(K key, V value) {
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        for (Entry<K, V> e: mapArray[index]) {
            if (e._key.equals(key)) {
                mapArray[index].remove(e);
                size --;
                return true;
            }
        }
        return false;
    }

    @Override
    public V remove(K key) {
        //throw new UnsupportedOperationException();
        int index = (key.hashCode() & 0x7FFFFFFF) % capacity;
        for (Entry<K, V> e: mapArray[index]) {
            if (e._key.equals(key)) {
                mapArray[index].remove(e);
                size --;
                return e._value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    /** Remove a single entry, KEY, from this table and returns true if successful. */
    private static class Entry<K,V> {

        /** The key used for lookup. */
        private K _key;
        /** The associated value. */
        private V _value;

        /** Create a new (KEY, VALUE) pair. */
        public Entry(K key, V value) {
            _key = key;
            _value = value;
        }

        /** Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return _key.equals(other._key);
        }

        /** Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry &&
                    _key.equals(((Entry) other)._key) &&
                    _value.equals(((Entry) other)._value));
        }

    }
}
