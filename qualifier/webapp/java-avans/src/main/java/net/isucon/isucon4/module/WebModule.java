package net.isucon.isucon4.module;

import com.google.inject.AbstractModule;
import lombok.NonNull;

import javax.servlet.ServletContext;

public class WebModule extends AbstractModule {
    private final ServletContext servletContext;

    public WebModule(@NonNull final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void configure() {
        bind(ServletContext.class)
                .toInstance(servletContext);
    }
}
