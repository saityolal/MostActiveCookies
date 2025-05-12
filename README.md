# Most Active Cookie Finder

A Java application that identifies the most active cookies by analyzing log files. The application processes CSV-formatted cookie logs and determines which cookies were most frequently used on a specific date.

## Features

- Reads and processes CSV-formatted cookie log files
- Analyzes cookie activity based on UTC dates
- Identifies the most active cookies for a given date
- Handles timezone conversions automatically
- Provides a user-friendly command-line interface
- Includes comprehensive unit tests

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher


## Usage

Run the application using the following command:

```bash
java -jar target/mostactivecookiefinder-1.0.jar -f <log_file> -d <date>
```

### Parameters

- `-f, --file`: Path to the cookie log file (required)
- `-d, --date`: Target date in UTC format (yyyy-MM-dd) (required)

### Example

```bash
java -jar target/mostactivecookiefinder-1.0.jar -f cookie_log.csv -d 2018-12-09
```

### Input File Format

The input file should be a CSV file with the following format:
```
cookie,timestamp
cookie1,2018-12-09T14:19:00+00:00
cookie2,2018-12-09T14:20:00+00:00
```

## Project Structure

```
src/
├── main/java/com/cookiefinder/
│   ├── App.java                 # Main application class
│   ├── model/
│   │   └── Cookie.java         # Cookie model class
│   └── service/
│       ├── CookieAnalyticsService.java  # Cookie analysis logic
│       └── CookieLogReader.java         # Log file reading logic
└── test/java/com/cookiefinder/
    ├── AppTest.java
    ├── model/
    │   └── CookieTest.java
    └── service/
        ├── CookieAnalyticsServiceTest.java
        └── CookieLogReaderTest.java
```


## Acknowledgments

- [Picocli](https://picocli.info/) - Command-line interface framework
- [JUnit 5](https://junit.org/junit5/) - Testing framework
