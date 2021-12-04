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
public class TestRabbitMQExtension implements BeforeAllCallback {

    private final Logger logger = LoggerFactory.getLogger(TestRabbitMQExtension.class);

    private static boolean started = false;
    private static GenericContainer<?> rabbitMQContainer;

    public static GenericContainer<?> getRabbitMQContainer() {
        return rabbitMQContainer;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            rabbitMQContainer = new GenericContainer<>(DockerImageName.parse("rabbitmq:3.9.11"))
                    .withExposedPorts(5672);
            rabbitMQContainer.start();
            logger.info("Started RabbitMQ container at {}/{}",
                    rabbitMQContainer.getHost(),
                    rabbitMQContainer.getFirstMappedPort());
            started = true;
        }
    }
}
