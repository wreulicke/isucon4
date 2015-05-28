package net.isucon.isucon4;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import net.isucon.isucon4.controller.LoginController;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Singleton
@MultipartConfig(maxFileSize = 5242880,
        maxRequestSize = 27262976,
        fileSizeThreshold = 32768)
public class Servlet extends HttpServlet {

    @Inject
    private Injector injector;

    private Dispatcher dispatcher;

    @Override
    public void init() {
        log.info("Initialized Servlet");
        dispatcher = new Dispatcher(injector);
        dispatcher.registerPackage(LoginController.class.getPackage());
    }

    @Override
    public void service(final ServletRequest req, final ServletResponse res)
            throws ServletException, IOException {
        this.dispatcher.handler(
                (HttpServletRequest) req,
                (HttpServletResponse) res);
    }

}
