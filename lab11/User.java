import java.util.Arrays;
import java.util.Objects;

public class User implements Comparable<User> {
    /** Global counter tracking the next available id **/
    private static int nextId = 1;
    /** Identifier marking that this is the id-th user created **/
    private int id;
    /**
     * For this assignment, age is just an automatically assigned field
     * for the sake of variety.
     */
    private int age;
    private String username;
    private String email;

    public User(String username, String email) {
        id = nextId++;
        this.username = username;
        this.email = email;
        setAge();
    }

    /** Force assign an id to a created user **/
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        setAge();
    }

    void setAge() {
        age = (id % 13) + 20;
    }

    int getAge() {
        return age;
    }

    int getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o.toString().equals(this.toString())){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(User o) {
        if(!(o instanceof User)){
            throw new RuntimeException("not User") ;
        }
        else if(this.id!=o.id){
            return this.id - o.id;
        }
        else {
            return this.username.compareTo(o.username);
        }
        //return Integer.MIN_VALUE; // FIX ME
    }

    public static void main(String[] args) {
        User[] users = {new User(2, "daniel", ""), new User(4, "matt", ""),
                new User(1, "sarahjkim", ""), new User(1, "alanyao", "")};
        Arrays.sort(users);
        System.out.println(Arrays.toString(users));
    }
}
