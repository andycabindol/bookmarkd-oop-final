package bookmarkd.service;

import bookmarkd.model.Book;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookService {
    private List<Book> books;
    private static final Pattern TITLE_PATTERN = Pattern.compile("\"title\"\\s*:\\s*\"([^\"]+)\"");

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

        List<Book> apiResults = searchBooksFromApi(query, 1);
        if (!apiResults.isEmpty()) {
            Book apiBook = apiResults.get(0);
            addBookIfMissing(apiBook);
            return apiBook;
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

        if (results.isEmpty()) {
            List<Book> apiResults = searchBooksFromApi(query, 5);
            for (Book book : apiResults) {
                addBookIfMissing(book);
                results.add(book);
            }
        }

        return results;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    private void addBookIfMissing(Book newBook) {
        if (newBook == null) {
            return;
        }

        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(newBook.getIsbn())
                    || book.getTitle().equalsIgnoreCase(newBook.getTitle())) {
                return;
            }
        }

        books.add(newBook);
    }

    private List<Book> searchBooksFromApi(String query, int limit) {
        List<Book> results = new ArrayList<>();

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
            String url = "https://openlibrary.org/search.json?q=" + encodedQuery + "&limit=" + limit;

            HttpURLConnection connection = (HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            if (connection.getResponseCode() != 200) {
                return results;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            results.addAll(parseBooksFromJson(json.toString(), limit));
        } catch (Exception ignored) {
            // If API is unavailable, the app still works with local seeded data.
        }

        return results;
    }

    private List<Book> parseBooksFromJson(String json, int limit) {
        List<Book> parsed = new ArrayList<>();
        Matcher matcher = TITLE_PATTERN.matcher(json);
        int index = 0;

        while (matcher.find() && parsed.size() < limit) {
            String title = matcher.group(1);
            int start = matcher.start();
            int end = Math.min(json.length(), start + 500);
            String nearbyChunk = json.substring(start, end);

            String author = extractArrayFirst(nearbyChunk, "\"author_name\"");
            String isbn = extractArrayFirst(nearbyChunk, "\"isbn\"");
            String genre = extractArrayFirst(nearbyChunk, "\"subject\"");

            if (author.isEmpty()) {
                author = "Unknown Author";
            }
            if (isbn.isEmpty()) {
                isbn = "API-" + Math.abs((title + index).hashCode());
            }
            if (genre.isEmpty()) {
                genre = "Unknown";
            }

            parsed.add(new Book("api-" + (index + 1), unescape(title), unescape(author), unescape(isbn), unescape(genre)));
            index++;
        }

        return parsed;
    }

    private String extractArrayFirst(String json, String fieldName) {
        int fieldIndex = json.indexOf(fieldName);
        if (fieldIndex < 0) {
            return "";
        }

        int bracketStart = json.indexOf("[", fieldIndex);
        int bracketEnd = json.indexOf("]", bracketStart);
        if (bracketStart < 0 || bracketEnd < 0) {
            return "";
        }

        String content = json.substring(bracketStart + 1, bracketEnd).trim();
        if (content.isEmpty()) {
            return "";
        }

        if (content.startsWith("\"")) {
            int secondQuote = content.indexOf("\"", 1);
            if (secondQuote > 1) {
                return content.substring(1, secondQuote);
            }
        }

        int comma = content.indexOf(",");
        if (comma > 0) {
            return content.substring(0, comma).replace("\"", "").trim();
        }
        return content.replace("\"", "").trim();
    }

    private String unescape(String value) {
        return value
                .replace("\\\"", "\"")
                .replace("\\/", "/");
    }
}