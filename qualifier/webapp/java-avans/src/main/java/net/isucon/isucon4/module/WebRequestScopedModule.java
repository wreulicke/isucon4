package net.isucon.isucon4.module;

import java.sql.Connection;

import javax.sql.DataSource;

import net.isucon.isucon4.provider.DataSourceProvider;
import net.isucon.isucon4.provider.PooledConnectionProvider;
import net.isucon.isucon4.provider.TinyORMProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletScopes;

import me.geso.tinyorm.TinyORM;

public class WebRequestScopedModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(DataSource.class)
			.toProvider(DataSourceProvider.class)
			.in(Scopes.SINGLETON);

		// TODO If you want to use connection pool, change to PooledConnectionProvider.
		bind(Connection.class)
		//	.toProvider(ConnectionProvider.class)
			.toProvider(PooledConnectionProvider.class)
			.in(ServletScopes.REQUEST);

		bind(TinyORM.class)
			.toProvider(TinyORMProvider.class)
			.in(ServletScopes.REQUEST);
	}
}
