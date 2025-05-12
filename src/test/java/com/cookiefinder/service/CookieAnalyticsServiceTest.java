package com.cookiefinder.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cookiefinder.model.Cookie;

class CookieAnalyticsServiceTest {

    private final CookieAnalyticsService service = new CookieAnalyticsService();

    /**
     * Test that the service will return a single most active cookie if there is
     * only one with the highest number of occurrences.
     */
    @Test
    void findMostActiveCookies_SingleActiveCookie() {
        List<Cookie> cookies = List.of(
                new Cookie("Cookie A", OffsetDateTime.parse("2023-10-01T14:19:00+00:00")),
                new Cookie("Cookie A", OffsetDateTime.parse("2023-10-01T10:13:00+00:00")),
                new Cookie("Cookie B", OffsetDateTime.parse("2023-10-01T07:25:00+00:00"))
        );

        List<Cookie> result = service.findMostActiveCookies(cookies, OffsetDateTime.parse("2023-10-01T00:00:00+00:00").toLocalDate());
        assertEquals(1, result.size());

        boolean foundCookieA = false;
        for (Cookie cookie : result) {
            if (cookie.getId().equals("Cookie A")) {
                foundCookieA = true;
            }
        }

        assertTrue(foundCookieA);
    }

    /**
     * Test that the service will return all most active cookies if there are
     * multiple with the highest number of occurrences.
     */
    @Test
    void findMostActiveCookies_MultipleActiveCookie() {
        List<Cookie> cookies = List.of(
                new Cookie("Cookie A", OffsetDateTime.parse("2023-10-01T06:19:00+00:00")),
                new Cookie("Cookie B", OffsetDateTime.parse("2023-10-01T22:03:00+00:00")),
                new Cookie("Cookie A", OffsetDateTime.parse("2023-10-01T21:30:00+00:00")),
                new Cookie("Cookie B", OffsetDateTime.parse("2023-10-01T09:30:00+00:00"))
        );

        List<Cookie> result = service.findMostActiveCookies(cookies, OffsetDateTime.parse("2023-10-01T00:00:00+00:00").toLocalDate());
        assertEquals(2, result.size());

        boolean foundCookieA = false;
        boolean foundCookieB = false;

        for (Cookie cookie : result) {
            if (cookie.getId().equals("Cookie A")) {
                foundCookieA = true;
            }
            if (cookie.getId().equals("Cookie B")) {
                foundCookieB = true;
            }
        }

        assertTrue(foundCookieA);
        assertTrue(foundCookieB);
    }

    // Returns an empty list when no cookies match the specified date
    @Test
    public void findMostActiveCookies_NoMatchingCookies() {

        CookieAnalyticsService service = new CookieAnalyticsService();
        LocalDate testDate = LocalDate.of(2022, 12, 9);

        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie("Cookie A", OffsetDateTime.parse("2018-12-08T10:00+00:00")));
        cookies.add(new Cookie("Cookie B", OffsetDateTime.parse("2018-12-07T11:00+00:00")));
        cookies.add(new Cookie("Cookie C", OffsetDateTime.parse("2018-12-06T12:00+00:00")));
        cookies.add(new Cookie("Cookie D", OffsetDateTime.parse("2018-12-05T13:00+00:00")));

        List<Cookie> result = service.findMostActiveCookies(cookies, testDate);

        assertTrue(result.isEmpty());
    }
}
