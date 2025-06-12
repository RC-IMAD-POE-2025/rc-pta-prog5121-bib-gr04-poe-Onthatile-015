package st10486338;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.io.File;

/**
 *
 * @author Onthatile Lesatsi
 * This is for testing all the new Part 3 array and reporting features.
 * The test data comes directly from the PROG5121 POE PDF page 17.
 */
public class ReportingAndArrayLogicTest {

    private ReportingAndArrayLogic reportingLogic;
    private MessageLogic msg1, msg2, msg3, msg4, msg5;

    @BeforeEach
    public void setUp() {
        // This setup runs before each test
        // It's important to clean up old files BEFORE we start a new test
        cleanupTestFiles(); 
        
        reportingLogic = new ReportingAndArrayLogic();

        // Create messages based on POE test data
        msg1 = new MessageLogic("Onthatile", "+27834557896", "Did you get the cake?");
        msg2 = new MessageLogic("Onthatile", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg3 = new MessageLogic("Onthatile", "+27834484567", "Yohoooo, I am at your gate.");
        msg4 = new MessageLogic("Onthatile", "+27838884567", "It is dinner time!");
        msg5 = new MessageLogic("Onthatile", "+27838884567", "Ok, I am leaving without you.");

        // Add them to our logic class with their status
        reportingLogic.addMessage(msg1, MessageLogic.MessageStatus.SENT);
        reportingLogic.addMessage(msg2, MessageLogic.MessageStatus.STORED);
        reportingLogic.addMessage(msg3, MessageLogic.MessageStatus.DISREGARDED);
        reportingLogic.addMessage(msg4, MessageLogic.MessageStatus.SENT);
        reportingLogic.addMessage(msg5, MessageLogic.MessageStatus.STORED);
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up any files created during the tests to not affect other tests
        cleanupTestFiles();
    }
    
    private void cleanupTestFiles() {
        File currentDir = new File(".");
        File[] files = currentDir.listFiles((dir, name) -> name.startsWith("message_") && name.endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
    
    // Helper method to check if the sent array is correct
    private ArrayList<String> getSentMessagePayloads() {
        ArrayList<String> payloads = new ArrayList<>();
        for(MessageLogic msg : reportingLogic.getAllMessagesForTesting()) {
            if (msg.getStatus() == MessageLogic.MessageStatus.SENT) {
                payloads.add(msg.getMessagePayload());
            }
        }
        return payloads;
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        // Expected from POE: "Did you get the cake?", "It is dinner time!"
        ArrayList<String> sentPayloads = getSentMessagePayloads();
        
        assertEquals(2, sentPayloads.size(), "There should be 2 sent messages.");
        assertTrue(sentPayloads.contains("Did you get the cake?"), "Test data message 1 should be in sent list.");
        assertTrue(sentPayloads.contains("It is dinner time!"), "Test data message 4 should be in sent list.");
    }
    
    @Test
    public void testDisplayTheLongestMessage() {
        String expected = "Longest message is:\n\"Did you get the cake?\"";
        String actual = reportingLogic.getLongestSentMessage();
        assertEquals(expected, actual, "The longest SENT message should be msg1.");
    }
    
    @Test
    public void testSearchForMessageID() {
        String idToSearch = msg4.getMessageID();
        String expected = "Message Found:\nRecipient: " + msg4.getMessageRecipient() + "\nMessage: " + msg4.getMessagePayload();
        String actual = reportingLogic.searchByMessageID(idToSearch);
        assertEquals(expected, actual, "Searching by ID should return the correct recipient and message.");
    }

    @Test
    public void testSearchAllMessagesForRecipient() {
        String recipientToSearch = "+27838884567";
        String actual = reportingLogic.searchByRecipient(recipientToSearch);
        
        assertTrue(actual.contains("Where are you? You are late! I have asked you to be on time."), "Should find message 2.");
        assertTrue(actual.contains("It is dinner time!"), "Should find message 4.");
        assertTrue(actual.contains("Ok, I am leaving without you."), "Should find message 5.");
    }
    
    @Test
    public void testDeleteMessageUsingMessageHash() {
        String hashToDelete = msg2.getMessageHash();
        String expected = "Message \"" + msg2.getMessagePayload() + "\" successfully deleted from session and disk.";
        String actual = reportingLogic.deleteMessageByHash(hashToDelete);
        
        assertEquals(expected, actual, "The correct deletion message should be returned.");
        
        String afterDelete = reportingLogic.searchByMessageID(msg2.getMessageID());
        assertTrue(afterDelete.contains("not found"), "The deleted message should not be found after deletion.");
    }
    
    @Test
    public void testDisplayReport() {
        String report = reportingLogic.generateFullReport();
        
        assertTrue(report.contains("Did you get the cake?"), "Report should include message 1.");
        assertTrue(report.contains("Where are you? You are late!"), "Report should include message 2.");
        assertTrue(report.contains("It is dinner time!"), "Report should include message 4.");
        assertTrue(report.contains("Ok, I am leaving without you."), "Report should include message 5.");
        
        assertFalse(report.contains("Yohoooo, I am at your gate."), "Report should NOT include disregarded message 3.");
    }
    
    @Test
    public void testStoreAndLoadFromJson() {
        // 1. Create a new logic instance (it will be empty because of the cleanup)
        ReportingAndArrayLogic logicForStore = new ReportingAndArrayLogic();
        
        // 2. Create and store a test message
        MessageLogic testMsg = new MessageLogic("json_tester", "+27999999999", "This is a JSON test.");
        logicForStore.addMessage(testMsg, MessageLogic.MessageStatus.STORED);
        logicForStore.storeMessageAsJson(testMsg);
        
        // 3. Check that the file was actually created
        File testFile = new File("message_" + testMsg.getMessageID() + ".json");
        assertTrue(testFile.exists(), "The JSON file should be created on disk.");
        
        // 4. Create a *new* instance of the logic class, which should load the file
        ReportingAndArrayLogic logicForLoad = new ReportingAndArrayLogic();
        
        // 5. Check if the loaded message exists in the new instance
        String searchResult = logicForLoad.searchByMessageID(testMsg.getMessageID());
        assertTrue(searchResult.contains("Message Found"), "The message should be loaded from the JSON file into the new instance.");
        assertTrue(searchResult.contains("This is a JSON test."), "The payload of the loaded message should be correct.");
    }
}
