package net.isucon.isucon4.module;

import net.isucon.isucon4.config.Config;
import net.isucon.isucon4.provider.ConfigProvider;

import com.google.inject.AbstractModule;

public class BasicModule extends AbstractModule {
	private final Config config;

	public BasicModule() {
		this.config = null;
	}

	public BasicModule(final Config config) {
		this.config = config;
	}

	@Override
	protected void configure() {
		if (config != null) {
			bind(Config.class)
				.toInstance(config);
		} else {
			bind(Config.class)
				.toProvider(ConfigProvider.class)
				.asEagerSingleton();
		}
	}
}
