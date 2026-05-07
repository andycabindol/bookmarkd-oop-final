package bookmarkd.model;

import java.time.LocalDateTime;

public abstract class Content {
    protected String id;
    protected User author;
    protected LocalDateTime createdAt;

    public Content(String id, User author, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.createdAt = createdAt;
    }

    public abstract void edit(String newText);

    public abstract void delete();

    public String getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
