package ru.yandex.practicum.filmorate.repository.user;

import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private Long counter = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        if (isEmailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setId(++counter);
        String name = user.getName();
        if (name == null) user.setName(user.getLogin());
        return users.put(user.getId(), user);
    }

    @Override
    public User update(User user) {
        @NonNull
        User updatableUser = users.get(user.getId());
        String newEmail = user.getEmail();
        final boolean emailExists = isEmailExists(newEmail);
        if (!emailExists) updatableUser.setEmail(newEmail);
        String newLogin = user.getLogin();
        if (newLogin != null) updatableUser.setLogin(newLogin);
        String newName = user.getName();
        if (!newName.isBlank()) updatableUser.setName(newName);
        else updatableUser.setName(updatableUser.getLogin());
        LocalDate newBirthday = user.getBirthday();
        if (newBirthday != null) updatableUser.setBirthday(newBirthday);
        updatableUser.setFriends(user.getFriends());
        return updatableUser;
    }

    private boolean isEmailExists(String newEmail) {
        return users
                .values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(newEmail));
    }

    @Override
    public User delete(User user) {
        return users.remove(user.getId());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(long id) {
        @NonNull
        User foundUser = users.get(id);
        return foundUser;
    }
}
