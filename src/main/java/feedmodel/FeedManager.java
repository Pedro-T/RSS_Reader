package feedmodel;

import controller.NotificationSubject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import ui.NotificationObserver;

import java.io.IOException;
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
 * FeedManager.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-30
 * Central point for managing feed objects, retrieving data
 * MVC - Model (along with Article and Feed)
 */

public class FeedManager implements NotificationSubject {
    private List<Feed> feeds = new ArrayList<>();
    private final Logger logger = Logger.getLogger("app");

    private final List<NotificationObserver> observers = new ArrayList<>();

    public FeedManager() {

    }

    public List<Feed> getFeeds() {
        return this.feeds;
    }
    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public void removeArticle(Article article) {
        for (Feed f : feeds) {
            f.remove(article);
        }
    }

    /**
     * Remove a feed by given URL
     * @param feedURL String url to check for
     */
    public void remove(String feedURL) {
        for (Feed f : feeds) {
            if (f.getFeedURL().equals(feedURL)) {
                feeds.remove(f);
                break;
            }
        }
    }

    /**
     * Create a map of feed name to article list, used to populate the UI
     * @param perFeed number of articles per feed to retrieve
     * @return map of feed names and lists of Articles
     */
    public Map<String, List<Article>> getCurrentArticles(int perFeed)  {
        HashMap<String, List<Article>> articles = new HashMap<>();
        for (Feed f : feeds) {
            articles.put(f.getName(), new ArrayList<>());
            logger.log(Level.INFO, String.format("Feed %s has %d articles.", f.getName(), f.getArticles().size()));
            for (int i = 0; i < perFeed; i++) {
                if (f.getArticles().size() <= i) {
                    break;
                }
                articles.get(f.getName()).add(f.getArticles().get(i));
            }
        }
        return articles;
    }

    /**
     * Helper for addFeed to check if the url is already tied to a loaded feed
     * @param url String url to check for
     * @return the feed tied to this url, or null if no feed is available
     */
    private Feed getFeedByURL(String url) {
        for (Feed f : feeds) {
            logger.log(Level.INFO, String.format("Checking for feed %s", f.getFeedURL()));
            if (f.getFeedURL().equals(url)) {
                logger.log(Level.INFO, "Feed already exists");
                return f;
            } else {
                System.out.printf("%s is not equal to %s", url, f.getFeedURL());
            }
        }
        return null;
    }

    /**
     * Attempt to add a new RSS feed to the list and populate its articles. If the feed already exists, just add new articles
     * @param inputURLString url of the feed
     */
    public void addFeed(String inputURLString) throws DocumentException, IOException {
        logger.log(Level.INFO, String.format("Retrieving feed %s", inputURLString));
        Feed feed = getFeedByURL(inputURLString);
        URL url = new URL(inputURLString);
        Document feedDoc = getFeedData(url);
        if (feed == null) {
            feed = new Feed(feedDoc.valueOf(".//channel/title"), url.toString());
            feeds.add(feed);
        }
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
                        Element media = (Element) s.selectSingleNode(".//media:content[@medium=\"image\"]");
                        if (media != null) {
                            builder.setImageURL(new URL(media.attributeValue("url")));
                        }
                    }
                }
                Node uidNode = s.selectSingleNode("guid");
                if (uidNode != null && feed.containsUID(uidNode.getText())) {
                    break;
                }
                if (uidNode != null) {
                    logger.log(Level.INFO, String.format("No match for %s among %s", uidNode.getText(), feed.getReadUIDs()));
                    builder.setUniqueID(uidNode.getText());
                    feed.addArticle(builder.build());
                }
            }
        }
        notifyUpdates(String.format("Feed %s has %d unread articles", feed.getName(), feed.getArticles().size()));
    }

    private Document getFeedData(URL url) throws DocumentException {
        return new SAXReader().read(url);
    }

    // Observer pattern bits
    @Override
    public void addObserver(NotificationObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(NotificationObserver o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyUpdates(String s) {
        for (NotificationObserver o : observers) {
            o.update(s);
        }
    }
}
