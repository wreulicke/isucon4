package net.isucon.isucon4.cli;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import me.geso.tinyorm.TinyORM;
import net.isucon.isucon4.module.BasicModule;
import net.isucon.isucon4.module.CLIModule;

import javax.inject.Inject;
import java.sql.SQLException;

@Slf4j
public class SampleCLI {
    public static void main(String[] args) throws SQLException {
        final Injector injector = Guice.createInjector(new BasicModule(), new CLIModule());
        final SampleCLI instance = injector.getInstance(SampleCLI.class);
        instance.run();
    }

    @Inject
    public SampleCLI(final TinyORM db) {
        this.db = db;
    }

    private final TinyORM db;

    public void run() throws SQLException {
        final String databaseProductName = db.getConnection()
                .getMetaData().getDatabaseProductName();
        log.info("{}", databaseProductName);
    }
}
