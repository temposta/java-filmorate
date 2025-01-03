package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

    private int counter = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(++counter);
        if (user.getName().isBlank()) user.setName(user.getLogin());
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        User oldUser = users.get(user.getId());
        if (oldUser != null) {
            String newEmail = user.getEmail();
            boolean emailExists = users
                    .values()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(newEmail));
            if (!emailExists) oldUser.setEmail(newEmail);
            String newLogin = user.getLogin();
            if (!newLogin.isBlank()) oldUser.setLogin(newLogin);
            String newName = user.getName();
            if (!newName.isBlank()) oldUser.setName(newName);
            else  oldUser.setName(oldUser.getLogin());
            LocalDate newBirthday = user.getBirthday();
            if (newBirthday != null) oldUser.setBirthday(newBirthday);
            return oldUser;
        }
        return null;
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
