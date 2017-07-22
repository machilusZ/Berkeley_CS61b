import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserExercises extends DBTable<User> {
    UserExercises() {
    }

    UserExercises(Collection<User> lst) {
        super(lst);
    }

    /**
     * Get an ordered List of Users, sorted first on age,
     * then on their id if the age is the same.
     */
    public List<User> getOrderedByAgeThenId() {
        return null; // FIX ME
    }

    /**
     * Get the average age of all the users.
     * If there are no users, the average is 0.
     */
    public double getAverageAge() {
        return -1; // FIX ME
        // HINT: You may find an IntStream helpful 
    }

    /**
     * Group usernames by user age, for all users that have an age greater than min_age.
     * Usernames with ages less than or equal to min_age are excluded.
     * Returns a Map from each age present to a list of the usernames that have that age.
     */
    public Map<Integer, List<String>> groupUsernamesByAgeOlderThan(int min_age) {
        return null; // FIX ME
        // HINT: See the Additional Examples for a helpful Collector
        // HINT2: You will need to use Collectors.mapping. See the Javadocs for examples
    }
}
