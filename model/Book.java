package bookmarkd.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String coverUrl;
    private String description;
    private List<LogEntry> logEntries;

    public Book(String bookId, String title, String author, String isbn, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.logEntries = new ArrayList<>();
    }

    public void addLogEntry(LogEntry entry) {
        logEntries.add(entry);
    }

    public double getAvgRating() {
        if (logEntries.isEmpty()) {
            return 0.0;
        }

        int total = 0;
        for (LogEntry entry : logEntries) {
            total += entry.getRating();
        }

        return (double) total / logEntries.size();
    }

    public List<LogEntry> getReviews() {
        return logEntries;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}