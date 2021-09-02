package com.kushtrimh.tomorr.task.manager;

import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = { @Tag("redis"), @Tag("integration") })
@ContextConfiguration(classes = {TestRedisConfiguration.class})
@ExtendWith({SpringExtension.class})
public class ArtistSyncTaskManagerIntegrationTest {
}
