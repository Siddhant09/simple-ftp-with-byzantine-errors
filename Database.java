import java.util.ArrayList;
import java.util.List;

public class Database {
    
    public static List<User> users = new ArrayList<User>();

    public static void createData() {
        try {
            User user1 = new User("jane01", "Jane@123");
            users.add(user1);
            
            User user2 = new User("john99", "John@123");
            users.add(user2);            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
