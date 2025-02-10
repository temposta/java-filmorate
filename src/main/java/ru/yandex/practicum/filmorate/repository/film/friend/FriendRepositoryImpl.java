package ru.yandex.practicum.filmorate.repository.film.friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@Transactional
public class FriendRepositoryImpl implements FriendRepository {

    NamedParameterJdbcOperations jdbc;

    public FriendRepositoryImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        log.info("FriendRepositoryImpl created");
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = """
                INSERT INTO friends (user_id, friend_id)
                VALUES (:user_id, :friend_id)
                """;
        batchUpdateFriends(userId, friendId, sql);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sql = """
                DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id;
                """;
        batchUpdateFriends(userId, friendId, sql);

    }

    @Override
    public Set<Long> getFriendIds(Long userId) {
        String sql = """
                SELECT friend_id FROM friends WHERE user_id = :user_id;
                """;
        Set<Long> friendIds;
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        friendIds = jdbc.query(sql, params, rs -> {
            Set<Long> friendsSet = new HashSet<>();
            while (rs.next()) {
                friendsSet.add(rs.getLong("friend_id"));
            }
            return friendsSet;
        });
        return friendIds;
    }

    @Override
    public Set<Long> getCommonFriendIds(Long user1, Long user2) {
        /*
filmorate=# select friend_id, count(friend_id)
filmorate-# from friends
filmorate-# where user_id in (1,4)
filmorate-# group by friend_id
filmorate-# having count(friend_id)>1;
        */
        String sql = """
                SELECT friend_id, count(friend_id) as count
                FROM friends
                WHERE user_id IN (:user1, :user2)
                GROUP BY friend_id
                Having count(friend_id) > 1;
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("user1", user1);
        params.put("user2", user2);
        return jdbc.query(sql, params, rs -> {
            Set<Long> friendsSet = new HashSet<>();
            while (rs.next()) {
                friendsSet.add(rs.getLong("friend_id"));
            }
            return friendsSet;
        });
    }

    @Override
    public Map<Long, Set<Long>> getAllFriendSets() {
        String sql = """
                select user_id, friend_id from friends;
                """;
        return jdbc.query(sql, rs -> {
            Map<Long, Set<Long>> friendSets = new HashMap<>();
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                Long friendId = rs.getLong("friend_id");
                Set<Long> friendsSet;
                friendsSet = friendSets.get(userId);
                if (friendsSet == null) {
                    friendsSet = new HashSet<>();
                    friendsSet.add(friendId);
                    friendSets.put(userId, friendsSet);
                } else {
                    friendsSet.add(friendId);
                }
            }
            return friendSets;
        });
    }

    private void batchUpdateFriends(Long userId, Long friendId, String sql) {
        Map<String, Object> params = new HashMap<>();
        SqlParameterSource[] ps = new SqlParameterSource[2];
        ps[0] = new MapSqlParameterSource(params)
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);
        ps[1] = new MapSqlParameterSource(params)
                .addValue("user_id", friendId)
                .addValue("friend_id", userId);
        jdbc.batchUpdate(sql, ps);
    }
}
