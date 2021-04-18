package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.properties.DataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
@EnableTransactionManagement
public class DataAccessConfiguration {

    private final DataSourceProperties dataSourceProperties;

    public DataAccessConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    public DataSource dataSource() {
        var config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        return new HikariDataSource(config);
    }

    @Bean
    public TransactionAwareDataSourceProxy transactionAwareDataSourceProxy(DataSource dataSource) {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    @Bean
    public ConnectionProvider connectionProvider(TransactionAwareDataSourceProxy transactionAwareDataSourceProxy) {
        return new DataSourceConnectionProvider(transactionAwareDataSourceProxy);
    }

    @Bean
    public JooqExceptionTranslator exceptionTranslator() {
        return new JooqExceptionTranslator();
    }

    @Bean
    public DefaultConfiguration config(ConnectionProvider connectionProvider,
                                       JooqExceptionTranslator exceptionTranslator) {
        var jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.setSQLDialect(SQLDialect.POSTGRES);
        jooqConfiguration.setConnectionProvider(connectionProvider);
        jooqConfiguration.setExecuteListener(exceptionTranslator);
        return jooqConfiguration;
    }

    @Bean
    public DSLContext dsl(DefaultConfiguration config) {
        return new DefaultDSLContext(config);
    }
}
