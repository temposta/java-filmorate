package ru.yandex.practicum.filmorate.dal.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;


@Repository
public class EventRepository {

    private static final String SAVE_EVENT = "INSERT INTO events " +
            "(timestamp, user_id, event_type, operation, entity_id) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_EVENT_BY_USER_ID = "SELECT * FROM events WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Event event) {
        jdbcTemplate.update(SAVE_EVENT, event.getTimestamp(), event.getUserId(), event.getEventType().name(),
                event.getOperation().name(), event.getId());
    }

    public List<Event> getUserEvents(Long id) {
        return jdbcTemplate.query(FIND_EVENT_BY_USER_ID, new EventRowMapper(), id);
    }
}
