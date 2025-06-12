package st10486338;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Onthatile Lesatsi
 * This tests the MessageLogic class to make sure it handles one message correctly.
 */
public class MessageLogicTest {

    // Test a totally valid message
    @Test
    public void testValidMessageCreation() {
        MessageLogic msg = new MessageLogic("kyl_1", "+27834557896", "Did you get the cake?");
        assertNotNull(msg.getMessageID(), "Message ID should not be null.");
        assertEquals(10, msg.getMessageID().length(), "Message ID should be 10 digits long.");
        assertEquals("kyl_1", msg.getSender());
        assertEquals("+27834557896", msg.getMessageRecipient());
        assertEquals("Did you get the cake?", msg.getMessagePayload());
        assertTrue(msg.isRecipientNumberValid(), "A valid number should return true.");
        assertTrue(msg.isPayloadLengthValid(), "A valid length payload should return true.");
    }

    // Test a number that's formatted wrong
    @Test
    public void testInvalidRecipientNumber() {
        // This number is missing the country code
        MessageLogic msg = new MessageLogic("kyl_1", "0834557896", "Test message");
        assertFalse(msg.isRecipientNumberValid(), "An invalid number format should return false.");
    }

    // Test a message that's way too long
    @Test
    public void testInvalidPayloadLength() {
        // Making a super long string
        String longPayload = "a".repeat(251); 
        MessageLogic msg = new MessageLogic("kyl_1", "+27834557896", longPayload);
        assertFalse(msg.isPayloadLengthValid(), "A payload longer than 250 chars should return false.");
    }
    
    // Test that the status gets set correctly
    @Test
    public void testSetMessageStatus() {
        MessageLogic msg = new MessageLogic("test_1", "+27111111111", "Testing status");
        assertNull(msg.getStatus(), "Status should be null at the start.");
        
        msg.setStatus(MessageLogic.MessageStatus.SENT);
        assertEquals(MessageLogic.MessageStatus.SENT, msg.getStatus(), "Status should be updated to SENT.");
    }
}
