package bookmarkd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Review extends Content {
    private String reviewId;
    private LogEntry logEntry;
    private String body;
    private List<User> likes;
    private List<String> comments;

    public Review(String reviewId, User author, LogEntry logEntry, String body) {
        super(reviewId, author, LocalDateTime.now());
        this.reviewId = reviewId;
        this.logEntry = logEntry;
        this.body = body;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    @Override
    public void edit(String newText) {
        this.body = newText;
    }

    @Override
    public void delete() {
        this.body = "";
        this.comments.clear();
        this.likes.clear();
    }

    public void like(User user) {
        if (user != null && !likes.contains(user)) {
            likes.add(user);
        }
    }

    public void addComment(User user, String text) {
        if (user != null && text != null && !text.trim().isEmpty()) {
            comments.add(user.getUsername() + ": " + text.trim());
        }
    }

    public String getReviewId() {
        return reviewId;
    }

    public LogEntry getLogEntry() {
        return logEntry;
    }

    public String getBody() {
        return body;
    }

    public List<User> getLikes() {
        return likes;
    }

    public List<String> getComments() {
        return comments;
    }
}
