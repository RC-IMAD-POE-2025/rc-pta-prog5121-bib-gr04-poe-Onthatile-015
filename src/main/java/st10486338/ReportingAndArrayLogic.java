package st10486338;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Onthatile Lesatsi
 * This class handles all the Part 3 stuff like arrays, reports, and JSON storage.
 */
public class ReportingAndArrayLogic {

    // Arrays to hold all our messages, as per POE Part 3
    private final ArrayList<MessageLogic> allMessages = new ArrayList<>();

    // To create unique message hashes
    private int messageCounter = 0;

    /**
     * Constructor for the logic class.
     * It automatically loads any messages that were previously stored in JSON files.
     */
    public ReportingAndArrayLogic() {
        loadStoredMessagesFromJson();
    }
    
    /**
     * This method saves a given message object to a JSON file.
     * It's used when a user chooses to "Store" a message.
     * @param message The message to save.
     * @return A status message indicating success or failure.
     */
    public String storeMessageAsJson(MessageLogic message) {
        // OpenAI, 2024. Code for writing a JSONObject to a file.
        // https://chat.openai.com/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messageID", message.getMessageID());
        jsonObject.put("sender", message.getSender());
        jsonObject.put("recipient", message.getMessageRecipient());
        jsonObject.put("payload", message.getMessagePayload());
        jsonObject.put("hash", message.getMessageHash());
        jsonObject.put("status", message.getStatus().toString());

        // Use message ID for a unique filename to avoid overwriting files
        String fileName = "message_" + message.getMessageID() + ".json";

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(jsonObject.toJSONString());
            file.flush();
            return "Message also saved to file: " + fileName;
        } catch (IOException e) {
            return "Error saving message to file: " + e.getMessage();
        }
    }

    /**
     * This method runs on startup to read all message_*.json files
     * from the project directory and load them into the main ArrayList.
     */
    private void loadStoredMessagesFromJson() {
        // OpenAI, 2024. Code for reading and parsing JSON files from a directory.
        // https://chat.openai.com/
        File currentDir = new File(".");
        // Look for any file that starts with "message_" and ends with ".json"
        File[] files = currentDir.listFiles((dir, name) -> name.startsWith("message_") && name.endsWith(".json"));

        if (files == null) {
            return; // No files found or directory error
        }

        JSONParser parser = new JSONParser();

        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                JSONObject jsonObject = (JSONObject) parser.parse(reader);

                // Read all the details from the JSON object
                String id = (String) jsonObject.get("messageID");
                String sender = (String) jsonObject.get("sender");
                String recipient = (String) jsonObject.get("recipient");
                String payload = (String) jsonObject.get("payload");
                String hash = (String) jsonObject.get("hash");
                MessageLogic.MessageStatus status = MessageLogic.MessageStatus.valueOf((String) jsonObject.get("status"));
                
                // Use the new constructor to create a message object with the loaded data
                MessageLogic loadedMsg = new MessageLogic(id, sender, recipient, payload, hash, status);
                allMessages.add(loadedMsg);

            } catch (IOException | ParseException | NullPointerException e) {
                System.out.println("Could not load message from file " + file.getName() + ". It might be corrupted. Error: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a message to our main list and sets its status.
     * This is how we populate our arrays.
     * @param message The message object to add.
     * @param status The status (Sent, Stored, Disregarded).
     */
    public void addMessage(MessageLogic message, MessageLogic.MessageStatus status) {
        message.setStatus(status);
        // Only create a hash for messages that are sent or stored
        if (status == MessageLogic.MessageStatus.SENT || status == MessageLogic.MessageStatus.STORED) {
            message.setMessageHash(createMessageHash(message.getMessageID(), message.getMessagePayload()));
        }
        // Avoid adding duplicates if they were already loaded from JSON
        boolean alreadyExists = false;
        for(MessageLogic existingMsg : allMessages){
            if(existingMsg.getMessageID().equals(message.getMessageID())){
                alreadyExists = true;
                break;
            }
        }
        if(!alreadyExists){
            allMessages.add(message);
        }
    }
    
    /**
     * Creates the message hash like the POE wants.
     * FORMAT: <first two digits of ID>:<message number>:<FIRSTWORDLASTWORD>
     * @param messageID The unique ID of the message.
     * @param messagePayload The text of the message.
     * @return The formatted hash string.
     */
    private String createMessageHash(String messageID, String messagePayload) {
        messageCounter++; // Increment for each new hash
        String firstTwoID = messageID.substring(0, 2);
        
        // Get the first and last words
        String[] words = messagePayload.trim().split("\\s+");
        String wordPart = "";
        if (words.length > 0 && !words[0].isEmpty()) {
            String firstWord = words[0].toUpperCase();
            String lastWord = words[words.length - 1].toUpperCase();
            wordPart = firstWord + lastWord;
        }

        return firstTwoID + ":" + messageCounter + ":" + wordPart;
    }
    
    // --- Reporting Methods for POE Part 3 ---

    // a. Display sender and recipient of all sent messages
    public String getSentMessagesDetails() {
        StringBuilder details = new StringBuilder("--- Sent Messages ---\n");
        boolean found = false;
        for (MessageLogic msg : allMessages) {
            if (msg.getStatus() == MessageLogic.MessageStatus.SENT) {
                details.append("Sender: ").append(msg.getSender())
                       .append(", Recipient: ").append(msg.getMessageRecipient()).append("\n");
                found = true;
            }
        }
        return found ? details.toString() : "No messages have been sent.";
    }

    // b. Display the longest sent message
    public String getLongestSentMessage() {
        MessageLogic longestMessage = null;
        for (MessageLogic msg : allMessages) {
            if (msg.getStatus() == MessageLogic.MessageStatus.SENT) {
                if (longestMessage == null || msg.getMessagePayload().length() > longestMessage.getMessagePayload().length()) {
                    longestMessage = msg;
                }
            }
        }

        if (longestMessage != null) {
            return "Longest message is:\n\"" + longestMessage.getMessagePayload() + "\"";
        } else {
            return "No sent messages to compare.";
        }
    }

    // c. Search for a message by its ID
    public String searchByMessageID(String id) {
        for (MessageLogic msg : allMessages) {
            if (msg.getMessageID().equals(id)) {
                return "Message Found:\nRecipient: " + msg.getMessageRecipient() + "\nMessage: " + msg.getMessagePayload();
            }
        }
        return "Message with ID " + id + " not found.";
    }

    // d. Search for all messages sent to a particular recipient
    public String searchByRecipient(String recipientNumber) {
        StringBuilder results = new StringBuilder("--- Messages for " + recipientNumber + " ---\n");
        boolean found = false;
        for (MessageLogic msg : allMessages) {
            if (msg.getMessageRecipient().equals(recipientNumber) && msg.getStatus() != MessageLogic.MessageStatus.DISREGARDED) {
                results.append("- \"").append(msg.getMessagePayload()).append("\"\n");
                found = true;
            }
        }
        return found ? results.toString() : "No messages found for recipient " + recipientNumber + ".";
    }

    // e. Delete a message using its hash
    public String deleteMessageByHash(String hash) {
        for (int i = 0; i < allMessages.size(); i++) {
            MessageLogic msg = allMessages.get(i);
            if (msg.getMessageHash() != null && msg.getMessageHash().equals(hash)) {
                String deletedMessagePayload = msg.getMessagePayload();
                
                // Also delete the corresponding JSON file
                File fileToDelete = new File("message_" + msg.getMessageID() + ".json");
                if(fileToDelete.exists()){
                    fileToDelete.delete();
                }
                
                allMessages.remove(i);
                return "Message \"" + deletedMessagePayload + "\" successfully deleted from session and disk.";
            }
        }
        return "Message with hash " + hash + " not found.";
    }

    // f. Display a report of all messages (not disregarded)
    public String generateFullReport() {
        if (allMessages.isEmpty()) {
            return "No messages to report.";
        }
        
        StringBuilder report = new StringBuilder("--- Full Message Report ---\n\n");
        for (MessageLogic msg : allMessages) {
             if (msg.getStatus() != MessageLogic.MessageStatus.DISREGARDED) {
                report.append(msg.getMessageDetails()).append("\n\n");
             }
        }
        return report.toString();
    }
    
    // This is a helper for the tests, not for the main app
    public ArrayList<MessageLogic> getAllMessagesForTesting() {
        return allMessages;
    }
}
