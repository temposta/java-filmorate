package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.Repository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends Repository<User> {

    void checkUser(Long id);

    List<User> getListUsers(Set<Long> ids);

}
