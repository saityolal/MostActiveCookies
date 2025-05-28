package com.cookiefinder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cookiefinder.model.Cookie;
import com.cookiefinder.service.BinarySearchCookieFinder;
import com.cookiefinder.service.CookieAnalyticsService;
import com.cookiefinder.service.CookieLogReader;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Main application class for the Most Active Cookie Finder program. This class
 * implements the command line interface using Picocli library and provides
 * functionality to find the most active cookies for a given date.
 */
@Command(name = "cookie")
public class App implements Runnable {

    @Option(names = {"-f", "--file"}, description = "Cookie log file path", required = true)
    private String filePath;

    @Option(names = {"-d", "--date"}, description = "Date in UTC (format: yyyy-MM-dd)", required = true)
    private String date;

    private final CookieLogReader logReader = new CookieLogReader();
    private final CookieAnalyticsService analyticsService = new CookieAnalyticsService();
    private final BinarySearchCookieFinder binarySearchCookieFinder = new BinarySearchCookieFinder();

    @Override
    public void run() {
         
        try {
            LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            // List<Cookie> cookies = logReader.readLog(filePath);
            List<Cookie> cookies = binarySearchCookieFinder.findAllMatchCookies(filePath,targetDate);
            List<Cookie> mostActiveCookies = analyticsService.findMostActiveCookies(cookies, targetDate);

            if (mostActiveCookies.isEmpty()) {
                System.out.println("No active cookies found for " + targetDate);
            } else {
                System.out.println("\nMost Active Cookie(s) for " + targetDate + " was used " + mostActiveCookies.get(0).getNumOccur() + " times\n");

                for (Cookie cookie : mostActiveCookies) {
                    System.out.println(cookie.getId() + "\n");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing cookie log: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
