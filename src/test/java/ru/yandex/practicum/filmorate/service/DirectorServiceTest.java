package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тестирование сервиса работы с режиссерами")
class DirectorServiceTest {

    @Mock
    private DirectorStorage directorStorage;

    @InjectMocks
    private DirectorService directorService;

    private Director director;

    @BeforeEach
    void setUp() {
        director = Director.builder()
                .id(1L)
                .name("Director Name")
                .build();
    }

    @Test
    @DisplayName("получение всего списка директоров")
    void findAll() {
        List<Director> directors = List.of(director);

        when(directorStorage.getAll()).thenReturn(directors);

        List<Director> result = directorService.findAll();

        assertNotNull(result);
        assertEquals(directors.size(), result.size());
        verify(directorStorage, times(1)).getAll();
    }

    @Test
    @DisplayName("успешное получение режиссера по ID")
    void findById() {
        when(directorStorage.read(1L))
                .thenReturn(Optional.of(Director.builder().id(1L).name("Director Name").build()));

        Director result = directorService.findById(1L);
        assertNotNull(result);
        verify(directorStorage, times(1)).read(1L);
    }

    @Test
    @DisplayName("успешное создание режиссера")
    void create() {
        when(directorStorage.create(any(Director.class))).thenReturn(director);

        Director result = directorService.create(director);
        assertNotNull(result);
        verify(directorStorage, times(1)).create(any(Director.class));
    }

    @Test
    @DisplayName("успешное обновление режиссера")
    void update() {
        when(directorStorage.update(any(Director.class))).thenReturn(director);

        Director result = directorService.update(director);
        assertNotNull(result);
        verify(directorStorage, times(1)).update(any(Director.class));
    }
}