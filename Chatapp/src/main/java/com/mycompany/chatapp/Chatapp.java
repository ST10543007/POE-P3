package com.mycompany.chatapp;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Chatapp {

    private static final int MAX_LIMIT = 100;
    private static String[] sentMessagesArray = new String[MAX_LIMIT];
    private static String[] disregardedMessagesArray = new String[MAX_LIMIT];
    private static String[] storedMessagesArray = new String[MAX_LIMIT];
    private static String[] messageHashArray = new String[MAX_LIMIT];
    private static String[] messageIdArray = new String[MAX_LIMIT];
    private static String[] recipientArray = new String[MAX_LIMIT];

    private static String[] storedMessageIdArray = new String[MAX_LIMIT];
    private static String[] storedMessageHashArray = new String[MAX_LIMIT];
    private static String[] storedRecipientArray = new String[MAX_LIMIT];

    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;
    private static int totalMessagesSent = 0;

    // Automatically load Test Data 1 to 5 on startup to satisfy assignment requirements
    public static void loadTestData() {
        // Message 1: Sent (+27834557896, "Did you get the cake?")
        totalMessagesSent++;
        sentMessagesArray[sentCount] = "Did you get the cake?";
        messageIdArray[sentCount] = "0834557896";
        recipientArray[sentCount] = "+27834557896";
        messageHashArray[sentCount] = createMessageHash("0834557896", totalMessagesSent, "Did you get the cake?");
        sentCount++;

        // Message 2: Stored (+27838884567, "Where are you? You are late! I have asked you to be on time.")
        totalMessagesSent++;
        String h2 = createMessageHash("0838884567", totalMessagesSent, "Where are you? You are late! I have asked you to be on time.");
        storedMessagesArray[storedCount] = "Where are you? You are late! I have asked you to be on time.";
        storedMessageIdArray[storedCount] = "0838884567";
        storedRecipientArray[storedCount] = "+27838884567";
        storedMessageHashArray[storedCount] = h2;
        storedCount++;

        // Message 3: Disregarded (+27834484567, "Yohoooo, I am at your gate.")
        disregardedMessagesArray[disregardedCount] = "Yohoooo, I am at your gate.";
        disregardedCount++;

        // Message 4: Sent (0838884567, "It is dinner time !")
        totalMessagesSent++;
        sentMessagesArray[sentCount] = "It is dinner time !";
        messageIdArray[sentCount] = "0838884567";
        recipientArray[sentCount] = "0838884567";
        messageHashArray[sentCount] = createMessageHash("0838884567", totalMessagesSent, "It is dinner time !");
        sentCount++;

        // Message 5: Stored (+27838884567, "Ok, I am leaving without you.")
        totalMessagesSent++;
        String h5 = createMessageHash("0838884567", totalMessagesSent, "Ok, I am leaving without you.");
        storedMessagesArray[storedCount] = "Ok, I am leaving without you.";
        storedMessageIdArray[storedCount] = "0838884567";
        storedRecipientArray[storedCount] = "+27838884567";
        storedMessageHashArray[storedCount] = h5;
        storedCount++;
    }

    public static boolean checkMessageID(String messageId) {
        if (messageId == null) return false;
        return messageId.length() < 10;
    }

    public static String checkRecipientCell(String cellNumber) {
        if (cellNumber != null && cellNumber.matches("^\\+27\\d{8}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted. Please try again.";
    }

    public static String checkMessageLength(String messageText) {
        if (messageText == null) return "Message is empty.";
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int overrun = messageText.length() - 250;
        return "Message exceeds 250 characters by " + overrun + ".";
    }

    public static String createMessageHash(String messageId, int currentMsgNum, String messageText) {
        if (messageId == null || messageId.length() < 2) messageId = "ID";
        String firstTwoId = messageId.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";
        return (firstTwoId + ":" + currentMsgNum + ":" + firstWord + lastWord).toUpperCase();
    }

    public static void saveMessage(String messageId, String hash, String recipient, String messageText) {
        try (FileWriter writer = new FileWriter("stored_messages.json", true)) {
            writer.write("{\n");
            writer.write("  \"messageId\": \"" + messageId + "\",\n");
            writer.write("  \"hash\": \"" + hash + "\",\n");
            writer.write("  \"recipient\": \"" + recipient + "\",\n");
            writer.write("  \"message\": \"" + messageText + "\"\n");
            writer.write("},\n");
        } catch (IOException e) {
            System.out.println("Error saving message to file: " + e.getMessage());
        }
    }

    public static String sendOrStoreMessage(Scanner input, String messageId,
                                            String recipient, String messageText) {
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Choose an option: ");

        int choice = input.nextInt();
        input.nextLine();

        totalMessagesSent++;
        String hash = createMessageHash(messageId, totalMessagesSent, messageText);

        if (choice == 1) {
            sentMessagesArray[sentCount] = messageText;
            messageHashArray[sentCount] = hash;
            messageIdArray[sentCount] = messageId;
            recipientArray[sentCount] = recipient;
            sentCount++;

            System.out.println("Message successfully sent.");
            return "Message successfully sent.";
        } else if (choice == 2) {
            disregardedMessagesArray[disregardedCount] = messageText;
            disregardedCount++;
            System.out.println("Message disregarded.");
            return "Message disregarded.";
        } else if (choice == 3) {
            storedMessagesArray[storedCount] = messageText;
            storedMessageIdArray[storedCount] = messageId;
            storedMessageHashArray[storedCount] = hash;
            storedRecipientArray[storedCount] = recipient;
            storedCount++;

            saveMessage(messageId, hash, recipient, messageText);
            System.out.println("Message successfully stored.");
            return "Message successfully stored.";
        }

        return "Invalid choice.";
    }

    public static String getLongestStoredMessageText() {
        if (storedCount == 0) return "No messages stored.";
        String longest = storedMessagesArray[0];
        for (int i = 1; i < storedCount; i++) {
            if (storedMessagesArray[i].length() > longest.length()) {
                longest = storedMessagesArray[i];
            }
        }
        return longest;
    }

    public static void displayStoredMenu(Scanner input) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STORED MESSAGES MENU ---");
            System.out.println("a) Display the sender and recipient of all stored messages[cite: 2].");
            System.out.println("b) Display the longest stored message[cite: 2].");
            System.out.println("c) Search for a message ID and display recipient and message[cite: 2].");
            System.out.println("d) Search for all messages stored for a particular recipient[cite: 2].");
            System.out.println("e) Delete a message using the message hash[cite: 2].");
            System.out.println("f) Display a report of all stored messages[cite: 2].");
            System.out.println("g) Return to Main Menu");
            System.out.print("Choose an option (a-g): ");

            String opt = input.nextLine().trim().toLowerCase();
            switch (opt) {
                case "a":
                    if (storedCount == 0) {
                        System.out.println("No stored messages.");
                    } else {
                        for (int i = 0; i < storedCount; i++) {
                            System.out.println("Recipient: " + storedRecipientArray[i] + " | Message ID: " + storedMessageIdArray[i]);
                        }
                    }
                    break;
                case "b":
                    System.out.println("Longest Stored Message: " + getLongestStoredMessageText());
                    break;
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String searchId = input.nextLine();
                    boolean foundId = false;
                    for (int i = 0; i < storedCount; i++) {
                        if (storedMessageIdArray[i].equalsIgnoreCase(searchId)) {
                            System.out.println("Recipient: " + storedRecipientArray[i] + " | Message: " + storedMessagesArray[i]);
                            foundId = true;
                        }
                    }
                    if (!foundId) System.out.println("No stored message found with ID: " + searchId);
                    break;
                case "d":
                    System.out.print("Enter Recipient cell number to search: ");
                    String searchRec = input.nextLine();
                    boolean foundRec = false;
                    for (int i = 0; i < storedCount; i++) {
                        if (storedRecipientArray[i].equalsIgnoreCase(searchRec)) {
                            System.out.println("Message: " + storedMessagesArray[i]);
                            foundRec = true;
                        }
                    }
                    if (!foundRec) System.out.println("No messages found for recipient: " + searchRec);
                    break;
                case "e":
                    System.out.print("Enter Message Hash to delete: ");
                    String searchHash = input.nextLine().toUpperCase();
                    boolean deleted = false;
                    for (int i = 0; i < storedCount; i++) {
                        if (storedMessageHashArray[i].equals(searchHash)) {
                            System.out.println("Message: \"" + storedMessagesArray[i] + "\" successfully deleted.");
                            for (int j = i; j < storedCount - 1; j++) {
                                storedMessagesArray[j] = storedMessagesArray[j + 1];
                                storedMessageIdArray[j] = storedMessageIdArray[j + 1];
                                storedMessageHashArray[j] = storedMessageHashArray[j + 1];
                                storedRecipientArray[j] = storedRecipientArray[j + 1];
                            }
                            storedCount--;
                            deleted = true;
                            break;
                        }
                    }
                    if (!deleted) System.out.println("Message hash not found.");
                    break;
                case "f":
                    if (storedCount == 0) {
                        System.out.println("No stored messages to report.");
                    } else {
                        System.out.println("\n--- STORED MESSAGES REPORT ---");
                        for (int i = 0; i < storedCount; i++) {
                            System.out.println("Hash: " + storedMessageHashArray[i]);
                            System.out.println("Recipient: " + storedRecipientArray[i]);
                            System.out.println("Message: " + storedMessagesArray[i]);
                            System.out.println("-----------------------------------");
                        }
                    }
                    break;
                case "g":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        // Automatically load test data 1-5 on startup
        loadTestData();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== CHAT APP MAIN MENU ===");
            System.out.println("1. Send a new message");
            System.out.println("2. Display Sent Messages Report");
            System.out.println("3. Display Disregarded Messages");
            System.out.println("4. Stored Messages Menu");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    System.out.print("Enter Message ID: ");
                    String mId = scanner.nextLine();
                    System.out.print("Enter Recipient Cell Number (+27XXXXXXXXX): ");
                    String cell = scanner.nextLine();
                    System.out.print("Enter Message Text: ");
                    String text = scanner.nextLine();

                    String lenCheck = checkMessageLength(text);
                    System.out.println(lenCheck);
                    if (!lenCheck.contains("ready to send")) break;

                    String cellCheck = checkRecipientCell(cell);
                    System.out.println(cellCheck);
                    if (!cellCheck.contains("successfully captured")) break;

                    sendOrStoreMessage(scanner, mId, cell, text);
                    break;
                case 2:
                    System.out.println("\n--- SENT MESSAGES REPORT ---");
                    for (int i = 0; i < sentCount; i++) {
                        System.out.println("Hash: " + messageHashArray[i] + " | Recipient: " + recipientArray[i] + " | Message: " + sentMessagesArray[i]);
                    }
                    break;
                case 3:
                    System.out.println("\n--- DISREGARDED MESSAGES ---");
                    for (int i = 0; i < disregardedCount; i++) {
                        System.out.println(disregardedMessagesArray[i]);
                    }
                    break;
                case 4:
                    displayStoredMenu(scanner);
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 5.");
            }
        }
        scanner.close();
    }
}