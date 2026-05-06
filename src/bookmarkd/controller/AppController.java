package bookmarkd.controller;

import bookmarkd.model.Book;
import bookmarkd.model.User;
import bookmarkd.service.BookService;
import bookmarkd.service.FeedService;
import bookmarkd.service.UserService;

import java.util.Scanner;

public class AppController {
    private BookService bookService;
    private UserService userService;
    private FeedService feedService;
    private Scanner scanner;
    private User currentUser;

    public AppController() {
        this.bookService = new BookService();
        this.userService = new UserService();
        this.feedService = new FeedService();
        this.scanner = new Scanner(System.in);
        seedData();
    }

    private void seedData() {
        bookService.addBook(new Book("b1", "The Hobbit", "J.R.R. Tolkien", "9780001", "Fantasy"));
        bookService.addBook(new Book("b2", "Dune", "Frank Herbert", "9780002", "Science Fiction"));
        bookService.addBook(new Book("b3", "Pride and Prejudice", "Jane Austen", "9780003", "Romance"));

        currentUser = userService.registerUser("alice", "alice@email.com", "hash123");
        userService.registerUser("bob", "bob@email.com", "hash456");
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
                    searchBooks();
                    break;
                case "3":
                    logBook();
                    break;
                case "4":
                    addBookToTBR();
                    break;
                case "5":
                    followUser();
                    break;
                case "6":
                    feedService.printFeed(currentUser);
                    break;
                case "7":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Bookmarkd ===");
        System.out.println("Current user: " + currentUser.getUsername());
        System.out.println("1. Register user");
        System.out.println("2. Search books");
        System.out.println("3. Log a book");
        System.out.println("4. Add book to TBR");
        System.out.println("5. Follow user");
        System.out.println("6. View feed");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    private void registerUser() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        User user = userService.registerUser(username, email, "defaultHash");
        currentUser = user;

        System.out.println("Registered and switched to user: " + user.getUsername());
    }

    private void searchBooks() {
        System.out.print("Search query: ");
        String query = scanner.nextLine();

        for (Book book : bookService.searchBooks(query)) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }

    private void logBook() {
        System.out.print("Book title: ");
        String title = scanner.nextLine();

        Book book = bookService.searchBook(title);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.print("Rating 1-5: ");
        int rating = Integer.parseInt(scanner.nextLine());

        System.out.print("Review: ");
        String review = scanner.nextLine();

        currentUser.logBook(book, rating, review);
        System.out.println("Logged book: " + book.getTitle());
    }

    private void addBookToTBR() {
        System.out.print("Book title: ");
        String title = scanner.nextLine();

        Book book = bookService.searchBook(title);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        currentUser.getTBRList().add(book);
        System.out.println("Added to TBR: " + book.getTitle());
    }

    private void followUser() {
        System.out.print("Username to follow: ");
        String username = scanner.nextLine();

        User targetUser = userService.searchUser(username);

        if (targetUser == null) {
            System.out.println("User not found.");
            return;
        }

        userService.followUser(currentUser, targetUser);
        System.out.println(currentUser.getUsername() + " followed " + targetUser.getUsername());
    }
}