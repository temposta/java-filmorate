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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> findAll() {
        log.info("Запрос на получение всех режиссеров");
        List<Director> directors = directorService.findAll();
        log.info("Получен список режиссеров - {}", directors);
        return directors;
    }

    @GetMapping("/{id}")
    public Director findById(@PathVariable Long id) {
        log.info("Запрос на получение режиссера с id = {}", id);
        Director director = directorService.findById(id);
        log.info("Получен режиссер - {}", director);
        return director;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director save(@Valid @RequestBody Director director) {
        log.info("Получен запрос на создание режиссера: {}", director);
        director = directorService.create(director);
        log.info("Создан режиссер - {}", director);
        return director;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director update(@Valid @RequestBody Director director) {
        log.info("Получен запрос на изменение режиссера, исх.данные: {}", director);
        Director directorUpdate = directorService.update(director);
        log.info("Режиссер обновлен, результат - {}", directorUpdate);
        return directorUpdate;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        log.info("Получен запрос на удаление режиссера с идентификатором: {}", id);
        directorService.delete(id);
        log.info("Режиссер с id = {} удален", id);
    }
}