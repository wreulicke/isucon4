package net.isucon.isucon4.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.geso.tinyorm.TinyORM;
import net.isucon.isucon4.provider.ConnectionProvider;
import net.isucon.isucon4.provider.TinyORMProvider;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;

public class WebRequestScopedModule extends AbstractModule {
    private final HttpServletRequest request;
    private final ConnectionProvider connectionProvider;

    public WebRequestScopedModule(final HttpServletRequest request, final ConnectionProvider connectionProvider) {
        this.request = request;
        this.connectionProvider = connectionProvider;
    }

    @Override
    protected void configure() {
        bind(HttpServletRequest.class)
                .toInstance(request);
        bind(Connection.class)
                .toProvider(connectionProvider)
                .in(Scopes.SINGLETON);
        bind(TinyORM.class)
                .toProvider(TinyORMProvider.class);
    }
}
