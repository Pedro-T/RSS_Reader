package feedmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Feed.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-14
 * Represents an individual RSS feed and contains the articles from the feed
 */

public class Feed {

    private final List<Article> articles = new ArrayList<>();
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
        this.articles.add(article);
    }

    public void remove(Article article) {
        this.articles.remove(article);
    }

}
