package net.isucon.isucon4.controller;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import me.geso.avans.annotation.GET;
import me.geso.avans.annotation.Param;
import me.geso.tinyorm.TinyORM;
import me.geso.webscrew.response.WebResponse;
import net.isucon.isucon4.service.MySQLService;

import javax.inject.Inject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class RootController extends BaseController {
    @Inject
    private TinyORM db;
    @Inject
    private MySQLService mySQLService;

    @GET("/")
    public WebResponse index(@Param("name") Optional<String> name) throws IOException, TemplateException, SQLException {
        final String databaseProductName = db.getConnection()
                .getMetaData().getDatabaseProductName();
        return this.freemarker("index.html.ftl")
                .param("name", name.orElse("Anonymous"))
                .param("db", databaseProductName)
                .param("jdbcMajorVersion", mySQLService.getJDBCMajorVersion())
                .render();
    }
}

