package st10486338;

/**
 * @author Onthathile Lesatsi
 */
public class LoginLogic 
{
    // Need this to check against registered details
    private final RegistrationLogic registrationLogic;
    private boolean isLoggedIn;

    // Constructor to link with RegistrationLogic
    public LoginLogic(RegistrationLogic reg) 
    {
        this.registrationLogic = reg;
        this.isLoggedIn = false; // Start logged out
    }

    // Try to log in with username and password
    public boolean loginUser(String username, String password) 
    {
        // Check if they match what’s stored
        isLoggedIn = username != null && password != null &&
                     username.equals(registrationLogic.getUsername()) &&
                     password.equals(registrationLogic.getPassword());
        return isLoggedIn;
    }

    // Give feedback on login status
    public String returnLoginStatus() 
    {
        if (isLoggedIn) {
            return "Welcome back " + registrationLogic.getFirstName() + " " + 
                   registrationLogic.getLastName() + ",\nit is great to see you.";
        } else {
            return "Username & Password do not match our records, please try again.";
        }
    }
}