package ru.otus.library.config;

import lombok.RequiredArgsConstructor;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LiquibaseDataSourceConfig {

    private final DataSourceProperties dataSourceProperties;

    @Bean
    @LiquibaseDataSource
    public DataSource liquibaseDataSource() {
        return DataSourceBuilder.create()
                .type(PGSimpleDataSource.class)
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .url(dataSourceProperties.getUrl())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .build();
    }

}
