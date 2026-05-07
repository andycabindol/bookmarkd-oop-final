package bookmarkd.model;

import java.util.LinkedList;
import java.util.List;

public class TBRList {
    private User owner;
    private List<Book> books;

    public TBRList(User owner) {
        this.owner = owner;
        this.books = new LinkedList<>();
    }

    public void addBook(Book book) {
        if (book != null && !books.contains(book)) {
            books.add(book);
        }
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void prioritizeBook(Book book, int position) {
        if (book == null || !books.contains(book)) {
            return;
        }

        if (position < 0 || position >= books.size()) {
            return;
        }

        books.remove(book);
        books.add(position, book);
    }

    public List<Book> getBooks() {
        return books;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isEmpty() {
        return books.isEmpty();
    }
}