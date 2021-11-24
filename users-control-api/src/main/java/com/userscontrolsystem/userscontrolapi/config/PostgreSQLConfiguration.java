package com.userscontrolsystem.userscontrolapi.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgreSQLConfiguration {

	@Value("${postgres.database.driver}")
	private String driveClassName;

	@Value("${postgres.datasource.url}")
	private String url;

	@Value("${postgres.datasource.username}")
	private String username;

	@Value("${postgres.datasource.password}")
	private String password;
	
    @Bean
    public DataSource getPostgreSQLDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(driveClassName);
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }
}