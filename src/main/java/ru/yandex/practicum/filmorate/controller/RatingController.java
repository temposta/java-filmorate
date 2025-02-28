package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public List<Mpa> findAll() {
        log.info("Запрос на получение всех рейтингов");
        return ratingService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable Long id) {
        log.info("Запрос на получение рейтинга с id = {}", id);
        return ratingService.findById(id);
    }
}