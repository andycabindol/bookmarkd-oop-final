package bookmarkd.model;

import java.util.LinkedList;

public class TBRList {
    private User owner;
    private LinkedList<Book> books;

    public TBRList(User owner) {
        this.owner = owner;
        this.books = new LinkedList<>();
    }

    public void add(Book book) {
        books.add(book);
    }

    public void remove(Book book) {
        books.remove(book);
    }

    public void prioritize(Book book, int position) {
        if (!books.contains(book) || position < 0 || position >= books.size()) {
            return;
        }

        books.remove(book);
        books.add(position, book);
    }

    public LinkedList<Book> getBooks() {
        return books;
    }
}