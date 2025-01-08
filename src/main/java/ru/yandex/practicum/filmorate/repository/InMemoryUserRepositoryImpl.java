package ru.yandex.practicum.filmorate.repository;

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
    public User addUser(User user) {
        user.setId(++counter);
        String name = user.getName();
        if (name == null) user.setName(user.getLogin());
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        @NonNull
        User updatableUser = users.get(user.getId());
        String newEmail = user.getEmail();
        boolean emailExists = users
                .values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(newEmail));
        if (!emailExists) updatableUser.setEmail(newEmail);
        String newLogin = user.getLogin();
        if (newLogin != null) updatableUser.setLogin(newLogin);
        String newName = user.getName();
        if (!newName.isBlank()) updatableUser.setName(newName);
        else updatableUser.setName(updatableUser.getLogin());
        LocalDate newBirthday = user.getBirthday();
        if (newBirthday != null) updatableUser.setBirthday(newBirthday);
        return updatableUser;
    }

    @Override
    public User deleteUser(User user) {
        return users.remove(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
