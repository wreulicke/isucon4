package net.isucon.isucon4;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.isucon.isucon4.config.Config;
import net.isucon.isucon4.module.BasicModule;
import net.isucon.isucon4.module.WebModule;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class Servlet extends HttpServlet {
    private me.geso.avans.Dispatcher dispatcher;

    @Override
    public void init() {
        log.info("Initialized Servlet");
        final Injector injector = Guice.createInjector(
                this.buildModule(getServletConfig()),
                new WebModule(getServletContext()));
        dispatcher = new Dispatcher(injector);
        dispatcher.registerPackage(net.isucon.isucon4.controller.LoginController.class.getPackage());
    }

    private BasicModule buildModule(ServletConfig servletConfig) {
        final Object config = servletConfig.getServletContext().getAttribute("java-avans.config");
        if (config != null && config instanceof Config) {
            return new BasicModule((Config) config);
        } else {
            return new BasicModule();
        }
    }

    @Override
    public void service(final ServletRequest req, final ServletResponse res)
            throws ServletException, IOException {
        this.dispatcher.handler(
                (HttpServletRequest) req,
                (HttpServletResponse) res);
    }

}
