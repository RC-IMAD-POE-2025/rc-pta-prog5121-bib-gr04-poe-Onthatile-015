package st10486338;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Onthatile Lesatsi
 */
public class MessageLogicTest {
    private MessageLogic message;

    @BeforeEach
    public void setUp() {
        MessageLogic.resetMessageCounterForTesting();
        message = new MessageLogic("+27718693002", "Hi Mike, can you join us for dinner tonight");
    }

    @Test
    public void testMessageConstructor_InitializesPropertiesCorrectly() {
        MessageLogic msg = new MessageLogic("testRecipient", "testPayload");
        assertNotNull(msg.getMessageID(), "Message ID should be automatically generated and not null.");
        assertTrue(msg.getMessageID().matches("\\d{10}"), "Message ID should consist of 10 digits.");
        assertEquals("testRecipient", msg.getMessageRecipient(), "Recipient should match the constructor argument.");
        assertEquals("testPayload", msg.getMessagePayload(), "Payload should match the constructor argument.");
        assertEquals(0, msg.getMessageIndex(), "Initial message index should be 0.");
        assertEquals("", msg.getMessageHash(), "Initial message hash should be an empty string.");
    }

    @Test
    public void testValidateRecipientNumber_ValidFormat_ReturnsSuccessMessage() {
        assertEquals("Cell phone number successfully captured.", message.validateRecipientNumber("+27123456789"), "Correctly formatted South African number should be valid.");
    }

    @Test
    public void testValidateRecipientNumber_InvalidFormat_ReturnsFailureMessage() {
        String expectedError = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        assertEquals(expectedError, message.validateRecipientNumber("0712345678"), "Number missing '+27' prefix should be invalid.");
    }

    @Test
    public void testValidatePayloadLength_ValidPayload_ReturnsSuccessMessage() {
        String shortPayload = "Hello";
        assertEquals("Message ready to send.", message.validatePayloadLength(shortPayload), "Short payload should be valid.");
    }

    @Test
    public void testValidatePayloadLength_TooLongPayload_ReturnsFailureMessageWithExcessCount() {
        String longPayload = new String(new char[251]).replace('\0', 'a');
        assertEquals("Message exceeds 250 characters by 1, please reduce size.", message.validatePayloadLength(longPayload), "Payload exceeding max length by 1 char should report correctly.");
    }

    @Test
    public void testSentMessage_InvalidRecipient_ReturnsFailure() {
        MessageLogic msg = new MessageLogic("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Failed to send message: Invalid recipient", msg.sentMessage(), "sentMessage should return failure for an invalid recipient.");
    }

    @Test
    public void testSentMessage_PayloadTooLong_ReturnsFailure() {
        String longPayload = new String(new char[251]).replace('\0', 'a');
        MessageLogic msg = new MessageLogic("+27718693002", longPayload);
        assertEquals("Failed to send message: Payload too long", msg.sentMessage(), "sentMessage should return failure for a payload exceeding maximum length.");
    }

    @Test
    public void testSentMessage_EmptyPayload_ReturnsFailure() {
        MessageLogic msg = new MessageLogic("+27718693002", "   ");
        assertEquals("Failed to send message: Message content cannot be empty", msg.sentMessage(), "sentMessage should return failure for an empty payload.");
    }
}