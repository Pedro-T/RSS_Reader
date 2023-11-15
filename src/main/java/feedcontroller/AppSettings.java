package feedcontroller;

import com.fasterxml.jackson.jr.ob.JSON;

import java.io.File;
import java.io.IOException;

public class AppSettings {

    private static final String SETTINGS_FILE_NAME = "settings.json";
    private int updateInterval = 5;

    private int articlesPerFeed = 3;
    private boolean autoRefresh = false;
    private boolean aggregateFeeds = true;
    private static AppSettings instance = new AppSettings();

    public static AppSettings getInstance() {
        return instance;
    }

    private AppSettings() {

    }

    public void readSettingsFile() throws IOException {
        instance = JSON.std.beanFrom(AppSettings.class, new File(SETTINGS_FILE_NAME));
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

    public void setDefaults() {
        updateInterval = 5;
        articlesPerFeed = 3;
        autoRefresh = false;
        aggregateFeeds = true;
    }

    public void save() {
        try {
            writeSettingsFile();
        } catch (IOException ignored) {

        }
    }


}
