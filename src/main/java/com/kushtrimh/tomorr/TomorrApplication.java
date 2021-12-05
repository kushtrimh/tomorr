package com.kushtrimh.tomorr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class TomorrApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TomorrApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}
