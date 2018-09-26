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

import net.isucon.isucon4.Pair;
import net.isucon.isucon4.entity.LoginLog;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    
    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<LoginLog> rowLoginLogMapper = new BeanPropertyRowMapper<>(LoginLog.class);

    public List<LoginLog> getBannedIpsNotSucceed(int threshold) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("threshold", threshold);

        return jdbcTemplate.query(
                "SELECT ip FROM (SELECT ip, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY ip) AS t0 WHERE t0.max_succeeded = 0 AND t0.cnt >= :threshold",
                param,
                rowLoginLogMapper);
    }

    public List<LoginLog> getBannedIpsLastSucceed() {
        return jdbcTemplate.query(
                "SELECT ip, MAX(id) AS id FROM login_log WHERE succeeded = 1 GROUP by ip",
                rowLoginLogMapper);
    }

    public Pair<String, Long> getBannedIpsLastSucceedCounts(LoginLog loginLog) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ip", loginLog.getIp())
                .addValue("id", loginLog.getId());

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS cnt FROM login_log WHERE ip = :ip AND :id < id",
                param,
                Long.class);

        return new Pair<>(loginLog.getIp(), count);
    }

    public List<LoginLog> getLockedUsersNotSucceed(int threshold) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("threshold", threshold);

        return jdbcTemplate.query(
                "SELECT login FROM (SELECT user_id, login, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY user_id) AS t0 WHERE t0.user_id IS NOT NULL AND t0.max_succeeded = 0 AND t0.cnt >= :threshold",
                param,
                rowLoginLogMapper);
    }

    public List<LoginLog> getLockedUsersLastSucceed() {
        return jdbcTemplate.query(
                "SELECT user_id, login, MAX(id) AS id FROM login_log WHERE user_id IS NOT NULL AND succeeded = 1 GROUP BY user_id",
                rowLoginLogMapper);
    }

    public Pair<String, Long> getLockedUsersLastSucceedCounts(LoginLog loginLog) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_id", loginLog.getUserId())
                .addValue("id", loginLog.getId());

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS cnt FROM login_log WHERE user_id = :user_id AND :id < id",
                param,
                Long.class);

        return new Pair<>(loginLog.getLogin(), count);
    }
}
