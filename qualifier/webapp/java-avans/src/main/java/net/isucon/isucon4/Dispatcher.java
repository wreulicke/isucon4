package net.isucon.isucon4;

import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import me.geso.avans.Controller;
import me.geso.tinyorm.TinyORM;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class Dispatcher extends me.geso.avans.Dispatcher {
    private final Injector injector;

    public Dispatcher(final Injector injector) {
        this.injector = injector;
    }

    @Override
    public void runController(
            final Class<? extends Controller> controllerClass,
            final Method method, final HttpServletRequest request,
            final HttpServletResponse response,
            final Map<String, String> captured) {

        Connection connection = null;

        try (Controller controller = injector.getInstance(controllerClass)) {
            connection = injector.getInstance(TinyORM.class).getConnection();
            controller.invoke(method, request, response, captured);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Connection close error!", e);
            }
        }
    }
}
