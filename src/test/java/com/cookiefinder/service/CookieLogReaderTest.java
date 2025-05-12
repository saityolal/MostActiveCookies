package com.cookiefinder.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cookiefinder.model.Cookie;

class CookieLogReaderTest {

    /**
     * Tests readLog to ensure it correctly reads a cookie log file, parses
     * cookies and timestamps, and verifies the correct number of cookies and
     * accurate parsing of IDs and timestamps.
     *
     * @throws IOException if an I/O error occurs during file operations
     */
    @Test
    public void testReadCookieLogFile() throws IOException {
        String testFile = "src/test/resources/test.csv";

        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("cookie,timestamp\n");
            writer.write("cookie1,2021-12-09T14:19:00.00+00:00\n");
            writer.write("cookie2,2021-12-09T14:20:00.00+00:00\n");
            writer.write("cookie3,2021-12-09T14:21:00.00+00:00\n");
        }

        CookieLogReader cookieLogReader = new CookieLogReader();
        List<Cookie> cookies = cookieLogReader.readLog(testFile);

        assertEquals(3, cookies.size());
        assertEquals("cookie1", cookies.get(0).getId());
        assertEquals(OffsetDateTime.parse("2021-12-09T14:19:00.00+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                cookies.get(0).getTimestamp());
        assertEquals("cookie2", cookies.get(1).getId());
        assertEquals("cookie3", cookies.get(2).getId());
    }

    /**
     * Checks if the readLog method throws an IOException with a clear error
     * message when a non-existent file path is given
     */
    @Test
    public void testFileNotFound() {

        String nonExistentFilePath = "non_existent_file.csv";
        CookieLogReader cookieLogReader = new CookieLogReader();

        IOException exception = assertThrows(IOException.class, () -> {
            cookieLogReader.readLog(nonExistentFilePath);
        });

        assertTrue(exception.getMessage().contains("Error reading log file"));
    }

    /**
     * Tests if readLog correctly handles time zone offsets in date-time strings
     * by parsing a CSV file with a +06:00 offset and setting the correct UTC
     * timestamp
     *
     * @throws IOException if an I/O error occurs during file operations
     */
    @Test
    public void testTimeZoneOffsetDateTime() throws IOException {
        String testFile = "src/test/resources/test.csv";

        try (FileWriter writer = new FileWriter(testFile)) {

            writer.write("cookie,timestamp\n");
            writer.write("cookie,2021-12-09T02:21:00.00+06:00\n");
        }

        CookieLogReader cookieLogReader = new CookieLogReader();
        List<Cookie> cookies = cookieLogReader.readLog(testFile);

        assertEquals(1, cookies.size());
        assertEquals("cookie", cookies.get(0).getId());
        assertEquals("2021-12-08T20:21Z", cookies.get(0).getTimestamp().toString());

    }
}
