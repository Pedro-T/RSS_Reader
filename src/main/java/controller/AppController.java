package controller;

import feedmodel.Article;
import feedmodel.Feed;
import feedmodel.FeedManager;
import ui.NotificationHandler;
import ui.UserInterface;
import org.dom4j.*;

import java.awt.*;
import java.io.IOException;
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
 * @version 2023-11-12
 * Overall application controller
 */

public class AppController {

    private final Logger logger = Logger.getLogger("app");
    private final AppSettings settings = AppSettings.getInstance();

    private final UserInterface ui;
    private final FeedManager manager;

    public AppController() {
        manager = new FeedManager();
        ui = new UserInterface(this);
    }

    public void start() {
        manager.addObserver(new NotificationHandler());
        if (settings.getSubscribedFeedURLs().size() != 0) {
            for (String url : settings.getSubscribedFeedURLs()) {
                logger.log(Level.INFO, url);
                requestLoadFeed(url);
            }
        }
        setAutoRefresh();
        ui.setAggregateDisplayMode(settings.isAggregateFeeds());
        ui.show();
    }

    private void setAutoRefresh() {
        ScheduledExecutorService refreshService = Executors.newSingleThreadScheduledExecutor();
        refreshService.scheduleAtFixedRate(this::refreshAll, settings.getUpdateInterval(), settings.getUpdateInterval(), TimeUnit.MINUTES);
    }

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

    public void refreshAll() {
        manager.clearAll();
        for (String feedURL : settings.getSubscribedFeedURLs()) {
            requestLoadFeed(feedURL);
        }
        ui.updateArticleList(manager.getCurrentArticles(settings.getArticlesPerFeed()));
    }

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

    public void removeFeedByURL(String feedURL) {
        manager.remove(feedURL);
        settings.removeSubURL(feedURL);
        settings.save();
        logger.log(Level.INFO, String.format("Current feeds: %s", manager.getFeeds()));
        ui.updateArticleList(getCurrentArticles());
    }

    public void requestRemoveArticle(Article article) {
        settings.addReadUID(article.getUniqueID());
        manager.removeArticle(article);
        settings.save();
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

    public void setSettings(int updateInterval, int articleCount, boolean autoRefresh, boolean aggregate) {
        settings.setUpdateInterval(updateInterval);
        settings.setArticlesPerFeed(articleCount);
        settings.setAutoRefresh(autoRefresh);
        settings.setAggregateFeeds(aggregate);
        settings.save();
        ui.setAggregateDisplayMode(aggregate);
        ui.updateArticleList(manager.getCurrentArticles(articleCount));
        logger.log(Level.INFO, String.valueOf(settings.getUpdateInterval()));
        logger.log(Level.INFO, String.valueOf(settings.getArticlesPerFeed()));
        logger.log(Level.INFO, settings.toString());
    }
}
