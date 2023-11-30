package controller;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AppSettings.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * This class is responsible for holding, updating, saving/loading the application settings
 * Assignment note - Singleton usage
 */

public class AppSettings {

    private static final String SETTINGS_FILE_PATH = System.getProperty("user.home") + File.separator + "RSS_Reader" + File.separator + "settings.json";
    private int updateInterval = 5;
    private int articlesPerFeed = 3;
    private boolean autoRefresh = false;
    private boolean aggregateFeeds = true;
    private static AppSettings instance = null;
    private List<String> subscribedFeedURLs;

    // Assignment note - Singleton
    public static synchronized AppSettings getInstance() {
        if (instance == null) {
            try {
                instance = readSettingsFile();
            } catch (IOException e) {
                instance = new AppSettings();
                instance.setDefaults();
            }
        }
        return instance;
    }

    private AppSettings() {
        ensureDirectoryExists();
    }

    private static AppSettings readSettingsFile() throws IOException {
        return JSON.std.beanFrom(AppSettings.class, new File(SETTINGS_FILE_PATH));
    }

    private void ensureDirectoryExists() {
        File dir = new File(String.format("%s%sRSS_Reader", System.getProperty("user.home"), File.separator));
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private void writeSettingsFile() throws IOException {
        JSON.std.write(this, new File(SETTINGS_FILE_PATH));
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public int getArticlesPerFeed() {
        return articlesPerFeed;
    }

    public void setArticlesPerFeed(int articlesPerFeed) {
        this.articlesPerFeed = articlesPerFeed;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public boolean isAggregateFeeds() {
        return aggregateFeeds;
    }

    public void setAggregateFeeds(boolean aggregateFeeds) {
        this.aggregateFeeds = aggregateFeeds;
    }

    public void removeSubURL(String url) {
        subscribedFeedURLs.remove(url);
    }

    public void addSubURL(String url) {
        subscribedFeedURLs.add(url);
    }

    public List<String> getSubscribedFeedURLs() {
        return this.subscribedFeedURLs;
    }

    // following only used for serialization / deserialization
    public void setSubscribedFeedURLs(List<String> subscribedFeedURLs) {
        this.subscribedFeedURLs = subscribedFeedURLs;
    }

    public void setDefaults() {
        updateInterval = 5;
        articlesPerFeed = 3;
        autoRefresh = false;
        aggregateFeeds = true;
        subscribedFeedURLs = new ArrayList<>();
    }

    public void save() {
        try {
            writeSettingsFile();
        } catch (IOException ignored) {
            // just skip
        }
    }
}
