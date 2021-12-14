package com.kushtrimh.tomorr.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Kushtrim Hajrizi
 */
public class TestRedisExtension implements BeforeAllCallback {

    private final Logger logger = LoggerFactory.getLogger(TestRedisExtension.class);

    private static boolean started = false;
    private static GenericContainer<?> redisContainer;

    public static GenericContainer<?> getRedisContainer() {
        return redisContainer;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            redisContainer = new GenericContainer<>(DockerImageName.parse("redis:6.2.6"))
                    .withExposedPorts(6379);
            redisContainer.start();
            logger.info("Started Redis container at {}/{}",
                    redisContainer.getHost(),
                    redisContainer.getFirstMappedPort());
            started = true;
        }
    }
}
