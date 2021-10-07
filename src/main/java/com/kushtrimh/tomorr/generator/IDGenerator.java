package com.kushtrimh.tomorr.generator;

import org.springframework.stereotype.Component;

/**
 * @author Kushtrim Hajrizi
 */
@Component
public class IDGenerator {

    private static final String DEFAULT_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String defaultSet;
    private int defaultLength;

    public IDGenerator() {
        this.defaultLength = 32;
        this.defaultSet = DEFAULT_SET;
    }

    public IDGenerator(int defaultLength) {
        this.defaultLength = defaultLength;
        this.defaultSet = DEFAULT_SET;
    }

    public IDGenerator(String defaultSet) {
        this.defaultLength = 32;
        this.defaultSet = defaultSet;
    }

    public IDGenerator(int defaultLength, String defaultSet) {
        this.defaultLength = defaultLength;
        this.defaultSet = defaultSet;
    }

    public String generate() {
        var idBuilder = new StringBuilder();
        var setLength = defaultSet.length();
        for (var i = 0; i < defaultLength; i++) {
            idBuilder.append(defaultSet.charAt((int) (Math.random() * setLength)));
        }
        return idBuilder.toString();
    }
}
