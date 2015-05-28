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

package net.isucon.isucon4.provider;

import com.zaxxer.hikari.HikariDataSource;
import net.isucon.isucon4.config.Config;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class DataSourceProvider implements Provider<DataSource> {

    private static final int MINIMUM_IDLE =
            System.getProperty("minimumIdle") != null
                    ? Integer.parseInt(System.getProperty("minimumIdle"))
                    : 16;

    private static final int MAXIMUM_POOL_SIZE =
            System.getProperty("maximumPoolSize") != null
                    ? Integer.parseInt(System.getProperty("maximumPoolSize"))
                    : 200;

    private HikariDataSource dataSource;

    @Inject
    public DataSourceProvider(Config config) {
        dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl(config.getJdbc().getUrl());
        dataSource.setUsername(config.getJdbc().getUsername());
        dataSource.setPassword(config.getJdbc().getPassword());
        dataSource.setAutoCommit(false);
        dataSource.setMinimumIdle(MINIMUM_IDLE);
        dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
    }

    @Override
    public DataSource get() {
        return dataSource;
    }
}
