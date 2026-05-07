package bookmarkd.model;

import java.time.LocalDate;

public class LogEntry {
    private String entryId;
    private User user;
    private Book book;
    private int rating;
    private String reviewText;
    private LocalDate dateRead;
    private Visibility visibility;
    private boolean spoiler;

    public LogEntry(String entryId, User user, Book book, int rating, String reviewText,
                    LocalDate dateRead, Visibility visibility) {
        this.entryId = entryId;
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.reviewText = reviewText;
        this.dateRead = dateRead;
        this.visibility = visibility;
        this.spoiler = false;
    }

    public void edit(String newText) {
        this.reviewText = newText;
    }

    public void delete() {
        this.reviewText = "";
        this.rating = 0;
    }

    public void markSpoiler() {
        this.spoiler = true;
    }

    public String getEntryId() {
        return entryId;
    }

    public User getUser() {
        return user;
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