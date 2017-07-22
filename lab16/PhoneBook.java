import java.util.*;

public class PhoneBook {
    // TODO Add any instance variables necessary
    HashMap<Person, PhoneNumber> hm = new HashMap();

    /*
     * Adds a person with this name to the phone book and associates
     * with the given PhoneNumber.
     */
    public void addEntry(Person personToAdd, PhoneNumber numberToAdd){
        // TODO Add your own code
        hm.put(personToAdd, numberToAdd);

    }

    /*
     * Access an entry in the phone book.
     */
    public PhoneNumber getNumber(Person personToLookup){
        // TODO Add your own code
        if(hm.containsKey(personToLookup)){
            return hm.get(personToLookup);
        }
        return null;
    }

}
