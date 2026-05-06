package bookmarkd.service;

import bookmarkd.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Book searchBook(String query) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(query)) {
                return book;
            }
        }

        return null;
    }

    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())
                    || book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            }
        }

        return results;
    }

    public List<Book> getAllBooks() {
        return books;
    }
}