package com.cookiefinder.model;

import java.time.OffsetDateTime;

/**
 * Represents a cookie with an ID,  timestamp and number of occurrences.
 */
public class Cookie {

    private final String id;
    private final OffsetDateTime timestamp;
    private int numOccur;

    public Cookie(String id, OffsetDateTime timestamp, int numOccur) {
        if (timestamp == null) {
            throw new NullPointerException("Timestamp cannot be null");
        }
        if (id == null) {
            throw new NullPointerException("ID cannot be null");
        }
        this.id = id;
        this.timestamp = timestamp;
        this.numOccur = numOccur;
    }

    public Cookie(String id, OffsetDateTime timestamp) {
        this(id, timestamp, 1);
    }

    public String getId() {
        return id;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getNumOccur() {
        return numOccur;
    }

    public void setNumOccur(int numOccur) {
        this.numOccur = numOccur;
    }
}
