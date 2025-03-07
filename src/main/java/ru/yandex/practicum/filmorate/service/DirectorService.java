package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

/**
 * Сервис для работы с режиссерами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    /**
     * Получает список всех режиссеров.
     *
     * @return Список всех режиссеров.
     */
    public List<Director> findAll() {
        return directorStorage.getAll();
    }

    /**
     * Получает режиссера по его ID.
     *
     * @param id ID режиссера для получения.
     * @return Режиссер с указанным ID.
     * @throws NotFoundException если режиссер с указанным ID не найден.
     */
    public Director findById(Long id) {
        return directorStorage.read(id)
                .orElseThrow(() -> new NotFoundException(String.format(ExceptionMessages.DIRECTOR_NOT_FOUND_ERROR, id)));
    }

    /**
     * Создает нового режиссера.
     *
     * @param director Режиссер для создания.
     * @return Созданный режиссер.
     */
    public Director create(Director director) {
        return directorStorage.create(director);
    }

    /**
     * Обновляет режиссера.
     *
     * @param director Режиссер для обновления.
     * @return Обновленный режиссер.
     * @throws ValidationException если нет ID обновляемого режиссера.
     * @throws NotFoundException   если режиссера с указанным ID не существует.
     */
    public Director update(Director director) {
        if (director.getId() == null) {
            log.error("Не указан id режиссера {}", director);
            throw new ValidationException("Id должен быть указан");
        }

        if (directorStorage.read(director.getId()).isEmpty()) {
            String error = String.format(ExceptionMessages.DIRECTOR_NOT_FOUND_ERROR, director.getId());
            log.error(error);
            throw new NotFoundException(error);
        }

        return directorStorage.update(director);
    }

    /**
     * Удаляет режиссера с указанным ID.
     *
     * @param id ID режиссера для удаления.
     */
    public void delete(Long id) {
        directorStorage.delete(id);
    }
}