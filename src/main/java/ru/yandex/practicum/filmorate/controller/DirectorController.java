package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

/**
 * Контроллер для обработки запросов, связанных с режиссерами.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    /**
     * Получает всех режиссеров фильмов
     *
     * @return Список режиссеров фильмов
     */
    @GetMapping
    public List<Director> findAll() {
        log.info("Получение всех режиссеров");
        List<Director> directors = directorService.findAll();
        log.info("Получен список всех режиссеров: {}", directors);
        return directors;
    }

    /**
     * Получает режиссера по его id
     *
     * @param id ID директора для получения.
     * @return режиссер с указанным id.
     */
    @GetMapping("/{id}")
    public Director findById(@PathVariable Long id) {
        log.info("Получение режиссера с id: {}", id);
        Director director = directorService.findById(id);
        log.info("Получен режиссер: {}", director);
        return director;
    }

    /**
     * Создает нового режиссера.
     *
     * @param director Режиссер для создания.
     * @return Созданный режиссер.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        log.info("Cозданиe режиссера: {}", director);
        director = directorService.create(director);
        log.info("Режиссер создан: - {}", director);
        return director;
    }

    /**
     * Обновляет существующего режиссера.
     *
     * @param director Режиссер для обновления.
     * @return Обновленный режиссер.
     */
    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Обновление режиссера: {}", director);
        Director directorUpdate = directorService.update(director);
        log.info("Режиссер обновлен: {}", directorUpdate);
        return directorUpdate;
    }

    /**
     * Удаляет режиссера по его ID.
     *
     * @param id ID режиссера для удаления.
     *           При удалении режиссера, режиссер удаляется также из связанных таблиц.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Удаление режиссера с id: {}", id);
        directorService.delete(id);
    }
}