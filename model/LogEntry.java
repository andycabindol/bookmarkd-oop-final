package bookmarkd.model;

import java.time.LocalDate;

public class LogEntry extends Content {
    private String entryId;
    private Book book;
    private int rating;
    private String reviewText;
    private LocalDate dateRead;
    private Visibility visibility;
    private boolean spoiler;

    public LogEntry(String entryId, User user, Book book, int rating, String reviewText,
                    LocalDate dateRead, Visibility visibility) {
        super(entryId, user);
        this.entryId = entryId;
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

    public int getRating() {
        return rating;
    }

    public Book getBook() {
        return book;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Visibility getVisibility() {
        return visibility;
    }
}