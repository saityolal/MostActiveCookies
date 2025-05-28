package com.cookiefinder.service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.cookiefinder.model.Cookie;

public class BinarySearchCookieFinder {

    private final int EXPECTED_COLUMNS = 2;
    private final int INDEX_ID = 0;
    private final int INDEX_TIMESTAMP = 1;
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public List<Cookie> findAllMatchCookies(String filePath, LocalDate targetDate) throws IOException {
        List<Cookie> result = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            String header = raf.readLine();
            long startPosition = binarySearch(raf, targetDate);

            if (startPosition == -1) {
                return result;
            }

            raf.seek(startPosition);
            String[] parts = raf.readLine().trim().split(",");
            OffsetDateTime timestamp = OffsetDateTime.parse(parts[INDEX_TIMESTAMP], DATE_TIME_FORMATTER);
            result.add(new Cookie(parts[INDEX_ID], timestamp));

            // Read backwards
            long prevPos = findLineStart(raf, startPosition - 1);
            readLinesUntilMismatch(raf, prevPos, targetDate, result, true, header);

            // Read forwards
            long nextPos = findNextLineStart(raf, startPosition);
            readLinesUntilMismatch(raf, nextPos, targetDate, result, false, header);
        }

        return result;
    }

    private void readLinesUntilMismatch(RandomAccessFile raf, long startPos, LocalDate targetDate, List<Cookie> result, boolean backwards, String header) throws IOException {
        long pos = startPos;

        while (pos >= 0 && pos < raf.length()) {
            raf.seek(pos);
            String line = raf.readLine();

            if (line == null || line.equals(header)) {
                break;
            }

            String[] parts = line.trim().split(",");
            if (parts.length != EXPECTED_COLUMNS) {
                throw new IOException("Invalid line format: " + line);
            }

            OffsetDateTime timestamp = OffsetDateTime.parse(parts[INDEX_TIMESTAMP], DATE_TIME_FORMATTER);
            LocalDate lineDate = timestamp.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate();

            if (!lineDate.equals(targetDate)) {
                break;
            }

            result.add(new Cookie(parts[INDEX_ID], timestamp));

            if (backwards) {
                pos = findLineStart(raf, pos - 1);
            } else {
                pos = findNextLineStart(raf, pos);
            }

        }
    }

    private long binarySearch(RandomAccessFile raf, LocalDate targetDate) {
        try {
            raf.seek(0);
            raf.readLine(); // skip header
            long min = raf.getFilePointer();
            long max = raf.length();

            while (min <= max) {
                long mid = (min + max) / 2;
                long lineStart = findLineStart(raf, mid);
                raf.seek(lineStart);
                String line = raf.readLine();

                if (line == null) {
                    break;
                }

                String[] parts = line.trim().split(",");
                if (parts.length != EXPECTED_COLUMNS) {
                    throw new IOException("Invalid line: " + line);
                }

                OffsetDateTime timestamp = OffsetDateTime.parse(parts[INDEX_TIMESTAMP], DATE_TIME_FORMATTER);
                LocalDate lineDate = timestamp.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate();

                int compare = lineDate.compareTo(targetDate);

                if (compare > 0) {
                    min = raf.getFilePointer() + 1;
                } else if (compare < 0) {
                    max = lineStart - 1;
                } else {
                    return lineStart;
                }
            }

        } catch (IOException e) {
            System.out.println("Error in binary search: " + e.getMessage());
        }

        return -1;
    }

    private long findLineStart(RandomAccessFile raf, long position) throws IOException {
        if (position <= 0) {
            return 0;
        }

        long pos = position;
        while (pos > 0) {
            raf.seek(pos - 1);
            int b = raf.read();
            if (b == '\n') {
                break;
            }
            pos--;
        }
        return pos;
    }

    private long findNextLineStart(RandomAccessFile raf, long position) throws IOException {
        long length = raf.length();
        long pos = position;

        while (pos < length) {
            raf.seek(pos);
            int b = raf.read();
            pos++;
            if (b == '\n') {
                break;
            }
        }

        return pos;
    }
}
