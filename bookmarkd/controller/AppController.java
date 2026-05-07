package bookmarkd.controller;

import bookmarkd.model.Book;
import bookmarkd.model.LogEntry;
import bookmarkd.model.Shelf;
import bookmarkd.model.User;
import bookmarkd.model.Visibility;
import bookmarkd.service.UserService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AppController {
    private static final String DIVIDER = "==============================================================";
    private UserService userService;
    private Scanner scanner;
    private User currentUser;
    private String lastActionStatus;

    public AppController() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
        this.lastActionStatus = "Ready.";
        seedData();
    }

    private void seedData() {
        userService.addBook(new Book("b1", "The Hobbit", "J.R.R. Tolkien", "9780001", "Fantasy"));
        userService.addBook(new Book("b2", "Dune", "Frank Herbert", "9780002", "Science Fiction"));
        userService.addBook(new Book("b3", "Pride and Prejudice", "Jane Austen", "9780003", "Romance"));
        userService.addBook(new Book("b4", "Atomic Habits", "James Clear", "9780004", "Self Help"));
        userService.addBook(new Book("b5", "Norwegian Wood", "Haruki Murakami", "9780005", "Literary Fiction"));

        currentUser = userService.registerUser("alice", "alice@email.com", "alice123");
        User bob = userService.registerUser("bob", "bob@email.com", "bob123");

        // Demo data so the feed/profile features are easier to test
        Book dune = userService.searchBook("Dune");
        if (dune != null) {
            bob.logBook(dune, 5, "Amazing worldbuilding.", Visibility.PUBLIC);
        }
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    switchUser();
                    break;
                case "3":
                    searchBooks();
                    break;
                case "4":
                    logReadBook();
                    break;
                case "5":
                    addBookToTBR();
                    break;
                case "6":
                    viewTBR();
                    break;
                case "7":
                    removeBookFromTBR();
                    break;
                case "8":
                    prioritizeTBR();
                    break;
                case "9":
                    createShelf();
                    break;
                case "10":
                    addBookToShelf();
                    break;
                case "11":
                    viewShelves();
                    break;
                case "12":
                    followUser();
                    break;
                case "13":
                    viewFeed();
                    break;
                case "14":
                    viewProfile();
                    break;
                case "15":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printWelcome() {
        System.out.println(DIVIDER);
        System.out.println(" ____              _                        _    _ ");
        System.out.println("|  _ \\            | |                      | |  | |");
        System.out.println("| |_) | ___   ___ | | ___ __ ___   __ _ _ __| | _| |");
        System.out.println("|  _ < / _ \\ / _ \\| |/ / '_ ` _ \\ / _` | '__| |/ / |");
        System.out.println("| |_) | (_) | (_) |   <| | | | | | (_| | |  |   <|_|");
        System.out.println("|____/ \\___/ \\___/|_|\\_\\_| |_| |_|\\__,_|_|  |_|\\_(_)");
        System.out.println("Social Book Cataloging CLI");
        System.out.println(DIVIDER);
    }

    private void printMenu() {
        System.out.println("\n=== Bookmarkd ===");
        System.out.println("Current user: " + currentUser.getUsername());
        System.out.println("1. Register user");
        System.out.println("2. Switch user");
        System.out.println("3. Search books");
        System.out.println("4. Log a read book");
        System.out.println("5. Add book to TBR");
        System.out.println("6. View my TBR");
        System.out.println("7. Remove book from TBR");
        System.out.println("8. Prioritize TBR book");
        System.out.println("9. Create shelf");
        System.out.println("10. Add book to shelf");
        System.out.println("11. View my shelves");
        System.out.println("12. Follow user");
        System.out.println("13. View activity feed");
        System.out.println("14. View user profile");
        System.out.println("15. Exit");
        System.out.print("Choose an option: ");
    }

    private void registerUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        String email = readValidEmail();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            User user = userService.registerUser(username, email, password);
            currentUser = user;
            System.out.println("Registered and switched to user: " + user.getUsername());
            lastActionStatus = "Registered user '" + user.getUsername() + "'.";
        } catch (IllegalArgumentException e) {
            System.out.println("Could not register user: " + e.getMessage());
            lastActionStatus = "Registration failed: " + e.getMessage();
        }
    }

    private String readValidEmail() {
        while (true) {
            System.out.print("Email: ");
            String email = scanner.nextLine();

            if (!userService.isEmailFormatValid(email)) {
                System.out.println("Invalid email format. Please try again.");
                continue;
            }

            if (userService.isEmailTaken(email)) {
                System.out.println("Email is already in use. Please try another.");
                continue;
            }

            return email;
        }
    }

    private void switchUser() {
        System.out.print("Username to switch to: ");
        String username = scanner.nextLine();

        User user = userService.searchUser(username);

        if (user == null) {
            System.out.println("User not found.");
            lastActionStatus = "Switch failed: user not found.";
            return;
        }

        currentUser = user;
        System.out.println("Switched to user: " + currentUser.getUsername());
        lastActionStatus = "Switched to '" + currentUser.getUsername() + "'.";
    }

    private void searchBooks() {
        System.out.print("Search by title, author, ISBN, or genre: ");
        String query = scanner.nextLine();

        List<Book> results = userService.searchBooks(query);

        if (results.isEmpty()) {
            System.out.println("No books found.");
            lastActionStatus = "Search returned no books for '" + query + "'.";
            return;
        }

        System.out.println("\nSearch results:");
        for (Book book : results) {
            System.out.println("- " + book);
        }
        lastActionStatus = "Found " + results.size() + " book(s) for '" + query + "'.";
    }

    private void logReadBook() {
        System.out.print("Book title or ISBN: ");
        String query = scanner.nextLine();

        Book book = chooseBookForLogging(query);

        if (book == null) {
            System.out.println("Book not found.");
            lastActionStatus = "Log canceled or no valid book selected.";
            return;
        }

        int rating = readRating();

        System.out.print("Review, optional: ");
        String reviewText = scanner.nextLine();

        Visibility visibility = readVisibility();

        LogEntry entry = currentUser.logBook(book, rating, reviewText, visibility);

        System.out.println("Logged book: " + entry.getBook().getTitle());
        lastActionStatus = "Logged '" + entry.getBook().getTitle() + "' with rating " + entry.getRating() + "/5.";
    }

    private Book chooseBookForLogging(String query) {
        Book exactMatch = userService.searchBook(query);
        if (exactMatch != null) {
            return exactMatch;
        }

        List<Book> candidates = userService.searchBooks(query);
        if (candidates.isEmpty()) {
            candidates = getFuzzyBookCandidates(query);
        }
        if (candidates.isEmpty()) {
            return null;
        }

        System.out.println("\nNo exact match found. Did you mean:");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println((i + 1) + ". " + candidates.get(i));
        }
        System.out.println("0. Cancel");

        while (true) {
            System.out.print("Choose a book number: ");
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                if (choice == 0) {
                    return null;
                }
                if (choice >= 1 && choice <= candidates.size()) {
                    return candidates.get(choice - 1);
                }
                System.out.println("Please choose a valid number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private List<Book> getFuzzyBookCandidates(String query) {
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase();
        if (normalizedQuery.isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> allBooks = userService.getAllBooks();
        List<BookMatch> matches = new ArrayList<>();

        for (Book book : allBooks) {
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            String isbn = book.getIsbn().toLowerCase();

            int titleScore = fuzzyScore(normalizedQuery, title);
            int authorScore = fuzzyScore(normalizedQuery, author);
            int isbnScore = fuzzyScore(normalizedQuery, isbn);
            int bestScore = Math.max(titleScore, Math.max(authorScore, isbnScore));

            if (bestScore > 0) {
                matches.add(new BookMatch(book, bestScore));
            }
        }

        matches.sort(Comparator.comparingInt(BookMatch::score).reversed());
        List<Book> result = new ArrayList<>();
        int limit = Math.min(5, matches.size());
        for (int i = 0; i < limit; i++) {
            result.add(matches.get(i).book());
        }
        return result;
    }

    private int fuzzyScore(String query, String target) {
        if (target.contains(query)) {
            return 100;
        }

        int subsequenceLength = subsequenceLength(query, target);
        if (subsequenceLength == 0) {
            return 0;
        }

        // Higher score for better character-order overlap.
        return (subsequenceLength * 100) / query.length();
    }

    private int subsequenceLength(String query, String target) {
        int i = 0;
        int j = 0;

        while (i < query.length() && j < target.length()) {
            if (query.charAt(i) == target.charAt(j)) {
                i++;
            }
            j++;
        }

        return i;
    }

    private record BookMatch(Book book, int score) {}

    private int readRating() {
        while (true) {
            System.out.print("Rating 1-5: ");
            String input = scanner.nextLine();

            try {
                int rating = Integer.parseInt(input);

                if (rating >= 1 && rating <= 5) {
                    return rating;
                }

                System.out.println("Rating must be between 1 and 5.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private Visibility readVisibility() {
        while (true) {
            System.out.println("Visibility:");
            System.out.println("1. Public");
            System.out.println("2. Friends only");
            System.out.println("3. Private");
            System.out.print("Choose visibility: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    return Visibility.PUBLIC;
                case "2":
                    return Visibility.FRIENDS_ONLY;
                case "3":
                    return Visibility.PRIVATE;
                default:
                    System.out.println("Invalid visibility.");
            }
        }
    }

    private void addBookToTBR() {
        System.out.print("Book title or ISBN: ");
        String query = scanner.nextLine();

        Book book = userService.searchBook(query);

        if (book == null) {
            System.out.println("Book not found.");
            lastActionStatus = "Add to TBR failed: book not found.";
            return;
        }

        currentUser.getTBRList().addBook(book);
        System.out.println("Added to TBR: " + book.getTitle());
        lastActionStatus = "Added '" + book.getTitle() + "' to TBR.";
    }

    private void viewTBR() {
        List<Book> books = currentUser.getTBRList().getBooks();

        System.out.println("\n=== " + currentUser.getUsername() + "'s TBR List ===");

        if (books.isEmpty()) {
            System.out.println("TBR list is empty.");
            return;
        }

        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    private void removeBookFromTBR() {
        System.out.print("Book title or ISBN to remove: ");
        String query = scanner.nextLine();

        Book book = userService.searchBook(query);

        if (book == null) {
            System.out.println("Book not found.");
            lastActionStatus = "Remove from TBR failed: book not found.";
            return;
        }

        currentUser.getTBRList().removeBook(book);
        System.out.println("Removed from TBR: " + book.getTitle());
        lastActionStatus = "Removed '" + book.getTitle() + "' from TBR.";
    }

    private void prioritizeTBR() {
        System.out.print("Book title or ISBN to prioritize: ");
        String query = scanner.nextLine();

        Book book = userService.searchBook(query);

        if (book == null) {
            System.out.println("Book not found.");
            lastActionStatus = "Prioritize failed: book not found.";
            return;
        }

        System.out.print("New position, starting from 1: ");
        String input = scanner.nextLine();

        try {
            int position = Integer.parseInt(input) - 1;
            currentUser.getTBRList().prioritizeBook(book, position);
            System.out.println("Updated TBR priority.");
            lastActionStatus = "Updated TBR priority for '" + book.getTitle() + "'.";
        } catch (NumberFormatException e) {
            System.out.println("Invalid position.");
            lastActionStatus = "Prioritize failed: invalid position.";
        }
    }

    private void createShelf() {
        System.out.print("Shelf name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String description = scanner.nextLine();

        System.out.print("Public shelf? yes/no: ");
        String answer = scanner.nextLine();

        boolean isPublic = answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("y");

        Shelf shelf = currentUser.createShelf(name, description, isPublic);

        System.out.println("Created shelf: " + shelf.getName());
        lastActionStatus = "Created shelf '" + shelf.getName() + "'.";
    }

    private void addBookToShelf() {
        System.out.print("Shelf name: ");
        String shelfName = scanner.nextLine();

        Shelf shelf = currentUser.findShelfByName(shelfName);

        if (shelf == null) {
            System.out.println("Shelf not found.");
            lastActionStatus = "Add to shelf failed: shelf not found.";
            return;
        }

        System.out.print("Book title or ISBN: ");
        String query = scanner.nextLine();

        Book book = userService.searchBook(query);

        if (book == null) {
            System.out.println("Book not found.");
            lastActionStatus = "Add to shelf failed: book not found.";
            return;
        }

        shelf.addBook(book);
        System.out.println("Added " + book.getTitle() + " to shelf " + shelf.getName());
        lastActionStatus = "Added '" + book.getTitle() + "' to shelf '" + shelf.getName() + "'.";
    }

    private void viewShelves() {
        List<Shelf> shelves = currentUser.getShelves();

        System.out.println("\n=== " + currentUser.getUsername() + "'s Shelves ===");

        if (shelves.isEmpty()) {
            System.out.println("No shelves yet.");
            lastActionStatus = "No shelves to display.";
            return;
        }

        for (Shelf shelf : shelves) {
            System.out.println("\nShelf: " + shelf.getName());
            System.out.println("Description: " + shelf.getDescription());
            System.out.println("Public: " + shelf.isPublic());

            List<Book> books = shelf.getBooks();

            if (books.isEmpty()) {
                System.out.println("No books in this shelf.");
            } else {
                System.out.println("Books:");
                for (Book book : books) {
                    System.out.println("- " + book);
                }
            }
        }
        lastActionStatus = "Displayed " + shelves.size() + " shelf/s.";
    }

    private void followUser() {
        System.out.print("Username to follow: ");
        String username = scanner.nextLine();

        User targetUser = userService.searchUser(username);

        if (targetUser == null) {
            System.out.println("User not found.");
            lastActionStatus = "Follow failed: user not found.";
            return;
        }

        userService.followUser(currentUser, targetUser);
        System.out.println(currentUser.getUsername() + " followed " + targetUser.getUsername());
        lastActionStatus = "Now following '" + targetUser.getUsername() + "'.";
    }

    private void viewFeed() {
        List<LogEntry> feed = currentUser.getFeed();

        System.out.println("\n=== Activity Feed for " + currentUser.getUsername() + " ===");

        if (feed.isEmpty()) {
            System.out.println("No activity yet. Try following another user first.");
            lastActionStatus = "Feed is empty.";
            return;
        }

        boolean hasVisibleEntry = false;

        for (LogEntry entry : feed) {
            if (!currentUser.canViewLogEntry(entry)) {
                continue;
            }

            hasVisibleEntry = true;

            System.out.println(entry.getUser().getUsername()
                    + " read "
                    + entry.getBook().getTitle()
                    + " and rated it "
                    + entry.getRating()
                    + "/5");

            if (!entry.getReviewText().isEmpty()) {
                System.out.println("Review: " + entry.getReviewText());
            }

            System.out.println("Visibility: " + entry.getVisibility());
            System.out.println();
        }

        if (!hasVisibleEntry) {
            System.out.println("No visible activity.");
            lastActionStatus = "Feed has no visible entries.";
            return;
        }
        lastActionStatus = "Displayed activity feed (" + feed.size() + " total item candidates).";
    }

    private void viewProfile() {
        System.out.print("Username to view, or press Enter for your own profile: ");
        String username = scanner.nextLine();

        User user;

        if (username.isEmpty()) {
            user = currentUser;
        } else {
            user = userService.searchUser(username);
        }

        if (user == null) {
            System.out.println("User not found.");
            lastActionStatus = "Profile lookup failed: user not found.";
            return;
        }

        System.out.println("\n=== Profile: " + user.getUsername() + " ===");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Followers: " + user.getFollowers().size());
        System.out.println("Following: " + user.getFollowing().size());

        System.out.println("\nRead Books:");
        if (user.getLogEntries().isEmpty()) {
            System.out.println("No logged books yet.");
        } else {
            for (LogEntry entry : user.getLogEntries()) {
                if (currentUser.canViewLogEntry(entry)) {
                    System.out.println("- " + entry.getBook().getTitle()
                            + " | Rating: " + entry.getRating()
                            + "/5 | Review: " + entry.getReviewText());
                }
            }
        }

        System.out.println("\nShelves:");
        if (user.getShelves().isEmpty()) {
            System.out.println("No shelves yet.");
        } else {
            for (Shelf shelf : user.getShelves()) {
                if (shelf.isPublic() || user == currentUser) {
                    System.out.println("- " + shelf.getName()
                            + " (" + shelf.getBooks().size() + " books)");
                }
            }
        }
        lastActionStatus = "Viewed profile for '" + user.getUsername() + "'.";
    }
}