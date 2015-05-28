package net.isucon.isucon4.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.geso.tinyorm.TinyORM;
import net.isucon.isucon4.provider.DataSourceProvider;
import net.isucon.isucon4.provider.TinyORMProvider;

import javax.sql.DataSource;

public class WebRequestScopedModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DataSource.class)
                .toProvider(DataSourceProvider.class)
                .in(Scopes.SINGLETON);
        bind(TinyORM.class)
                .toProvider(TinyORMProvider.class);
    }
}
