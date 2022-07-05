package com.kushtrimh.tomorr.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {
    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MailProperties that = (MailProperties) o;
        return Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from);
    }

    @Override
    public String toString() {
        return "MailProperties{" +
                "from='" + from + '\'' +
                '}';
    }
}
