# Bookmarkd OOP Final Project

Bookmarkd is a Java console application for social book cataloging. Users can register, search books, log reads with visibility settings, manage a TBR list, create shelves, follow other users, and view activity feeds.

## Project Structure

- `bookmarkd/Main.java` - entry point
- `bookmarkd/controller/` - console controller and menu flow
- `bookmarkd/service/` - user and book services
- `bookmarkd/model/` - domain models (`User`, `Book`, `LogEntry`, `Shelf`, `TBRList`, etc.)

## Features Implemented

- User registration and switching
- Book search by title, author, ISBN, or genre
- Log read books with rating, review text, and visibility
- TBR list management (add, remove, prioritize)
- Shelf creation and shelf book management
- Follow users and view activity feed
- Profile view with visibility-aware content filtering

## Recent Implementation Updates

To better align with the proposal/rubric, the following were added or updated:

- Added abstract `Content` class
- Added `Review` class
- Updated `LogEntry` to extend `Content`
- Updated `TBRList` to include owner and linked-list style storage
- Enforced visibility rules (`PUBLIC`, `FRIENDS_ONLY`, `PRIVATE`) in feed/profile checks
- Sorted feed entries reverse-chronologically
- Improved registration validation:
  - required non-empty username
  - email format validation
  - duplicate username/email checks
  - safe user-facing error handling in registration flow

## Requirements

- Java JDK installed (recommended: 17+)
- `javac` and `java` available in your `PATH`

## Compile and Run

From project root:

```bash
javac bookmarkd/Main.java
java bookmarkd.Main
```

## Notes for Demo

Suggested demo flow:

1. Register a user
2. Search for a book
3. Log a read book with different visibility levels
4. Add and prioritize books in TBR
5. Follow another user
6. View feed/profile to verify visibility behavior

