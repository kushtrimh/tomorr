package com.kushtrimh.tomorr.dal.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @author Kushtrim Hajrizi
 */
public class TestDatabaseExtension implements BeforeAllCallback, AfterAllCallback {

    private final Logger logger = LoggerFactory.getLogger(TestDatabaseExtension.class);

    private static GenericContainer<?> postgreSQLContainer;

    public static GenericContainer<?> getPostgreSQLContainer() {
        return postgreSQLContainer;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2")
                .withDatabaseName("tomorrtest")
                .withUsername("postgres")
                .withPassword("postgres")
                .withInitScript("testdata/test-setup.sql")
                .withExposedPorts(5432);
        postgreSQLContainer.start();
        logger.info("Started PostgreSQL container at {}/{}",
                postgreSQLContainer.getHost(),
                postgreSQLContainer.getFirstMappedPort());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (postgreSQLContainer != null) {
            postgreSQLContainer.close();
            logger.info("Closed PostgreSQL container");
        }
    }
}
