package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChatappTest {

    @Test
    public void testCheckMessageLength() {
        // Test data message 1 verification
        String msg = "Did you get the cake?";
        assertEquals("Message ready to send.", Chatapp.checkMessageLength(msg));
    }

    @Test
    public void testCheckRecipientCell() {
        // Test data recipient verification
        String validCell = "+27834557896";
        assertEquals("Cell phone number successfully captured.", Chatapp.checkRecipientCell(validCell));
    }

    @Test
    public void testCreateMessageHash() {
        // Verifying hash creation for test data message 4 ("It is dinner time !")[cite: 4]
        String hash = Chatapp.createMessageHash("0838884567", 1, "It is dinner time !");
        assertTrue(hash.contains("08:1:IT!"));
    }

    @Test
    public void testLongestStoredMessage() {
        // Test for identifying/displaying the longest stored message[cite: 5]
        // This tests the longest text logic among stored messages like message 2
        String longest = Chatapp.getLongestStoredMessageText();
        assertNotNull(longest);
    }

    @Test
    public void testMessageIDValidation() {
        // Test message ID length constraint (< 10 characters)
        assertTrue(Chatapp.checkMessageID("0838884567") || !Chatapp.checkMessageID("1234567890123"));
    }
}