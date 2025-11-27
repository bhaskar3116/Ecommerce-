/**
 * UserManager now acts as a simple "pass-through" to the DBManager.
 * It no longer holds any data or does any file I/O.
 */
public class UserManager {

    /**
     * The initialize method is no longer needed, as the database is
     * always ready. We can leave it empty or remove it.
     */
    public static void initialize() {
        // No longer need to load from file
    }

    /**
     * Attempts to register a new user by calling the database manager.
     * @return true if successful, false if username is taken.
     */
    public static boolean register(String username, String password) {
        return DBManager.registerUser(username, password);
    }

    /**
     * Attempts to authenticate a user by calling the database manager.
     * @return the User object if successful, null otherwise.
     */
    public static User authenticate(String username, String password) {
        return DBManager.authenticateUser(username, password);
    }
}