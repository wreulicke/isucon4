package net.isucon.isucon4.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.geso.tinyorm.TinyORM;
import net.isucon.isucon4.provider.ConnectionProvider;
import net.isucon.isucon4.provider.TinyORMProvider;

import java.sql.Connection;

public class CLIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Connection.class)
                .toProvider(ConnectionProvider.class)
                .in(Scopes.SINGLETON);
        bind(TinyORM.class)
                .toProvider(TinyORMProvider.class);
    }
}
