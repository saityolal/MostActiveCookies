package com.cookiefinder.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.cookiefinder.model.Cookie;

public class CookieLogReader {

    /**
     * Reads a cookie log file and returns a list of Cookie objects.
     * The first  line of the file is skipped as it is assumed to be a header.
     * @param filePath the path to the cookie log file.
     * @return a list of Cookie objects parsed from the log file.
     * @throws IOException if an error occurs while reading the file.
     * @throws IllegalArgumentException if a line in the log file is not formatted correctly.
     */
    private static final int EXPECTED_COLUMNS = 2;
    private static final int INDEX_ID = 0;
    private static final int INDEX_TIMESTAMP = 1;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public List<Cookie> readLog(String filePath) throws IOException {
        List<Cookie> cookies = new ArrayList<>();

        // Using FileInputStream and InputStreamReader to handle UTF-8 encoding, assumming cookies can contain special characters
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            reader.readLine(); // To skip the first line 
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {

                currentLine = currentLine.trim();
                String[] parts = currentLine.split(",");
                if (parts.length != EXPECTED_COLUMNS) {
                    throw new IllegalArgumentException("Invalid log format: " + currentLine);
                }
                String cookieId = parts[INDEX_ID];
              
                try {
                    OffsetDateTime timestamp = OffsetDateTime.parse(parts[INDEX_TIMESTAMP], DATE_TIME_FORMATTER);
                    timestamp = timestamp.withOffsetSameInstant(ZoneOffset.UTC);
                    cookies.add(new Cookie(cookieId, timestamp));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid timestamp format: " + e);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading log file: " + e.getMessage(), e);
        }

        return cookies;
    }
}
