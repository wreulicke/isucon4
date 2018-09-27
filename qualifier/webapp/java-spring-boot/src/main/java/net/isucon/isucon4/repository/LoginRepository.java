/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Manabu Matsuzaki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.isucon.isucon4.repository;

import net.isucon.isucon4.entity.LoginLog;
import net.isucon.isucon4.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);

    RowMapper<LoginLog> rowIpLastSucceedsMapper = new BeanPropertyRowMapper<>(LoginLog.class);
    
    @Cacheable("user")
    public Optional<User> findUserByLogin(String login) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("login", login);

        User user = null;

        try {
            user = jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE login = :login",
                    param,
                    rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

        return Optional.ofNullable(user);
    }

    public long countBannedIp(String ip) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ip", ip);
        long failures = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS failures FROM login_log " +
                        "WHERE ip = :ip AND id > IFNULL((select id from login_log where ip = :ip AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)",
                param,
                Long.class);

        return failures;
    }

    public long countLockedUser(int userId) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        long failures = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS failures FROM login_log " +
                        "WHERE user_id = :userId AND id > IFNULL((select id from login_log where user_id = :userId AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)",
                param,
                Long.class);

        return failures;
    }

    public LoginLog findLoginLogByUserId(int userId) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("userId", userId);
        List<LoginLog> loginLogs = jdbcTemplate.query(
                "SELECT * FROM login_log WHERE succeeded = 1 AND user_id = :userId ORDER BY id DESC LIMIT 2",
                param,
                rowIpLastSucceedsMapper);

        return loginLogs.get(loginLogs.size() - 1);
    }
}
