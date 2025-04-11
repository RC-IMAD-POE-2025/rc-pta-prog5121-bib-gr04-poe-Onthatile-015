package st10486338;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Onthabile Lesatsi
 */
public class LoginLogicTest {
    
    private RegistrationLogic registrationLogic;
    private LoginLogic loginLogic;

    // Test data constants
    private static final String VALID_USERNAME = "kyl_1";
    private static final String VALID_PASSWORD = "Passw0rd!";
    private static final String INVALID_USERNAME = "wrong";
    private static final String INVALID_PASSWORD = "wrong";
    private static final String VALID_CELL_PHONE = "+27123456789";
    private static final String VALID_FIRST_NAME = "Onthathile";
    private static final String VALID_LAST_NAME = "Lesatsi";

    // Message constants
    private static final String SUCCESS_MESSAGE = "Welcome back " + VALID_FIRST_NAME + " " + VALID_LAST_NAME + ",\nit is great to see you.";
    private static final String ERROR_MESSAGE = "Username & Password do not match our records, please try again.";

    @BeforeEach
    public void setUp() 
    {
        registrationLogic = new RegistrationLogic();
        registrationLogic.registerUser(VALID_USERNAME, VALID_PASSWORD, VALID_CELL_PHONE, VALID_FIRST_NAME, VALID_LAST_NAME);
        loginLogic = new LoginLogic(registrationLogic);
    }

    @Test
    public void testValidLogin() 
    {
        assertTrue(loginLogic.loginUser(VALID_USERNAME, VALID_PASSWORD));
        assertEquals(SUCCESS_MESSAGE, loginLogic.returnLoginStatus());
    }

    @Test
    public void testInvalidLogin() 
    {
        assertFalse(loginLogic.loginUser(INVALID_USERNAME, INVALID_PASSWORD));
        assertEquals(ERROR_MESSAGE, loginLogic.returnLoginStatus());
    }
}
