package com.kushtrimh.tomorr.mail;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kushtrimh.tomorr.configuration.TestMailConfiguration;
import com.kushtrimh.tomorr.extension.TestMailExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags(value = {@Tag("mail"), @Tag("integration")})
@ContextConfiguration(classes = {TestMailConfiguration.class})
@ExtendWith({SpringExtension.class, TestMailExtension.class})
public class DefaultMailServiceIntegrationTest {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RestTemplate mailRestTemplate;

    private DefaultMailService service;

    @BeforeEach
    public void init() {
        service = new DefaultMailService(javaMailSender);
    }

    @Test
    public void send_WhenEmailDataIsValid_SendEmailSuccessfully() throws MailException, JsonProcessingException {
        var from = "noreply@tomorrlocal.com";
        var to = "user1@tomorrlocal.com";
        var subject = "subject-tomorr";
        var content = "content here";
        service.send(from, subject, content, to);
        service.send(from, subject, content, to);

        var response = mailRestTemplate.getForObject("/api/v2/messages", String.class);
        ObjectMapper mapper = new ObjectMapper();
        var node = mapper.readTree(response);

        var item = node.get("items").get(0);

        var returnedContentObject = item.get("Content");
        var returnedContent = returnedContentObject.get("Body").asText();

        var returnedHeadersObject = returnedContentObject.get("Headers");

        var returnedSubject = returnedHeadersObject.get("Subject").get(0).asText();
        var returnedFrom = returnedHeadersObject.get("From").get(0).asText();
        var returnedTo = returnedHeadersObject.get("To").get(0).asText();

        assertAll(
                () -> assertEquals(2, node.get("items").size()),
                () -> assertEquals(subject, returnedSubject),
                () -> assertEquals(from, returnedFrom),
                () -> assertEquals(to, returnedTo),
                () -> assertEquals(content, returnedContent)
        );
    }
}