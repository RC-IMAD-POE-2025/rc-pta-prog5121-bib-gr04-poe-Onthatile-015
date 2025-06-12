package st10486338;

import javax.swing.JOptionPane;

/**
 * Handles the user interface for messaging features using JOptionPane dialogs.
 *
 * @author Onthatile Lesatsi
 */
public class MessageUI {
    // This will handle our new array and reporting logic
    private final ReportingAndArrayLogic reportingLogic;
    // This will hold the username of the logged-in user
    private final String currentSender;

    /**
     * Constructor for the Message UI
     * @param senderIdentifier The username of the person who logged in.
     */
    public MessageUI(String senderIdentifier) {
        this.reportingLogic = new ReportingAndArrayLogic();
        this.currentSender = senderIdentifier; // Keep track of who is sending
    }

    /**
     * Starts and manages the interactive messaging session.
     */
    public void startMessagingInteraction() {
        JOptionPane.showMessageDialog(null,
                "Welcome to QuickChat, " + currentSender + "!",
                "QuickChat", JOptionPane.INFORMATION_MESSAGE);

        boolean continueMessaging = true;
        while (continueMessaging) {
            String[] options = {"Send Messages", "View Reports", "Quit"};
            int choice = JOptionPane.showOptionDialog(null,
                    "What would you like to do?",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0: // Send Messages
                    processMessageBatch();
                    break;
                case 1: // View Reports
                    showReportingMenu();
                    break;
                case 2: // Quit
                default: // Also handles closing the dialog
                    continueMessaging = false;
                    break;
            }
        }
        JOptionPane.showMessageDialog(null, "Exiting QuickChat. Goodbye!", "Exit", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    
    /**
     * Handles sending a bunch of messages.
     */
    private void processMessageBatch() {
        String numMessagesStr = JOptionPane.showInputDialog(null,
                "How many messages would you like to process?",
                "Number of Messages", JOptionPane.QUESTION_MESSAGE);
        
        if (numMessagesStr == null) return; // User cancelled

        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Please enter digits only.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < numMessages; i++) {
            String recipient = JOptionPane.showInputDialog(null, "Message " + (i + 1) + "/" + numMessages + "\nEnter recipient's cell (+27...):", "Recipient", JOptionPane.PLAIN_MESSAGE);
            if (recipient == null) continue; // Skip if user cancels

            String payload = JOptionPane.showInputDialog(null, "Enter message (max 250 chars):", "Message Text", JOptionPane.PLAIN_MESSAGE);
            if (payload == null) continue; // Skip if user cancels
            
            // Create the message object
            MessageLogic currentMessage = new MessageLogic(currentSender, recipient, payload);

            // Check if details are valid before doing anything else
            if (!currentMessage.isRecipientNumberValid()) {
                JOptionPane.showMessageDialog(null, "Invalid phone number format. Must be like +27123456789.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (!currentMessage.isPayloadLengthValid()) {
                JOptionPane.showMessageDialog(null, "Message is too long. Max 250 characters.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Ask user what to do with the message
            String[] actions = {"Send", "Store for Later", "Disregard"};
            int actionChoice = JOptionPane.showOptionDialog(null, "What to do with this message?", "Action",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, actions, actions[0]);

            switch (actionChoice) {
                case 0: // Send
                    reportingLogic.addMessage(currentMessage, MessageLogic.MessageStatus.SENT);
                    JOptionPane.showMessageDialog(null, "Message sent!\nHash: " + currentMessage.getMessageHash(), "Success", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1: // Store for Later
                    reportingLogic.addMessage(currentMessage, MessageLogic.MessageStatus.STORED);
                    // Also save it to a JSON file for the next time we run the app
                    String saveResult = reportingLogic.storeMessageAsJson(currentMessage);
                    JOptionPane.showMessageDialog(null, "Message stored in this session.\n" + saveResult, "Success", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2: // Disregard
                default:
                    reportingLogic.addMessage(currentMessage, MessageLogic.MessageStatus.DISREGARDED);
                    JOptionPane.showMessageDialog(null, "Message disregarded.", "Disregarded", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }
    }
    
    /**
     * Shows the new reporting menu for Part 3.
     */
    private void showReportingMenu() {
        boolean inReportMenu = true;
        while(inReportMenu) {
            String[] reportOptions = {
                "Show Sent Senders/Recipients", 
                "Show Longest Sent Message", 
                "Search by Message ID",
                "Search by Recipient",
                "Delete Message by Hash",
                "Display Full Report",
                "Back to Main Menu"
            };
            int reportChoice = JOptionPane.showOptionDialog(null, "Select a report to view:", "Reporting Menu", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, reportOptions, reportOptions[0]);

            String result;
            String userInput;

            switch (reportChoice) {
                case 0: // Show Sent Senders/Recipients
                    result = reportingLogic.getSentMessagesDetails();
                    JOptionPane.showMessageDialog(null, result, "Sent Message Details", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1: // Show Longest Sent Message
                    result = reportingLogic.getLongestSentMessage();
                    JOptionPane.showMessageDialog(null, result, "Longest Sent Message", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2: // Search by Message ID
                    userInput = JOptionPane.showInputDialog("Enter the Message ID to search for:");
                    if (userInput != null) {
                        result = reportingLogic.searchByMessageID(userInput);
                        JOptionPane.showMessageDialog(null, result, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 3: // Search by Recipient
                    userInput = JOptionPane.showInputDialog("Enter the recipient's number to search for:");
                    if (userInput != null) {
                        result = reportingLogic.searchByRecipient(userInput);
                        JOptionPane.showMessageDialog(null, result, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 4: // Delete Message by Hash
                    userInput = JOptionPane.showInputDialog("Enter the Message Hash to delete:");
                    if (userInput != null) {
                        result = reportingLogic.deleteMessageByHash(userInput);
                        JOptionPane.showMessageDialog(null, result, "Delete Result", JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
                case 5: // Display Full Report
                    result = reportingLogic.generateFullReport();
                    JOptionPane.showMessageDialog(null, result, "Full Report", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 6: // Back to Main Menu
                default:
                    inReportMenu = false;
                    break;
            }
        }
    }
}
