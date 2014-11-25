/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Manabu Matsuzaki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.isucon.isucon4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Autowired
    DataSourceProperties properties;

    @Bean(destroyMethod = "close")
    DataSource dataSource() {
        DataSourceBuilder factory = DataSourceBuilder
                .create(this.properties.getClassLoader())
                .url(this.properties.getUrl())
                .username(this.properties.getUsername())
                .password(this.properties.getPassword());
        return factory.build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                // URLにセッションIDを含めない
                Set<SessionTrackingMode> set = new HashSet<>();
                set.add(SessionTrackingMode.COOKIE);
                servletContext.setSessionTrackingModes(set);
            }
        };
    }

//    /**
//     * Jettyカスタマイズ
//     */
//    @Bean
//    public EmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory(
//            @Value("${jetty.threadPool.minThreads:8}") int minThreads,
//            @Value("${jetty.threadPool.maxThreads:200}") int maxThreads,
//            @Value("${jetty.threadPool.idleTimeout:60000}") int idleTimeout) {
//
//        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
//        factory.addServerCustomizers(new JettyServerCustomizer() {
//            @Override
//            public void customize(Server server) {
//                QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
//                threadPool.setMaxThreads(maxThreads);
//                threadPool.setMinThreads(minThreads);
//                threadPool.setIdleTimeout(idleTimeout);
//            }
//        });
//
//        return factory;
//    }

//    /**
//     * Undertowカスタマイズ
//     */
//    @Bean
//    public EmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory(
//            @Value("${undertow.builder.workerThreads:200}") int workerThreads) {
//
//        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
//        factory.setWorkerThreads(workerThreads);
//
//        return factory;
//    }
}
