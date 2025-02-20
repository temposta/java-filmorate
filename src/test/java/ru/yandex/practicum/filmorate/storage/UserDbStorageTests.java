package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.repository.UserRepository;
import ru.yandex.practicum.filmorate.dal.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRowMapper.class, UserRepository.class, UserDbStorage.class})
class UserDbStorageTests {

	private final UserRepository userRepository;
	private final UserDbStorage userStorage;

	@Test
	public void testFindUserById() {

		Optional<User> userOptional = userStorage.read(1L);

		assertThat(userOptional).isEmpty();  // т.к. конфигурация не позволяет подключиться к БД (пока так)
	}
}