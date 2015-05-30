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
import net.isucon.isucon4.RepositoryConfig;
import net.isucon.isucon4.entity.LoginLog;
import net.isucon.isucon4.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;

import java.util.List;

@Dao
@RepositoryConfig
public interface ReportRepository {

    default List<LoginLog> getBannedIpsNotSucceed(int threshold) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT ip FROM (SELECT ip, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY ip) AS t0 WHERE t0.max_succeeded = 0 AND t0.cnt >= ")
                .param(int.class, threshold);

        return builder.getEntityResultList(LoginLog.class);
    }

    default List<LoginLog> getBannedIpsLastSucceed() {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT ip, MAX(id) AS id FROM login_log WHERE succeeded = 1 GROUP by ip");

        return builder.getEntityResultList(LoginLog.class);
    }

    default Pair<String, Long> getBannedIpsLastSucceedCounts(LoginLog loginLog) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT COUNT(1) AS cnt FROM login_log WHERE ip = ")
                .param(String.class, loginLog.getIp())
                .sql(" AND id >")
                .param(long.class, loginLog.getId());

        Long count = builder.getScalarSingleResult(Long.class);

        return new Pair<>(loginLog.getIp(), count);
    }

    default List<User> getLockedUsersNotSucceed(int threshold) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT login FROM (SELECT user_id, login, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY user_id) AS t0 WHERE t0.user_id IS NOT NULL AND t0.max_succeeded = 0 AND t0.cnt >= ")
                .param(int.class, threshold);

        return builder.getEntityResultList(User.class);
    }

    default List<LoginLog> getLockedUsersLastSucceed() {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT user_id, login, MAX(id) AS id FROM login_log WHERE user_id IS NOT NULL AND succeeded = 1 GROUP BY user_id");

        return builder.getEntityResultList(LoginLog.class);
    }

    default Pair<String, Long> getLockedUsersLastSucceedCounts(LoginLog loginLog) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT COUNT(1) AS cnt FROM login_log WHERE user_id = ")
                .param(int.class, loginLog.getUserId())
                .sql(" AND id > ")
                .param(long.class, loginLog.getId());

        Long count = builder.getScalarSingleResult(Long.class);

        return new Pair<>(loginLog.getLogin(), count);
    }
}
