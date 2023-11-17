package feedcontroller;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppSettings {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    private int updateInterval = 5;
    private int articlesPerFeed = 3;
    private boolean autoRefresh = false;
    private boolean aggregateFeeds = true;
    private static AppSettings instance = null;
    private List<String> subscribedFeedURLs;
    private List<String> readUIDs;

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

    }

    private static AppSettings readSettingsFile() throws IOException {
        return JSON.std.beanFrom(AppSettings.class, new File(SETTINGS_FILE_NAME));
    }

    private void writeSettingsFile() throws IOException {
        JSON.std.write(this, new File(SETTINGS_FILE_NAME));
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

    // only used for serialization
    public void setSubscribedFeedURLs(List<String> subscribedFeedURLs) {
        this.subscribedFeedURLs = subscribedFeedURLs;
    }

    // only used for serialization
    public void setReadUIDs(List<String> readUIDs) {
        this.readUIDs = readUIDs;
    }

    // only used for serialization
    public List<String> getReadUIDs() {
        return this.readUIDs;
    }

    public void addReadUID(String uid) {
        this.readUIDs.add(uid);
    }

    public boolean hasReadUID(String uid) {
        return readUIDs.contains(uid);
    }

    public void setDefaults() {
        updateInterval = 5;
        articlesPerFeed = 3;
        autoRefresh = false;
        aggregateFeeds = true;
        subscribedFeedURLs = new ArrayList<>();
        readUIDs = new ArrayList<>();
    }

    public void save() {
        try {
            writeSettingsFile();
        } catch (IOException ignored) {
            // just give up for now
        }
    }


}
