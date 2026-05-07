package bookmarkd.service;

import bookmarkd.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
    }

    public User registerUser(String username, String email) {
        User user = new User(
            "user-" + (users.size() + 1),
            username,
            email,
            "");
        users.add(user);
        return user;
    }

    public User searchUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }

        return null;
    }

    public void followUser(User follower, User targetUser) {
        follower.follow(targetUser);
    }

    public void unfollowUser(User follower, User targetUser) {
        follower.unfollow(targetUser);
    }

    public List<User> getAllUsers() {
        return users;
    }
}