package net.isucon.isucon4.provider;

import com.google.inject.servlet.RequestScoped;
import me.geso.tinyorm.TinyORM;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequestScoped
public class TinyORMProvider implements Provider<TinyORM> {
    @Inject
    private DataSource dataSource;

    private Connection connection;

    @Override
    public TinyORM get() {
        try {
            if (connection == null) {
                connection = dataSource.getConnection();
            }
            return new TinyORM(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
