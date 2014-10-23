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

import javafx.util.Pair;
import net.isucon.isucon4.model.IpLastSucceeds;
import net.isucon.isucon4.model.UserNotSucceeds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<String> rowStringMapper = new SingleColumnRowMapper<>(String.class);

    RowMapper<IpLastSucceeds> rowIpLastSucceedsMapper = new BeanPropertyRowMapper<>(IpLastSucceeds.class);

    RowMapper<UserNotSucceeds> rowUserNotSucceedsMapper = new BeanPropertyRowMapper<>(UserNotSucceeds.class);

    public List<String> getBannedIpsNotSucceed(int threshold) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("threshold", threshold);

        List<String> list = jdbcTemplate.query(
                "SELECT ip FROM (SELECT ip, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY ip) AS t0 WHERE t0.max_succeeded = 0 AND t0.cnt >= :threshold",
                param,
                rowStringMapper);

        return list;
    }

    public List<IpLastSucceeds> getBannedIpsLastSucceed() {
        return jdbcTemplate.query(
                "SELECT ip, MAX(id) AS last_login_id FROM login_log WHERE succeeded = 1 GROUP by ip",
                rowIpLastSucceedsMapper);
    }

    public Pair<String, Long> getBannedIpsLastSucceedCounts(IpLastSucceeds ipLastSucceeds) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ip", ipLastSucceeds.getIp())
                .addValue("last_login_id", ipLastSucceeds.getLastLoginId());

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS cnt FROM login_log WHERE ip = :ip AND :last_login_id < id",
                param,
                Long.class);

        return new Pair<>(ipLastSucceeds.getIp(), count);
    }

    public List<String> getLockedUsersNotSucceed(int threshold) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("threshold", threshold);

        List<String> list = jdbcTemplate.query(
                "SELECT login FROM (SELECT user_id, login, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY user_id) AS t0 WHERE t0.user_id IS NOT NULL AND t0.max_succeeded = 0 AND t0.cnt >= :threshold",
                param,
                rowStringMapper);

        return list;
    }

    public List<UserNotSucceeds> getLockedUsersLastSucceed() {
        return jdbcTemplate.query(
                "SELECT user_id, login, MAX(id) AS last_login_id FROM login_log WHERE user_id IS NOT NULL AND succeeded = 1 GROUP BY user_id",
                rowUserNotSucceedsMapper);
    }

    public Pair<String, Long> getLockedUsersLastSucceedCounts(UserNotSucceeds userNotSucceeds) {

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_id", userNotSucceeds.getUserId())
                .addValue("last_login_id", userNotSucceeds.getLastLoginId());

        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) AS cnt FROM login_log WHERE user_id = :user_id AND :last_login_id < id",
                param,
                Long.class);

        return new Pair<>(userNotSucceeds.getLogin(), count);
    }
}
