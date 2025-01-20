package ru.yandex.practicum.filmorate.repository;

import java.util.List;

public interface Repository<T> {
    T add(T entity);

    T update(T entity);

    T delete(T entity);

    List<T> getAll();

    T findById(long id);
}
