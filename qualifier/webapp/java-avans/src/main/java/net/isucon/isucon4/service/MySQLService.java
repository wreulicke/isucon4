package net.isucon.isucon4.service;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is a java-avans service class.
 */
public class MySQLService {
    private final Connection connection;

    @Inject
    public MySQLService(Connection connection) {
        this.connection = connection;
    }

    public int getJDBCMajorVersion() throws SQLException {
        return this.connection.getMetaData().getJDBCMajorVersion();
    }
}
