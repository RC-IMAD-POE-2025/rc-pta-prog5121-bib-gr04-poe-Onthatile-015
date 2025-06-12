package st10486338;

import java.util.Random;

/**
 *
 * @author Onthatile Lesatsi
 * This class just holds all the info for a single message.
 */
public class MessageLogic {
    // Info for one message
    private String messageID;
    private String messageRecipient;
    private String messagePayload;
    private String messageHash;
    private String sender; // Who sent the message
    
    // To keep track of the message status
    private MessageStatus status;

    public enum MessageStatus {
        SENT,
        STORED,
        DISREGARDED
    }

    /**
     * Constructor to create a new message during runtime.
     * It automatically generates a unique message ID.
     */
    public MessageLogic(String sender, String recipient, String payload) {
        this.sender = sender;
        this.messageRecipient = recipient;
        this.messagePayload = payload;
        
        // Make a random 10-digit ID for the message
        Random rand = new Random();
        this.messageID = String.format("%010d", rand.nextInt(1000000000));
        
        // Not processed yet
        this.messageHash = ""; 
        this.status = null; 
    }
    
    /**
     * New constructor for loading a complete message from a data source like a JSON file.
     * This allows us to re-create a message object exactly as it was saved.
     */
    public MessageLogic(String id, String sender, String recipient, String payload, String hash, MessageStatus status) {
        this.messageID = id;
        this.sender = sender;
        this.messageRecipient = recipient;
        this.messagePayload = payload;
        this.messageHash = hash;
        this.status = status;
    }


    // --- Getters to access message info ---
    public String getMessageID() {
        return messageID;
    }

    public String getMessageRecipient() {
        return messageRecipient;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public String getMessageHash() {
        return messageHash;
    }
    
    public String getSender() {
        return sender;
    }

    public MessageStatus getStatus() {
        return status;
    }

    // --- Setters to update message info ---
    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }
    
    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    // --- Validation Methods from POE ---

    // Check if the phone number looks right (+27 and 9 more digits)
    public boolean isRecipientNumberValid() {
        // OpenAI, 2024. Regular expression for South African phone number.
        // https://chat.openai.com/
        return messageRecipient != null && messageRecipient.matches("^\\+27\\d{9}$");
    }

    // Check if the message isn't too long
    public boolean isPayloadLengthValid() {
        return messagePayload != null && messagePayload.length() <= 250;
    }
    
    // Get a nice string with all the details of this message
    public String getMessageDetails() {
        return "Message Hash: " + messageHash + "\n" +
               "Sender: " + sender + "\n" +
               "Recipient: " + messageRecipient + "\n" +
               "Message: " + messagePayload + "\n" +
               "Status: " + status;
    }
}
