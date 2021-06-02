package com.kushtrimh.tomorr.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RedisProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
