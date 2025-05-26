package st10486338;

import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.Random;

/**
 *
 * @author Onthatile Lesatsi
 */
public class MessageLogic {
    private String messageID;
    private String messageRecipient;
    private String messagePayload;
    private int messageIndex;
    private String messageHash;
    private static int totalMessages = 0;

    public MessageLogic(String recipient, String payload) {
        this.messageRecipient = recipient;
        this.messagePayload = payload;
        this.messageIndex = 0;
        this.messageHash = "";
        Random rand = new Random();
        this.messageID = String.format("%010d", Math.abs(rand.nextInt(1000000000)));
    }

    public String validateRecipientNumber(String recipient) {
        if (recipient == null || !recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        } else {
            return "Cell phone number successfully captured.";
        }
    }

    public String validatePayloadLength(String payload) {
        if (payload == null) {
            return "Message exceeds 250 characters by -250, please reduce size.";
        }
        int length = payload.length();
        if (length > 250) {
            int excess = length - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        } else {
            return "Message ready to send.";
        }
    }

    public String createMessageHash(String messageID, int messageIndex, String messagePayload) {
        String firstTwoID = messageID.substring(0, 2).toUpperCase();
        String wordPart = "";
        String[] words = messagePayload.trim().split("\\s+");
        if (words.length >= 2) {
            wordPart = words[0].toUpperCase() + words[words.length - 1].toUpperCase();
        } else if (words.length == 1) {
            wordPart = words[0].toUpperCase() + words[0].toUpperCase();
        }
        return firstTwoID + ":" + messageIndex + ":" + wordPart;
    }

    public String sentMessage() {
        if (messagePayload == null || messagePayload.trim().isEmpty()) {
            return "Failed to send message: Message content cannot be empty";
        }
        String recipientValidation = validateRecipientNumber(messageRecipient);
        if (!recipientValidation.equals("Cell phone number successfully captured.")) {
            return "Failed to send message: Invalid recipient";
        }
        String payloadValidation = validatePayloadLength(messagePayload);
        if (!payloadValidation.equals("Message ready to send.")) {
            return "Failed to send message: Payload too long";
        }
        totalMessages++;
        messageIndex = totalMessages;
        messageHash = createMessageHash(messageID, messageIndex, messagePayload);
        return "Message sent successfully";
    }

    public String storeMessage() {
        JSONObject json = new JSONObject();
        json.put("MESSAGE_ID", messageID);
        json.put("MESSAGE_RECIPIENT", messageRecipient);
        json.put("MESSAGE_PAYLOAD", messagePayload);
        json.put("MESSAGE_INDEX", messageIndex);
        json.put("MESSAGE_HASH", messageHash);
        String fileName = "message_" + messageIndex + ".json";
        try {
            FileWriter file = new FileWriter(fileName);
            file.write(json.toString());
            file.close();
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Failed to store message: " + e.getMessage();
        }
    }

    public static void resetMessageCounterForTesting() {
        totalMessages = 0;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageRecipient() {
        return messageRecipient;
    }

    public String getMessagePayload() {
        return messagePayload;
    }

    public int getMessageIndex() {
        return messageIndex;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getGeneratedIdNotification() {
        return "Message ID generated: " + messageID;
    }
}