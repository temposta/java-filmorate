package ru.yandex.practicum.filmorate.repository;

import java.util.List;

public interface Repository<T> {
    default T add(T entity) {
        return null;
    }

    default T update(T entity) {
        return null;
    }

    default T delete(T entity) {
        return null;
    }

    List<T> getAll();

    T findById(long id);
}
