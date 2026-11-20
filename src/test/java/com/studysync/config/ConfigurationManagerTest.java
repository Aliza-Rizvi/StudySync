package com.studysync.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for the Singleton pattern */
class ConfigurationManagerTest {

    @Test
    void getInstanceAlwaysReturnsTheSameObject() {
        ConfigurationManager first = ConfigurationManager.getInstance();
        ConfigurationManager second = ConfigurationManager.getInstance();
        assertSame(first, second);
    }

    @Test
    void hasCorrectDefaultSettings() {
        ConfigurationManager config = ConfigurationManager.getInstance();
        assertEquals("StudySync", config.getAppName());
        assertEquals(6, config.getMaxGroupSize());
        assertEquals(24, config.getReminderHoursBefore());
        assertEquals("availability", config.getDefaultMatchingStrategy());
    }
}