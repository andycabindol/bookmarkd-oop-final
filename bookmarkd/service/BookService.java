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
            if (book.getTitle().equalsIgnoreCase(query)
                    || book.getIsbn().equalsIgnoreCase(query)) {
                return book;
            }
        }

        return null;
    }

    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerQuery)
                    || book.getAuthor().toLowerCase().contains(lowerQuery)
                    || book.getIsbn().toLowerCase().contains(lowerQuery)
                    || book.getGenre().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }

        return results;
    }

    public List<Book> getAllBooks() {
        return books;
    }
}