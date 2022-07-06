package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.extension.TestMailExtension;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kushtrim Hajrizi
 */
@TestConfiguration
public class TestMailConfiguration {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("localhost");
        javaMailSender.setPort(TestMailExtension.getMailContainer().getFirstMappedPort());
        javaMailSender.setUsername("");
        javaMailSender.setPassword("");
        return javaMailSender;
    }

    @Bean
    public RestTemplate mailRestTemplate() {
        return new RestTemplateBuilder()
                .rootUri("http://localhost:" + TestMailExtension.getMailContainer().getMappedPort(8025))
                .build();
    }
}
