package net.isucon.isucon4;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import net.isucon.isucon4.config.Config;
import net.isucon.isucon4.module.BasicModule;
import net.isucon.isucon4.module.WebModule;
import net.isucon.isucon4.module.WebRequestScopedModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {

	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext = servletContextEvent.getServletContext();
		super.contextInitialized(servletContextEvent);
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(
			buildBasicModule(),
			new WebModule(),
			new WebRequestScopedModule(),
			new ServletModule() {
				@Override
				protected void configureServlets() {
					serve("/images/*", "/stylesheets/*").with(DefaultServlet.class);
					serve("/*").with(Servlet.class);
				}
			});
	}

	private BasicModule buildBasicModule() {
		Object config = servletContext.getAttribute("java-avans.config");
		if (config != null && config instanceof Config) {
			return new BasicModule((Config)config);
		} else {
			return new BasicModule();
		}
	}
}
