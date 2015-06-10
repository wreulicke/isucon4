package net.isucon.isucon4.provider;

import com.google.inject.servlet.RequestScoped;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a JDBC connection provider.
 */
@Slf4j
@RequestScoped
public class RequestScopedConnectionProvider extends ConnectionProvider {
}
