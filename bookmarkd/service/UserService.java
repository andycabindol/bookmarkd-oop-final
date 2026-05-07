package bookmarkd.service;

import bookmarkd.model.Book;
import bookmarkd.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private List<User> users;
    private BookService bookService;

    public UserService() {
        this.users = new ArrayList<>();
        this.bookService = new BookService();
    }

    public User registerUser(String username, String email) {
        return registerUser(username, email, "default-password");
    }

    public User registerUser(String username, String email, String password) {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedEmail = email == null ? "" : email.trim();
        String normalizedPassword = password == null ? "" : password;

        if (normalizedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (!isEmailFormatValid(normalizedEmail)) {
            throw new IllegalArgumentException("Email format is invalid.");
        }

        if (normalizedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (searchUser(normalizedUsername) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (isEmailTaken(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        User user = new User(
            "user-" + (users.size() + 1),
            normalizedUsername,
            normalizedEmail,
            hashPassword(normalizedPassword));
        users.add(user);
        return user;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String value = Integer.toHexString(0xff & b);
                if (value.length() == 1) {
                    hex.append('0');
                }
                hex.append(value);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available.", e);
        }
    }

    public User searchUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }

        return null;
    }

    public boolean isEmailFormatValid(String email) {
        String normalizedEmail = email == null ? "" : email.trim();
        return EMAIL_PATTERN.matcher(normalizedEmail).matches();
    }

    public boolean isEmailTaken(String email) {
        String normalizedEmail = email == null ? "" : email.trim();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(normalizedEmail)) {
                return true;
            }
        }
        return false;
    }

    public void followUser(User follower, User targetUser) {
        follower.follow(targetUser);
    }

    public void unfollowUser(User follower, User targetUser) {
        follower.unfollow(targetUser);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void addBook(Book book) {
        bookService.addBook(book);
    }

    public Book searchBook(String query) {
        return bookService.searchBook(query);
    }

    public List<Book> searchBooks(String query) {
        return bookService.searchBooks(query);
    }

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
}