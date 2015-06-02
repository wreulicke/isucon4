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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    // SELECT項目が数パターンあるので、本来ならばEntityをそれぞれ作って、
    // RepositoryもEntityと1対1で作らないといけないが、
    // 作るのが面倒なので、EntityはLoginLogを使いまわしている。
    // JPAの1次キャッシュ機能により、Entityのidが同じものがキャッシュされていれば
    // SELECT結果をマッピングしてnewされた新しいEntityではなく、キャッシュ済みの前回のEntityを取得してしまう
    // それを避けるためにidの開始値(@i:=xxx)を各クエリでずらしている
    // 非常にダメなやり方なので、真似しないように！

    @Query(value = "SELECT ip, @i\\:=@i+1 AS id, null AS created_at, null AS user_id, null AS login, 0 AS succeeded FROM (SELECT ip, MAX(succeeded) as max_succeeded, COUNT(1) as cnt, @i\\:=0 FROM login_log GROUP BY ip) AS t0 WHERE t0.max_succeeded = 0 AND t0.cnt >= :threshold",
            nativeQuery = true)
    List<LoginLog> getBannedIpsNotSucceed(@Param("threshold") int threshold);

    @Query(value = "SELECT * FROM login_log WHERE succeeded = 1 AND user_id = :userId ORDER BY id DESC LIMIT 2",
            nativeQuery = true)
    List<LoginLog> findLoginLogByUserId(@Param("userId") long userId);

    @Query(value = "SELECT ip, MAX(id) + 10000 AS id, null AS created_at, null AS user_id, null AS login, 0 AS succeeded FROM login_log WHERE succeeded = 1 GROUP by ip",
            nativeQuery = true)
    List<LoginLog> getBannedIpsLastSucceed();

    @Query(value = "SELECT login, @i\\:=@i+1 AS id, null AS created_at, null AS user_id, null AS ip, 0 AS succeeded FROM (SELECT user_id, login, MAX(succeeded) as max_succeeded, COUNT(1) as cnt, @i\\:=100000 FROM login_log GROUP BY user_id) AS t0 WHERE t0.user_id IS NOT NULL AND t0.max_succeeded = 0 AND t0.cnt >= :threshold",
            nativeQuery = true)
    List<LoginLog> getLockedUsersNotSucceed(@Param("threshold") int threshold);

    @Query(value = "SELECT user_id, login, MAX(id) + 20000 AS id, null AS created_at, null AS ip, 0 AS succeeded FROM login_log WHERE user_id IS NOT NULL AND succeeded = 1 GROUP BY user_id",
            nativeQuery = true)
    List<LoginLog> getLockedUsersLastSucceed();

    @Query(value = "SELECT COUNT(1) AS cnt FROM login_log WHERE ip = :ip AND :id < id",
            nativeQuery = true)
    long getBannedIpsLastSucceedCounts(@Param("ip") String ip, @Param("id") Long id);

    @Query(value = "SELECT COUNT(1) AS cnt FROM login_log WHERE user_id = :userId AND :id < id",
            nativeQuery = true)
    long getLockedUsersLastSucceedCounts(@Param("userId") Integer userId, @Param("id") Long id);

    @Query(value = "SELECT COUNT(1) AS failures FROM login_log " +
            "WHERE ip = :ip AND id > IFNULL((select id from login_log where ip = :ip AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)",
            nativeQuery = true)
    long countBannedIp(@Param("ip") String ip);

    @Query(value = "SELECT COUNT(1) AS failures FROM login_log " +
            "WHERE user_id = :userId AND id > IFNULL((select id from login_log where user_id = :userId AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)",
            nativeQuery = true)
    long countLockedUser(@Param("userId") long userId);
}
