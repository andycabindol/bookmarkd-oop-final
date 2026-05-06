package bookmarkd.service;

import bookmarkd.model.LogEntry;
import bookmarkd.model.User;
import bookmarkd.model.Visibility;
import java.util.List;

public class FeedService {
    public List<LogEntry> getFeedForUser(User user) {
        return user.getFeed();
    }

    public void printFeed(User user) {
        List<LogEntry> feed = getFeedForUser(user);

        System.out.println("\n=== Feed for " + user.getUsername() + " ===");

        if (feed.isEmpty()) {
            System.out.println("No activity yet.");
            return;
        }

        for (LogEntry entry : feed) {
            if (entry.getVisibility() == Visibility.PRIVATE) {
                continue;
            }

            System.out.println(
                    entry.getAuthor().getUsername()
                            + " read "
                            + entry.getBook().getTitle()
                            + " and rated it "
                            + entry.getRating()
                            + "/5"
            );

            if (!entry.getReviewText().isEmpty()) {
                System.out.println("Review: " + entry.getReviewText());
            }

            System.out.println("Visibility: " + entry.getVisibility());
            System.out.println();
        }
    }
}