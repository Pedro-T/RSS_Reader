package feedmodel;

import controller.AppSettings;

import java.util.List;
import java.util.ArrayList;

/**
 * Feed.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-16
 * Represents an individual RSS feed and contains the articles from the feed
 */

public class Feed {

    private final List<Article> articles = new ArrayList<>();
    private final List<String> readUIDs = new ArrayList<>();
    private final String name;
    private final String feedURL;

    public Feed(String name, String feedURL) {
        this.name = name;
        this.feedURL = feedURL;
    }

    public String getName() {
        return name;
    }

    public String getFeedURL() {
        return feedURL;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void addArticle(Article article) {
        if (!AppSettings.getInstance().hasReadUID(article.getUniqueID())) {
            this.articles.add(article);
        }
    }

    public void remove(Article article) {
        this.readUIDs.add(article.getUniqueID());
        this.articles.remove(article);
    }

}
