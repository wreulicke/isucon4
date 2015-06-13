package net.isucon.isucon4.config;

import lombok.Data;

@Data
public class Config {
	private boolean development;
	private JDBCConfig jdbc;
	private DataSourceConfig dataSource;
}
