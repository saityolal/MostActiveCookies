package com.cookiefinder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

import picocli.CommandLine;

public class AppTest {

    private static final String testFile = "src/test/resources/test.csv";

    /**
     * Error should be thrown for invalid date format.
     */
    @Test
    void testInvalidDate() {
        App app = new App();
        String[] args = {"-f", testFile, "-d", "invalid-date"};

        int exitCode = new CommandLine(app).execute(args);
        assertNotEquals(0, exitCode);
    }

    /**
     * Error should be thrown for invalid file path.
     */
    @Test
    void testInvalidFile() {
        App app = new App();
        String[] args = {"-f", "non-exist.csv", "-d", "2024-01-01"};

        int exitCode = new CommandLine(app).execute(args);
        assertNotEquals(0, exitCode);
    }

    /**
     * Error should be thrown for missing required parameters.
     */
    @Test
    void testMissingRequiredParameters() {
        App app = new App();
        String[] args = {"-f", testFile}; 

        int exitCode = new CommandLine(app).execute(args);
        assertNotEquals(0, exitCode);
    }
}
