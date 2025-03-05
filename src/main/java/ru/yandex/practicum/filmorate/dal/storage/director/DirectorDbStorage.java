package ru.yandex.practicum.filmorate.dal.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.repository.DirectorRepository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {

    final DirectorRepository directorRepository;

    @Override
    public List<Director> getAll() {
        return directorRepository.findAll();
    }

    @Override
    public Optional<Director> read(Long id) {
        return directorRepository.findById(id);
    }

    @Override
    public Director create(Director director) {
        return directorRepository.create(director);
    }

    @Override
    public Director update(Director director) {
        return directorRepository.update(director);
    }

    @Override
    public void delete(Long id) {
        directorRepository.delete(id);
    }
}