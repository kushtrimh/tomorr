package com.kushtrimh.tomorr.generator;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * @author Kushtrim Hajrizi
 */
public class IDGeneratorTest {

    @Test
    public void generate_WhenDefaultLengthIs32_ReturnIdWithLength32() {
        IDGenerator idGenerator = new IDGenerator(32);
        String generatedId = idGenerator.generate();
        assertEquals(32, generatedId.length());
    }

    @Test
    public void generate_WhenDefaultLengthIs64_ReturnIdWithLength64() {
        IDGenerator idGenerator = new IDGenerator(64);
        String generatedId = idGenerator.generate();
        assertEquals(64, generatedId.length());
    }
}
