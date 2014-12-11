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

import net.isucon.isucon4.RepositoryConfig;
import net.isucon.isucon4.entity.User;
import org.seasar.doma.Dao;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;

import java.util.Optional;

@Dao
@RepositoryConfig
public interface LoginRepository {

    default Optional<User> findUserByLogin(String login) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT * FROM users WHERE login = ")
                .param(String.class, login);

        Optional<User> user = builder.getOptionalEntitySingleResult(User.class);

        return user;
    }

    default long countBannedIp(String ip) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT COUNT(1) AS failures FROM login_log ")
                .sql("WHERE ip = ").param(String.class, ip)
                .sql("  AND id > IFNULL((select id from login_log where ip = ").param(String.class, ip)
                .sql("  AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)");

        long count = builder.getScalarSingleResult(long.class);

        return count;
    }

    default long countLockedUser(int userId) {

        Config config = Config.get(this);
        SelectBuilder builder = SelectBuilder.newInstance(config);

        builder.sql("SELECT COUNT(1) AS failures FROM login_log ")
                .sql("WHERE user_id = ").param(int.class, userId)
                .sql("  AND id > IFNULL((select id from login_log where user_id = ").param(int.class, userId)
                .sql("  AND succeeded = 1 ORDER BY id DESC LIMIT 1), 0)");

        long count = builder.getScalarSingleResult(long.class);

        return count;
    }
}
