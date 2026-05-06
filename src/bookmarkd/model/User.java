package bookmarkd.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String username;
    private String email;
    private String passwordHash;

    private List<User> followers;
    private List<User> following;
    private List<LogEntry> logEntries;
    private List<Shelf> shelves;
    private TBRList tbrList;

    public User(String userId, String username, String email, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;

        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.logEntries = new ArrayList<>();
        this.shelves = new ArrayList<>();
        this.tbrList = new TBRList(this);
    }

    public void follow(User user) {
        if (user == this || following.contains(user)) {
            return;
        }

        following.add(user);
        user.followers.add(this);
    }

    public void unfollow(User user) {
        following.remove(user);
        user.followers.remove(this);
    }

    public LogEntry logBook(Book book, int rating, String reviewText) {
        LogEntry entry = new LogEntry(
                "entry-" + (logEntries.size() + 1),
                this,
                book,
                rating,
                reviewText,
                LocalDate.now(),
                Visibility.PUBLIC
        );

        logEntries.add(entry);
        book.addLogEntry(entry);
        return entry;
    }

    public Shelf createShelf(String name, String description) {
        Shelf shelf = new Shelf(
                "shelf-" + (shelves.size() + 1),
                this,
                name,
                description,
                true
        );

        shelves.add(shelf);
        return shelf;
    }

    public List<LogEntry> getFeed() {
        List<LogEntry> feed = new ArrayList<>();

        for (User friend : following) {
            feed.addAll(friend.getLogEntries());
        }

        return feed;
    }

    public TBRList getTBRList() {
        return tbrList;
    }

    public List<Shelf> getShelves() {
        return shelves;
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public String getUsername() {
        return username;
    }
}