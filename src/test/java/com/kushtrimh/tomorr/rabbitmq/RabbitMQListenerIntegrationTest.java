package com.kushtrimh.tomorr.rabbitmq;

import com.kushtrimh.tomorr.configuration.TestRabbitMQConfiguration;
import com.kushtrimh.tomorr.extension.TestRabbitMQExtension;
import com.kushtrimh.tomorr.task.Task;
import com.kushtrimh.tomorr.task.TaskType;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("rabbitmq"), @Tag("integration")})
@ContextConfiguration(classes = {TestRabbitMQConfiguration.class})
@ExtendWith({SpringExtension.class, TestRabbitMQExtension.class})
public class RabbitMQListenerIntegrationTest {

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Test
    public void convertSendAndReceive_WhenSendingArtistUsingRabbitMQ_SendAndReceiveSuccessfully() {
        Task<ArtistTaskData> artistTask = new Task<>(ArtistTaskData.fromArtistId("artistId", TaskType.SYNC));
        Object response = testRabbitTemplate.convertSendAndReceive("artistSync", artistTask);
        assertEquals(artistTask.getData().getArtistId(), response);
    }
}
