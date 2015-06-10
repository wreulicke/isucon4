package net.isucon.isucon4.module;

import net.isucon.isucon4.provider.web.FreemarkerConfigurationProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import freemarker.template.Configuration;

public class WebModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Configuration.class)
			.toProvider(FreemarkerConfigurationProvider.class)
			.in(Scopes.SINGLETON);
	}
}
