package com.cookiefinder.model;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class CookieTest {

    
    /**
     * Verifies that the constructor creates a Cookie object with the correct
     * ID, number of occurrences, and timestamp.
     */
    @Test
    void testCookieCreation() {
        String id = "testCookie123";
        int numOccur = 1;
        OffsetDateTime timestamp = OffsetDateTime.now();

        Cookie cookie = new Cookie(id, timestamp);

        assertEquals(id, cookie.getId());
        assertEquals(1, cookie.getNumOccur());
        assertEquals(timestamp, cookie.getTimestamp());
    }

    /**
     * Verifies that the constructor can handle cookie IDs with special
     * characters and the test ensures that the ID is correctly stored
     * and can be retrieved using the getId() method.
     */
    @Test
    void testCookieWithSpecialCharacters() {
        String id = "cookie@#.Ç$%^&*(ğüĞÜ¨~£{}[])_+";
        OffsetDateTime timestamp = OffsetDateTime.now();

        Cookie cookie = new Cookie(id, timestamp);

        assertEquals(id, cookie.getId());
        assertEquals(timestamp, cookie.getTimestamp());
    }

    @Test
    void testCookieWithEmptyId() {
        String id = null;
        OffsetDateTime timestamp = OffsetDateTime.now();


        assertThrows(NullPointerException.class, () -> {
            new Cookie(id, timestamp);
        });
       // assertEquals(timestamp, cookie.getTimestamp());
    }

    @Test
    void testCookieWithNullTimestamp() {
        String id = "testCookie";
        OffsetDateTime timestamp = null;

        assertThrows(NullPointerException.class, () -> {
            new Cookie(id, timestamp);
        });
    }
}
