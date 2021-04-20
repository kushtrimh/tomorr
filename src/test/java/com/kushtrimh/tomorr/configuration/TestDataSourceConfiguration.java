package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.album.repository.AlbumJooqRepository;
import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.dal.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
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
import org.testcontainers.containers.GenericContainer;

import javax.sql.DataSource;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class TestDataSourceConfiguration {
    private final DataSourceProperties dataSourceProperties;

    public TestDataSourceConfiguration() {
        var dataSourceProperties = new DataSourceProperties();
        GenericContainer<?> container = TestDatabaseExtension.getPostgreSQLContainer();
        dataSourceProperties.setUrl(String.format("jdbc:postgresql://%s:%d/tomorrtest",
                container.getHost(), container.getFirstMappedPort()));
        dataSourceProperties.setUsername("postgres");
        dataSourceProperties.setPassword("postgres");
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
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
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.setSQLDialect(SQLDialect.POSTGRES);
        jooqConfiguration.setConnectionProvider(connectionProvider);
        jooqConfiguration.setExecuteListener(exceptionTranslator);
        return jooqConfiguration;
    }

    @Bean
    public DSLContext dsl(DefaultConfiguration config) {
        return new DefaultDSLContext(config);
    }

    @Bean
    public AlbumRepository<AlbumRecord> albumRepository(DSLContext dsl) {
        return new AlbumJooqRepository(dsl);
    }
}
