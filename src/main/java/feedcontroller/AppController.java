package feedcontroller;

import feedmodel.Article;
import feedmodel.Feed;
import feedview.UserInterface;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AppController.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-12
 * Overall application controller
 */

public class AppController {

    private final Logger logger = Logger.getLogger("Controller");
    private final List<Feed> feeds = new ArrayList<>();
    private final AppSettings settings = AppSettings.getInstance();

    private final UserInterface ui;

    public AppController() {
        ui = new UserInterface(this);
    }

    public void start() {
        if (settings.getSubscribedFeedURLs().size() != 0) {
            for (String url : settings.getSubscribedFeedURLs()) {
                logger.log(Level.INFO, url);
                addFeed(url);
            }
        }
        ui.show();
    }


    public String[][] getFeedNamesAndURLs() {
        String[][] list = new String[feeds.size()][2];
        for (int i = 0; i < feeds.size(); i++) {
            list[i][0] = feeds.get(i).getName();
            list[i][1] = feeds.get(i).getFeedURL();
        }
        return list;
    }

    public void subscribeToFeed(String inputURLString) {
        settings.addSubURL(inputURLString);
        settings.save();
        addFeed(inputURLString);
    }

    /**
     * Attempt to add a new RSS feed to the list and populate its articles
     * @param inputURLString url of the feed
     */
    public void addFeed(String inputURLString) {
        try {
            URL url = new URL(inputURLString);
            Document feedDoc = getFeed(url);
            Feed feed = new Feed(feedDoc.valueOf(".//channel/title"), url.toString());
            List<Node> articles = feedDoc.selectNodes(".//channel/item");
            if (!articles.isEmpty()) {
                for (Node s : articles) {
                    Article.ArticleBuilder builder = new Article.ArticleBuilder(
                            feed.getName(),
                            s.selectSingleNode("title").getText(),
                            s.selectSingleNode("link").getText());
                    Node contentNode = s.selectSingleNode("description");
                    if (contentNode != null) {
                        String content = contentNode.getText();
                        Pattern pattern = Pattern.compile("<img src=\"(.*?)\">(.*)");
                        Matcher matcher = pattern.matcher(content);
                        if (matcher.find()) {
                            builder.setImageURL(new URL(matcher.group(1)));
                            builder.setSummary(matcher.group(2));
                        } else {
                            builder.setSummary(content);
                        }
                    }
                    Node uidNode = s.selectSingleNode("guid");
                    if (uidNode != null) {
                        builder.setUniqueID(uidNode.getText());
                    } else {
                        builder.generateUniqueID();
                    }
                    feed.addArticle(builder.build());
                }
            }
            feeds.add(feed);
            ui.updateArticleList(getCurrentArticles());

        } catch(IOException | DocumentException e) {
            ui.showError("Malformed URL or invalid feed. Please double-check input");
            settings.removeSubURL(inputURLString);
            settings.save();
            throw new RuntimeException(e);
        }
    }

    public void removeFeedByURL(String feedURL) {
        for (Feed f : feeds) {
            if (f.getFeedURL().equals(feedURL)) {
                feeds.remove(f);
                logger.log(Level.INFO, String.format("Removed feed %s with URL %s", f.getName(), f.getFeedURL()));
                break;
            }
        }
        settings.removeSubURL(feedURL);
        logger.log(Level.INFO, String.format("Current feeds: %s", feeds));
        ui.updateArticleList(getCurrentArticles());
    }

    private Document getFeed(URL url) throws IOException, DocumentException {
        return new SAXReader().read(url);
    }

    public void requestRemoveArticle(Article article) {
        for (Feed f : feeds) {
            f.remove(article);
        }
        settings.addReadUID(article.getUniqueID());
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

    private Map<String, List<Article>> getCurrentArticles()  {
        HashMap<String, List<Article>> articles = new HashMap<>();
        for (Feed f : feeds) {
            articles.put(f.getName(), new ArrayList<Article>());
            logger.log(Level.INFO, String.format("Feed %s has %d articles.", f.getName(), f.getArticles().size()));
            for (int i = 0; i < settings.getArticlesPerFeed(); i++) {
                if (f.getArticles().size() <= i) {
                    break;
                }
                articles.get(f.getName()).add(f.getArticles().get(i));
            }
        }
        return articles;
    }

    private List<Article> getCurrentArticleList()  {
        List<Article> articles = new ArrayList<>();
        for (Feed f : feeds) {
            logger.log(Level.INFO, String.format("Feed %s has %d articles.", f.getName(), f.getArticles().size()));
            for (int i = 0; i < settings.getArticlesPerFeed(); i++) {
                if (f.getArticles().size() <= i) {
                    break;
                }
                articles.add(f.getArticles().get(i));
            }
        }
        return articles;
    }

    public void setSettings(int updateInterval, int articleCount, boolean autoRefresh, boolean aggregate) {
        settings.setUpdateInterval(updateInterval);
        settings.setArticlesPerFeed(articleCount);
        settings.setAutoRefresh(autoRefresh);
        settings.setAggregateFeeds(aggregate);
        settings.save();
        logger.log(Level.INFO, String.valueOf(settings.getUpdateInterval()));
        logger.log(Level.INFO, String.valueOf(settings.getArticlesPerFeed()));
        logger.log(Level.INFO, settings.toString());
    }
}
