package net.isucon.isucon4.provider;

import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.isucon.isucon4.config.Config;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is a JDBC connection provider.
 * This class implements Closeable interface.
 * You can close the connection after work.
 */
@Slf4j
public class ConnectionProvider implements Provider<Connection>, Closeable {

    static final int MAXIMUM_POOL_SIZE =
            System.getProperty("maximumPoolSize") != null
                    ? Integer.parseInt(System.getProperty("maximumPoolSize"))
                    : 200;

    private static final HikariDataSource dataSource = new HikariDataSource();
    private final Config config;
    private Connection connection;

    @Inject
    public ConnectionProvider(Config config) {
        this.config = config;

        if (Strings.isNullOrEmpty(dataSource.getDriverClassName())) {

            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl(config.getJdbc().getUrl());
            dataSource.setUsername(config.getJdbc().getUsername());
            dataSource.setPassword(config.getJdbc().getPassword());
            dataSource.setAutoCommit(false);
            dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        }
    }

    public Connection get() {
        try {
            log.debug("Get JDBC connection from pool: {}", config.getJdbc().getUrl());
            connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Close last connection.
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            log.debug("Closed DB Connection");
        }
    }
}
