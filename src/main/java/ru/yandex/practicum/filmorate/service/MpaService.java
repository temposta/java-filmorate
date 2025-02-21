package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    MpaRepository mr;

    public MpaService(MpaRepository mr) {
        this.mr = mr;
        log.info("MpaService created");
    }

    public List<Mpa> getMpa() {
        log.info("getMpa");
        return mr.getAll();
    }

    public Mpa getMpaById(int id) {
        log.info("getMpaById");
        return mr.findById(id);
    }

}
