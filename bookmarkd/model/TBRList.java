package bookmarkd.model;

import java.util.ArrayList;
import java.util.List;

public class TBRList {
    private List<Book> books;

    public TBRList() {
        this.books = new ArrayList<>();
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

    public boolean isEmpty() {
        return books.isEmpty();
    }
}