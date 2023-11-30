package controller;

import com.fasterxml.jackson.jr.ob.JSON;
import feedmodel.Article;
import feedmodel.Feed;
import feedmodel.FeedManager;
import ui.NotificationHandler;
import ui.UserInterface;
import org.dom4j.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AppController.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Overall application controller
 * MVC - Controller
 */

public class AppController {

    private final Logger logger = Logger.getLogger("app");
    private final AppSettings settings = AppSettings.getInstance();

    private final UserInterface ui;
    private final FeedManager manager;
    private boolean isOfflineMode = false;
    private static final String PING_ADDRESS = "8.8.8.8";

    public AppController() {
        manager = new FeedManager();
        ui = new UserInterface(this);
    }

    /**
     * Basic setup. Desktop notifications, check connectivity, load cache if needed or load feeds, update and show UI
     */
    public void start() {
        manager.addObserver(new NotificationHandler());
        if (!checkConnectivity()) {
            isOfflineMode = true;
            ui.showError("No internet connectivity\nLoading cached articles");
            try {
                manager.setFeeds(CacheHandler.readCache());
                logger.log(Level.INFO, String.valueOf(manager.getFeeds().size()));
                ui.updateArticleList(getCurrentArticles());
            } catch (IOException e) {
                e.printStackTrace();
                ui.showError("Error loading feeds. Cache may be unavailable.");
            }
        } else {
            try {
                manager.setFeeds(CacheHandler.readCache());
            } catch (IOException ignored) {
                logger.log(Level.WARNING, "Error loading cache for online mode");
                // in this instance just ignore the inability to load cache and proceed with online retrieval
            }
            if (settings.getSubscribedFeedURLs().size() != 0) {
                for (String url : settings.getSubscribedFeedURLs()) {
                    logger.log(Level.INFO, String.format("Requesting feed %s", url));
                    requestLoadFeed(url);
                }
            }
            setAutoRefresh();
        }
        ui.setAggregateDisplayMode(settings.isAggregateFeeds());
        ui.show();
    }

    /**
     * Save the cache file if there are any loaded feeds, to use in offline mode if needed
     */
    public void exitSaveCache() {
        if (manager.getFeeds().size() > 0) {
            try {
                CacheHandler.writeCache(manager.getFeeds());
            } catch (IOException e) {
                ui.showError("Unable to save cache.");
            }
        }
    }

    /**
     * Set up refresh timer in a separate thread based on user setting interval
     */
    private void setAutoRefresh() {
        if (!isOfflineMode && settings.isAutoRefresh()) {
            ScheduledExecutorService refreshService = Executors.newSingleThreadScheduledExecutor();
            refreshService.scheduleAtFixedRate(this::refreshAll, settings.getUpdateInterval(), settings.getUpdateInterval(), TimeUnit.MINUTES);
        }
    }

    /**
     * Create a table of feed names and URLs - this is used to populate the subscription management table in the popup
     * @return 2d array of feed names and URLs
     */
    public String[][] getFeedNamesAndURLs() {
        List<Feed> feeds = manager.getFeeds();
        String[][] feedNameURLMatrix = new String[feeds.size()][2];
        for (int i = 0; i < feeds.size(); i++) {
            feedNameURLMatrix[i][0] = feeds.get(i).getName();
            feedNameURLMatrix[i][1] = feeds.get(i).getFeedURL();
        }
        return feedNameURLMatrix;
    }

    public void subscribeToFeed(String inputURLString) {
        settings.addSubURL(inputURLString);
        settings.save();
        requestLoadFeed(inputURLString);
    }

    public Map<String, List<Article>> getCurrentArticles() {
        return manager.getCurrentArticles(settings.getArticlesPerFeed());
    }

    /**
     * Refresh all feeds and update UI
     */
    public void refreshAll() {
        if (!isOfflineMode) {
            for (String feedURL : settings.getSubscribedFeedURLs()) {
                requestLoadFeed(feedURL);
            }
            ui.updateArticleList(manager.getCurrentArticles(settings.getArticlesPerFeed()));
        }
    }

    /**
     * Ask the feed manager to load a feed based on a given URL, then update the UI. If the feed is invalid or
     * malformed, inform the user via error popup and drop the feed subscription
     * @param inputURLString URL to attempt to load
     */
    public void requestLoadFeed(String inputURLString) {
        try {
            manager.addFeed(inputURLString);
            ui.updateArticleList(getCurrentArticles());
        } catch(IOException | DocumentException e) {
            logger.log(Level.WARNING, e.getMessage());
            ui.showError(String.format("Malformed URL or invalid feed.\nDeleted subscription for:\n%s", inputURLString));
            settings.removeSubURL(inputURLString);
            settings.save();
        }
    }

    /**
     * Request removal of a feed with the given URL from both the active feed manager list and the app
     * settings, then update the UI
     * @param feedURL URL to attempt to remove
     */
    public void removeFeedByURL(String feedURL) {
        manager.remove(feedURL);
        settings.removeSubURL(feedURL);
        settings.save();
        logger.log(Level.INFO, String.format("Current feeds: %s", manager.getFeeds()));
        ui.updateArticleList(getCurrentArticles());
    }

    /**
     * Request removal of a specific article, then update the UI
     * @param article article object to remove
     */
    public void requestRemoveArticle(Article article) {
        manager.removeArticle(article);
        logger.log(Level.INFO, String.format("Removed %s\n", article.getTitle()));
        ui.updateArticleList(getCurrentArticles());
    }

    /**
     * Attempt to open the system default web browser application to the URL of the article in question
     * @param url String url to open
     */
    public void openInBrowser(String url) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            }
        } catch (URISyntaxException | IOException e) {
            ui.showError("Unable to open browser or action not supported.");
            e.printStackTrace();
        }
    }

    /**
     * Update all settings attributes and save to file, then update the UI's feed display mode and refresh
     * @param updateInterval
     * @param articleCount
     * @param autoRefresh
     * @param aggregate
     */
    public void setSettings(int updateInterval, int articleCount, boolean autoRefresh, boolean aggregate) {
        settings.setUpdateInterval(updateInterval);
        settings.setArticlesPerFeed(articleCount);
        settings.setAutoRefresh(autoRefresh);
        settings.setAggregateFeeds(aggregate);
        settings.save();
        ui.setAggregateDisplayMode(aggregate);
        ui.updateArticleList(manager.getCurrentArticles(articleCount));
    }

    /**
     * Simple connectivity check, adapted from https://stackoverflow.com/questions/11506321/how-to-ping-an-ip-address
     * Using a hard-coded public Google DNS server as a destination that's 99.99% likely to be available
     * @return boolean value representing whether the user probably has internet connectivity
     */
    private boolean checkConnectivity() {
        try {
            return InetAddress.getByName(PING_ADDRESS).isReachable(3000);
        } catch (IOException e) {
            return false;
        }
    }
}


/**
 * Helper class just to save/load feed cache
 */
class CacheHandler {

    static final String FEED_CACHE_PATH = System.getProperty("user.home") + File.separator + "RSS_Reader" + File.separator + "feedcache.json";
    public static void writeCache(List<Feed> feeds) throws IOException {
        JSON.std.write(feeds, new File(FEED_CACHE_PATH));
    }

    public static List<Feed> readCache() throws IOException {
        return JSON.std.listOfFrom(Feed.class, new File(FEED_CACHE_PATH));
    }

}
