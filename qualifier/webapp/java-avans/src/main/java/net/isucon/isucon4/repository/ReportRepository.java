/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Manabu Matsuzaki
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
import me.geso.tinyorm.TinyORM;
import net.isucon.isucon4.row.LoginLog;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;

@Singleton
public class ReportRepository {

    @Inject
    private TinyORM orm;

    public List<LoginLog> getBannedIpsNotSucceed(int threshold) {
        return orm.searchBySQL(
                LoginLog.class,
                "SELECT ip FROM (SELECT ip, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY ip) AS t0 WHERE t0.max_succeeded = 0 AND t0.cnt >= ?",
                Collections.singletonList(threshold));
    }

    public List<LoginLog> getBannedIpsLastSucceed() {
        return orm.searchBySQL(
                LoginLog.class,
                "SELECT ip, MAX(id) AS id FROM login_log WHERE succeeded = 1 GROUP by ip");
    }

    public Pair<String, Long> getBannedIpsLastSucceedCounts(LoginLog loginLog) {
        long count = orm.count(LoginLog.class)
                .where("ip = ? AND id > ?",
                        loginLog.getIp(), loginLog.getId())
                .execute();
        return new Pair<>(loginLog.getIp(), count);
    }

    public List<LoginLog> getLockedUsersNotSucceed(int threshold) {
        return orm.searchBySQL(
                LoginLog.class,
                "SELECT login FROM (SELECT user_id, login, MAX(succeeded) as max_succeeded, COUNT(1) as cnt FROM login_log GROUP BY user_id) AS t0 WHERE t0.user_id IS NOT NULL AND t0.max_succeeded = 0 AND t0.cnt >= ?",
                Collections.singletonList(threshold));
    }

    public List<LoginLog> getLockedUsersLastSucceed() {
        return orm.searchBySQL(
                LoginLog.class,
                "SELECT user_id AS userId, login, MAX(id) AS id FROM login_log WHERE user_id IS NOT NULL AND succeeded = 1 GROUP BY user_id");
    }

    public Pair<String, Long> getLockedUsersLastSucceedCounts(LoginLog loginLog) {
        long count = orm.count(LoginLog.class)
                .where("user_id = ? AND id > ?",
                        loginLog.getUserId(), loginLog.getId())
                .execute();
        return new Pair<>(loginLog.getLogin(), count);
    }
}
