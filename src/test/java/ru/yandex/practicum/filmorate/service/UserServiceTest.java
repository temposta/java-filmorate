package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.dal.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.dal.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserStorage userStorage;

    @Mock
    private FriendshipStorage friendshipStorage;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User friendUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .login("testLogin")
                .name("Test User")
                .birthday(LocalDate.of(2000, 6, 15))
                .build();

        friendUser = User.builder()
                .id(2L)
                .email("friend@example.com")
                .login("friendLogin")
                .name("Friend User")
                .birthday(LocalDate.of(2000, 6, 15))
                .build();
    }

    // ------------------------- Тесты для create() -------------------------
    @Test
    void createUser_Success() {
        when(userStorage.create(any(User.class))).thenReturn(testUser);

        User result = userService.create(testUser);

        assertEquals(testUser, result);
        verify(userStorage, times(1)).create(testUser);
    }

    // ------------------------- Тесты для read() -------------------------
    @Test
    void readUser_WithNonExistingId_ThrowsNotFoundException() {
        when(userStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.read(1L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    // ------------------------- Тесты для update() -------------------------
    @Test
    void updateUser_WithNonExistingId_ThrowsNotFoundException() {
        when(userStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.update(testUser)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    @Test
    void updateUser_Success() {
        when(userStorage.read(anyLong())).thenReturn(Optional.of(testUser));
        when(userStorage.update(any(User.class))).thenReturn(testUser);

        User result = userService.update(testUser);

        assertEquals(testUser, result);
        verify(userStorage, times(1)).update(testUser);
    }

    // ------------------------- Тесты для delete() -------------------------
    @Test
    void deleteUser_Success() {
        userService.delete(1L);
        verify(userStorage, times(1)).delete(1L);
    }

    // ------------------------- Тесты для getFriends() -------------------------
    @Test
    void getFriends_WithNonExistingUser_ThrowsNotFoundException() {
        when(userStorage.read(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getFriends(1L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 1L), exception.getMessage());
    }

    // ------------------------- Тесты для getFriendsCommonOther() -------------------------
    @Test
    void getCommonFriends_WithNonExistingUser_ThrowsNotFoundException() {
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getFriendsCommonOther(1L, 2L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 2L), exception.getMessage());
    }

    @Test
    void getCommonFriends_Success() {
        List<User> expectedFriends = Collections.singletonList(friendUser);
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.of(friendUser));
        when(userStorage.getFriendsCommonOther(any(User.class), any(User.class))).thenReturn(expectedFriends);

        List<User> result = userService.getFriendsCommonOther(1L, 2L);

        assertEquals(expectedFriends, result);
    }

    // ------------------------- Тесты для addFriend() -------------------------
    @Test
    void addFriend_WithSameUser_ThrowsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.addFriend(1L, 1L)
        );

        assertEquals("Нельзя добавить в друзья самого себя", exception.getMessage());
    }

    @Test
    void addFriend_WithNonExistingUser_ThrowsNotFoundException() {
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.addFriend(1L, 2L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 2L), exception.getMessage());
    }

    @Test
    void addFriend_Success() {
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.of(friendUser));

        userService.addFriend(1L, 2L);

        verify(friendshipStorage, times(1)).create(1L, 2L);
    }

    // ------------------------- Тесты для removeFriend() -------------------------
    @Test
    void removeFriend_WithNonExistingUser_ThrowsNotFoundException() {
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.removeFriend(1L, 2L)
        );

        assertEquals(String.format(ExceptionMessages.USER_NOT_FOUNT_ERROR, 2L), exception.getMessage());
    }

    @Test
    void removeFriend_Success() {
        when(userStorage.read(1L)).thenReturn(Optional.of(testUser));
        when(userStorage.read(2L)).thenReturn(Optional.of(friendUser));

        userService.removeFriend(1L, 2L);

        verify(friendshipStorage, times(1)).delete(1L, 2L);
    }

    // ------------------------- Дополнительные тесты -------------------------
    @Test
    void getAllUsers_Success() {
        List<User> expectedUsers = Collections.singletonList(testUser);
        when(userStorage.getAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAll();

        assertEquals(expectedUsers, result);
        verify(userStorage, times(1)).getAll();
    }
}