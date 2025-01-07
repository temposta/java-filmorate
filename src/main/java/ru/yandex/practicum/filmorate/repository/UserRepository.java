package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    User addUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

    List<User> getAllUsers();

}
