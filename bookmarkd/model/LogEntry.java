package bookmarkd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogEntry extends Content {
    private Book book;
    private int rating;
    private String reviewText;
    private LocalDate dateRead;
    private Visibility visibility;
    private boolean spoiler;

    public LogEntry(String entryId, User user, Book book, int rating, String reviewText,
                    LocalDate dateRead, Visibility visibility) {
        super(entryId, user, LocalDateTime.now());
        this.book = book;
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateRead = dateRead;
        this.visibility = visibility;
        this.spoiler = false;
    }

    @Override
    public void edit(String newText) {
        this.reviewText = newText;
    }

    @Override
    public void delete() {
        this.reviewText = "";
        this.rating = 0;
    }

    public void markSpoiler() {
        this.spoiler = true;
    }

    public String getEntryId() {
        return id;
    }

    public User getUser() {
        return author;
    }

    public Book getBook() {
        return book;
    }

    public int getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public LocalDate getDateRead() {
        return dateRead;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isSpoiler() {
        return spoiler;
    }
}