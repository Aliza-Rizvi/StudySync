package com.studysync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Starting point of StudySync.
 * Right-click this file and choose Run to start the web server on http://localhost:8080
 *
 * @EnableScheduling lets Mahtab's ReminderScheduler run automatically every minute.
 */
@SpringBootApplication
@EnableScheduling
public class StudysyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudysyncApplication.class, args);
    }
}