package com.cookiefinder.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cookiefinder.model.Cookie;

public class CookieAnalyticsService {

    /**
     * This method takes a list of cookies and a target date, then determines
     * which cookies appeared the most on that date.
     * It returns a list of cookie IDs which is name of the cookie 
     * that have the highest frequency of occurrence on the specified date.
     * @param cookies a list of Cookie objects to analyze
     * @param date the target LocalDate for which to find the most active cookies.
     * @return a list of cookie IDs that are the most active on the specified
     * date. Returns an empty list if no cookies are found for the given date.
     */
    public List<Cookie> findMostActiveCookies(List<Cookie> cookies, LocalDate date) {
        if (cookies == null || cookies.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> cookieCounts = new HashMap<>();
        int maxCookieCount = 0;
        List<Cookie> mostActiveCookies = new ArrayList<>();

        for (Cookie cookie : cookies) {
            OffsetDateTime cookieTime = cookie.getTimestamp();
            if (cookieTime.toLocalDate().equals(date)) {
                String cookieId = cookie.getId();
                int count = cookieCounts.getOrDefault(cookieId, 0) + 1;
                cookieCounts.put(cookieId, count);

                if (count > maxCookieCount) {
                    maxCookieCount = count;
                    mostActiveCookies.clear();
                    cookie.setNumOccur(count);
                    mostActiveCookies.add(cookie);
                } else if (count == maxCookieCount) {
                    cookie.setNumOccur(count);
                    mostActiveCookies.add(cookie);
                }
            } else if (cookieTime.toLocalDate().isBefore(date)) {
                break;
            }
        }

        return mostActiveCookies;
    }

}
