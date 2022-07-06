package com.kushtrimh.tomorr.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;

/**
 * @author Kushtrim Hajrizi
 */
public class TestMailExtension implements BeforeAllCallback {

    private final Logger logger = LoggerFactory.getLogger(TestMailExtension.class);

    private static boolean started = false;
    private static GenericContainer<?> mailContainer;

    public static GenericContainer<?> getMailContainer() {
        return mailContainer;
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!started) {
            mailContainer = new GenericContainer<>("mailhog/mailhog:v1.0.1")
                    .withExposedPorts(1025, 8025);
            mailContainer.start();
            logger.info("Started mail container at {}/{}",
                    mailContainer.getHost(),
                    mailContainer.getFirstMappedPort());
            started = true;
        }
    }
}
