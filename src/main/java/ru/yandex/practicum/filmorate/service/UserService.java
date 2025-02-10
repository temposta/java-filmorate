package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.friend.FriendRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserRepository repository;
    private final FriendRepository friendRepository;

    public UserService(UserRepository repository, FriendRepository friendRepository) {
        this.repository = repository;
        this.friendRepository = friendRepository;
        log.info("UserService created");
    }

    public User createUser(User user) {
        log.info("Creating user: {} - Starting", user);
        repository.add(user);
        log.info("User created: {} - Finishing", user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Updating user: {} - Starting", user);
//        checkUserId(user.getId());
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
        if (foundUser == null) {
            throw new DataIntegrityViolationException("User not found");
        }
        log.info("User found: {} - Finishing", id);
        return foundUser;
    }

    public void addFriend(long id, long friendId) {
        log.info("Adding friend to userId: {} - Starting", id);
        if (id == friendId) {
            log.info("FriendID == UserID : {} - Finishing without adding", id);
            throw new DataIntegrityViolationException("FriendID == UserID");
        }
        friendRepository.addFriend(id, friendId);
        log.info("Friend added to userId: {} - Finishing", id);
    }

    public void deleteFriend(long id, long friendId) {
        log.info("Deleting friend: {} - Starting", friendId);
//        checkUserId(id);
        if (id == friendId) {
            log.info("FriendID == UserID : {} - Finishing without deleting", id);
            throw new DataIntegrityViolationException("FriendID == UserID");
        }
//        checkUserId(friendId);
        friendRepository.removeFriend(id, friendId);
        log.info("Friend deleted: {} - Finishing", friendId);
    }

    public Set<Long> getFriends(long id) {
        log.info("Getting friends: {} - Starting", id);
//        checkUserId(id);
        Set<Long> friends = friendRepository.getFriendIds(id);
        log.info("Friends found: {} - Finishing", id);
        return friends;
    }

    public Set<Long> getFriendsCommon(long id, long otherId) {
        log.info("Getting common friends: {} & {} - Starting", id, otherId);
//        checkUserId(id);
//        checkUserId(otherId);
        Set<Long> commonFriends = friendRepository.getCommonFriendIds(id, otherId);
        log.info("Common friends found: {} & {} - Finishing", id, otherId);
        return commonFriends;
    }

//    private void checkUserId(long id) {
//        User user = repository.findById(id);
//        if (user == null) {
//            throw new DataIntegrityViolationException("User with id " + id + " not found");
//        }
//    }
}
