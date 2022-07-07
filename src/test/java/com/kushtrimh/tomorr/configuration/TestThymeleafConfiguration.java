package com.kushtrimh.tomorr.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Kushtrim Hajrizi
 */
@TestConfiguration
@Import({ThymeleafConfiguration.class})
public class TestThymeleafConfiguration {
}
