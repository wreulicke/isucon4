package net.isucon.isucon4.provider;

import java.sql.Connection;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.inject.servlet.RequestScoped;

import me.geso.tinyorm.TinyORM;

@RequestScoped
public class TinyORMProvider implements Provider<TinyORM> {

	@Inject
	private Connection connection;

	@Override
	public TinyORM get() {
		return new TinyORM(connection);
	}
}
