package feedmodel;

import java.util.List;
import java.util.ArrayList;

/**
 * Feed.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-29
 * Represents an individual RSS feed and contains the articles from the feed
 */

public class Feed {

    private List<Article> articles;
    private List<String> readUIDs;
    private String name;
    private String feedURL;

    public Feed(String name, String feedURL) {
        this.name = name;
        this.feedURL = feedURL;
        this.articles = new ArrayList<>();
        this.readUIDs = new ArrayList<>();
    }

    // not used explicitly - default only for jackson-jr
    public Feed() {
        this.name = "";
        this.feedURL = "";
        this.articles = new ArrayList<>();
        this.readUIDs = new ArrayList<>();
    }

    public boolean containsUID(String uid) {
        for (Article a : articles) {
            if (a.getUniqueID().equals(uid)) {
                return true;
            }
        }
        return readUIDs.contains(uid);
    }

    // several "unused" methods here are for use by jackson-jr
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFeedURL() {
        return feedURL;
    }

    public void setFeedURL(String feedURL) {
        this.feedURL = feedURL;
    }

    public List<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<String> getReadUIDs() {
        return this.readUIDs;
    }

    public void setReadUIDs(List<String> readUIDs) {
        this.readUIDs = readUIDs;
        System.out.println(readUIDs);
    }

    public void addArticle(Article article) {
        if (!this.readUIDs.contains(article.getUniqueID())) {
            this.articles.add(article);
        }
    }

    public void remove(Article article) {
        if (articles.contains(article)) {
            readUIDs.add(article.getUniqueID());
            articles.remove(article);
        }
    }
}
