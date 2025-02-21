package ru.yandex.practicum.filmorate.repository.film.friend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("тестирование FriendRepositoryImpl")
class FriendRepositoryImplTest {

    DataSource dataSource;
    NamedParameterJdbcOperations jdbc;
    FriendRepository repository;

    public FriendRepositoryImplTest() {
        dataSource = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("classpath:/schema.sql")
                .addScript("classpath:/test-data.sql")
                .build();
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.repository = new FriendRepositoryImpl(jdbc);
    }

    @Test
    @DisplayName("добавление друга без ошибки")
    void addFriend() {
        assertDoesNotThrow(() -> repository.addFriend(4L, 3L));
    }

    @Test
    @DisplayName("добавление друга с неверным user_id")
    void addFriendWithIncorrectUserId() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.addFriend(555L, 5L));
    }

    @Test
    @DisplayName("добавление друга с неверным friend_id")
    void addFriendWithIncorrectFriendId() {
        assertThrows(DataIntegrityViolationException.class, () -> repository.addFriend(5L, 555L));
    }

    @Test
    @DisplayName("удаление друга без ошибки")
    void removeFriend() {
        assertDoesNotThrow(() -> repository.removeFriend(2L, 3L));
    }

    @Test
    @DisplayName("получение списка id друзей")
    void getFriendIds() {
        List<Long> friendIds = repository.getFriendIds(5L)
                .stream()
                .sorted(Comparator.comparingLong(Long::longValue))
                .toList();
        assertIterableEquals(friendIds, List.of(2L, 3L, 4L, 6L));
    }

    @Test
    @DisplayName("получение списка id общих друзей")
    void getCommonFriendIds() {
        List<Long> commonFriends = repository.getCommonFriendIds(2L, 5L)
                .stream()
                .sorted(Comparator.comparingLong(Long::longValue))
                .toList();
        assertIterableEquals(commonFriends, List.of(3L, 6L));
    }

    @Test
    @DisplayName("получение наборов id друзей для каждого id пользователя")
    void getAllFriendSets() {
        Map<Long, Set<Long>> friendSets = repository.getAllFriendSets();
        List<Long> l;
        l = friendSets.get(2L)
                .stream()
                .sorted(Comparator.comparingLong(Long::longValue))
                .toList();
        assertIterableEquals(l, List.of(3L, 6L));

        l = friendSets.get(6L)
                .stream()
                .sorted(Comparator.comparingLong(Long::longValue))
                .toList();
        assertIterableEquals(l, List.of(2L, 5L));

        l = friendSets.get(5L)
                .stream()
                .sorted(Comparator.comparingLong(Long::longValue))
                .toList();
        assertIterableEquals(l, List.of(2L, 3L, 4L, 6L));

        assertEquals(3, friendSets.size());
    }
}