package com.studysync.config;

/**
 * SINGLETON PATTERN: only ONE ConfigurationManager object exists in the whole app.
 * Everyone gets it with ConfigurationManager.getInstance().
 *
 * 1. The constructor is private, so nobody can write "new ConfigurationManager()".
 * 2. The single object is kept in the static field "instance".
 * 3. getInstance() creates it the first time and returns the same object every time after.
 */
public final class ConfigurationManager {

    private static volatile ConfigurationManager instance;

    private final String appName;
    private final int maxGroupSize;
    private final int reminderHoursBefore;
    private final String defaultMatchingStrategy;

    private ConfigurationManager() {
        this.appName = "StudySync";
        this.maxGroupSize = 6;
        this.reminderHoursBefore = 24;
        this.defaultMatchingStrategy = "availability";
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {                                  // first check (fast)
            synchronized (ConfigurationManager.class) {          // lock, in case two users come at the same time
                if (instance == null) {                          // second check (safe)
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    /** Biggest allowed study group */
    public int getMaxGroupSize() {
        return maxGroupSize;
    }

    /** Reminders go out this many hours before a session */
    public int getReminderHoursBefore() {
        return reminderHoursBefore;
    }

    /** Strategy used when the student does not choose one */
    public String getDefaultMatchingStrategy() {
        return defaultMatchingStrategy;
    }
}