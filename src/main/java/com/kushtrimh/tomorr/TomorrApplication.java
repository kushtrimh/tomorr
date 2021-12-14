package com.kushtrimh.tomorr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        XADataSourceAutoConfiguration.class,
        R2dbcAutoConfiguration.class,
        RabbitAutoConfiguration.class
})
public class TomorrApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TomorrApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}
