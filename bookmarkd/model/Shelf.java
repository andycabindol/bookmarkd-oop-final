package bookmarkd.model;

import java.util.ArrayList;
import java.util.List;

public class Shelf {
    private String shelfId;
    private User owner;
    private String name;
    private String description;
    private List<Book> books;
    private boolean isPublic;

    public Shelf(String shelfId, User owner, String name, String description, boolean isPublic) {
        this.shelfId = shelfId;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void reorder(Book book, int newPosition) {
        if (!books.contains(book) || newPosition < 0 || newPosition >= books.size()) {
            return;
        }

        books.remove(book);
        books.add(newPosition, book);
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }
}