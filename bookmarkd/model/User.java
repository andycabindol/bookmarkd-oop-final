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
        if (user == null || user == this || following.contains(user)) {
            return;
        }

        following.add(user);
        user.followers.add(this);
    }

    public void unfollow(User user) {
        if (user == null) {
            return;
        }

        following.remove(user);
        user.followers.remove(this);
    }

    public LogEntry logBook(Book book, int rating, String reviewText, Visibility visibility) {
        LogEntry entry = new LogEntry(
                "entry-" + (logEntries.size() + 1),
                this,
                book,
                rating,
                reviewText,
                LocalDate.now(),
                visibility
        );

        logEntries.add(entry);
        book.addLogEntry(entry);
        return entry;
    }

    public Shelf createShelf(String name, String description, boolean isPublic) {
        Shelf shelf = new Shelf(
                "shelf-" + (shelves.size() + 1),
                this,
                name,
                description,
                isPublic
        );

        shelves.add(shelf);
        return shelf;
    }

    public Shelf findShelfByName(String name) {
        for (Shelf shelf : shelves) {
            if (shelf.getName().equalsIgnoreCase(name)) {
                return shelf;
            }
        }

        return null;
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

    public List<User> getFollowers() {
        return followers;
    }

    public List<User> getFollowing() {
        return following;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}