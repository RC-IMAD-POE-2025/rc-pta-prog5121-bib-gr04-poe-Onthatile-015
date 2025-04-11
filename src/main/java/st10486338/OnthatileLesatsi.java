package st10486338;

import javax.swing.JFrame;

/**
 * @author Onthatile Lesatsi
 */
public class OnthatileLesatsi 
{
    // Main method to get this party started
    public static void main(String[] args) 
    {
        // Set up the core stuff we need
        RegistrationLogic registrationLogic = new RegistrationLogic();
        LoginLogic loginLogic = new LoginLogic(registrationLogic);

        // Kick off with the registration form
        RegistrationUI regGUI = new RegistrationUI(registrationLogic, loginLogic);
        
        // Make it close the app when done and look nice in the middle
        regGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regGUI.setLocationRelativeTo(null); // Centers it on screen
        regGUI.setVisible(true); // Let’s see it!
    }
}