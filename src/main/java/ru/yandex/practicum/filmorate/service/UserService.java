package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        log.info("Creating user: {} - Starting", user);
        repository.add(user);
        log.info("User created: {} - Finishing", user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Updating user: {} - Starting", user);
        User updatedUser = repository.update(user);
        log.info("User updated: {} - Finishing", user);
        return updatedUser;
    }

    public List<User> getAllUsers() {
        log.info("Getting all users");
        return repository.getAll();
    }

    public User deleteUser(User user) {
        log.info("Deleting user: {} - Starting", user);
        User deletedUser = repository.delete(user);
        log.info("User deleted: {} - Finishing", user);
        return deletedUser;
    }

    public User getUserById(long id) {
        log.info("Getting user by id: {} - Starting", id);
        User foundUser = repository.findById(id);
        log.info("User found: {} - Finishing", id);
        return foundUser;
    }

    public User addFriend(long id, long friendId) {
        log.info("Adding friend: {} - Starting", friendId);
        User foundUser = repository.findById(id);
        User friend = repository.findById(friendId);
        if (id == friendId) {
            log.info("FriendID == UserID : {} - Finishing without adding", id);
            return foundUser;
        }
        foundUser.getFriends().add(friendId);
        friend.getFriends().add(id);
        repository.update(foundUser);
        repository.update(friend);
        log.info("Friend added: {} - Finishing", friendId);
        return foundUser;
    }

    public User deleteFriend(long id, long friendId) {
        log.info("Deleting friend: {} - Starting", friendId);
        User foundUser = repository.findById(id);
        User friend = repository.findById(friendId);
        if (id == friendId) {
            log.info("FriendID == UserID : {} - Finishing without deleting", id);
            return foundUser;
        }
        foundUser.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        repository.update(foundUser);
        repository.update(friend);
        log.info("Friend deleted: {} - Finishing", friendId);
        return foundUser;
    }

    public List<User> getFriends(long id) {
        log.info("Getting friends: {} - Starting", id);
        List<User> friends = new ArrayList<>();
        repository.findById(id)
                .getFriends()
                .forEach(friendId -> friends.add(repository.findById(friendId)));
        log.info("Friends found: {} - Finishing", id);
        return friends;
    }

    public List<User> getFriendsCommon(long id, long otherId) {
        log.info("Getting common friends: {} & {} - Starting", id, otherId);
        List<User> commonFriends = new ArrayList<>();
        Set<Long> firstFriends = repository.findById(id).getFriends();
        Set<Long> secondFriends = repository.findById(otherId).getFriends();
        firstFriends.forEach(friendId -> {
            if (secondFriends.contains(friendId)) {
                commonFriends.add(repository.findById(friendId));
            }
        });
        log.info("Common friends found: {} & {} - Finishing", id, otherId);
        return commonFriends;
    }
}
