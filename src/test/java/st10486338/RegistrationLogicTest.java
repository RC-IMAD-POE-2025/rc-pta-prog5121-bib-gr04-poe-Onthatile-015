package st10486338;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Onthabile Lesatsi
 */
public class RegistrationLogicTest {
    
    private RegistrationLogic registrationLogic;
    
    // Test data constants
    private static final String VALID_USERNAME = "kyl_1";
    private static final String VALID_PASSWORD = "Passw0rd!";
    private static final String VALID_CELL_PHONE = "+27123456789";
    private static final String VALID_FIRST_NAME = "Onthathile";
    private static final String VALID_LAST_NAME = "Lesatsi";
    private static final String INVALID_USERNAME = "kyle";
    private static final String INVALID_PASSWORD = "pass";
    private static final String INVALID_CELL_PHONE = "12345";
    private static final String INVALID_FIRST_NAME = "123";
    private static final String INVALID_LAST_NAME = "";

    // Message constants
    private static final String USERNAME_SUCCESS = "Username successfully captured";
    private static final String PASSWORD_SUCCESS = "Password successfully captured";
    private static final String CELL_PHONE_SUCCESS = "Cellphone number successfully captured";
    private static final String FIRST_NAME_SUCCESS = "First name successfully captured";
    private static final String LAST_NAME_SUCCESS = "Last name successfully captured";
    private static final String REGISTRATION_SUCCESS = "Registration successful";
    private static final String USERNAME_ERROR = "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
    private static final String PASSWORD_ERROR = "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    private static final String CELL_PHONE_ERROR = "Cellphone number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
    private static final String FIRST_NAME_ERROR = "First name is invalid.";
    private static final String LAST_NAME_ERROR = "Last name is invalid.";
    private static final String REGISTRATION_ABORTED = "Registration aborted";

    @BeforeEach
    public void setUp() {
        registrationLogic = new RegistrationLogic();
    }

    @Test
    public void testValidRegistration() {
        String result = registrationLogic.registerUser(VALID_USERNAME, VALID_PASSWORD, VALID_CELL_PHONE, VALID_FIRST_NAME, VALID_LAST_NAME);
        assertTrue(result.contains(USERNAME_SUCCESS));
        assertTrue(result.contains(PASSWORD_SUCCESS));
        assertTrue(result.contains(CELL_PHONE_SUCCESS));
        assertTrue(result.contains(FIRST_NAME_SUCCESS));
        assertTrue(result.contains(LAST_NAME_SUCCESS));
        assertTrue(result.contains(REGISTRATION_SUCCESS));
    }

    @Test
    public void testInvalidUsername() {
        String result = registrationLogic.registerUser(INVALID_USERNAME, VALID_PASSWORD, VALID_CELL_PHONE, VALID_FIRST_NAME, VALID_LAST_NAME);
        assertTrue(result.contains(USERNAME_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }

    @Test
    public void testInvalidPassword() {
        String result = registrationLogic.registerUser(VALID_USERNAME, INVALID_PASSWORD, VALID_CELL_PHONE, VALID_FIRST_NAME, VALID_LAST_NAME);
        assertTrue(result.contains(PASSWORD_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }

    @Test
    public void testInvalidCellPhone() {
        String result = registrationLogic.registerUser(VALID_USERNAME, VALID_PASSWORD, INVALID_CELL_PHONE, VALID_FIRST_NAME, VALID_LAST_NAME);
        assertTrue(result.contains(CELL_PHONE_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }

    @Test
    public void testInvalidFirstName() {
        String result = registrationLogic.registerUser(VALID_USERNAME, VALID_PASSWORD, VALID_CELL_PHONE, INVALID_FIRST_NAME, VALID_LAST_NAME);
        assertTrue(result.contains(FIRST_NAME_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }

    @Test
    public void testInvalidLastName() {
        String result = registrationLogic.registerUser(VALID_USERNAME, VALID_PASSWORD, VALID_CELL_PHONE, VALID_FIRST_NAME, INVALID_LAST_NAME);
        assertTrue(result.contains(LAST_NAME_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }

    @Test
    public void testAllInvalid() {
        String result = registrationLogic.registerUser(INVALID_USERNAME, INVALID_PASSWORD, INVALID_CELL_PHONE, INVALID_FIRST_NAME, INVALID_LAST_NAME);
        assertTrue(result.contains(USERNAME_ERROR));
        assertTrue(result.contains(PASSWORD_ERROR));
        assertTrue(result.contains(CELL_PHONE_ERROR));
        assertTrue(result.contains(FIRST_NAME_ERROR));
        assertTrue(result.contains(LAST_NAME_ERROR));
        assertTrue(result.contains(REGISTRATION_ABORTED));
    }
    
}
