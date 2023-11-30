package feedmodel;

import java.net.URL;

/**
 * Article.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Individual article, with all attributes of an article used for display in the application
 */

public class Article {

    private String feedName;
    private String title;
    private String summary;
    private String url;
    private URL imageURL;
    private String uniqueID;

    public String getFeedName() {
        return feedName;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public String getURL() {
        return url;
    }

    // These are only used for serialization/deserialization
    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }
    public Article() {

    }

    private Article(ArticleBuilder builder) {
        this.feedName = builder.feedName;
        this.title = builder.title;
        this.url = builder.url;
        this.summary = builder.summary != null ? builder.summary : "No Summary Available";
        this.imageURL = builder.imageURL;
        this.uniqueID = builder.uniqueID;
    }

    public boolean hasImageURL() {
        return imageURL != null;
    }

    /**
     * Assignment note - Builder pattern
     * Provides builder functionality for Articles
     */
    public static class ArticleBuilder {
        private String feedName;
        private String title;
        private String summary;
        private String url;

        private String uniqueID;
        private URL imageURL = null;

        public ArticleBuilder(String feedName, String title, String url) {
            this.feedName = feedName;
            this.title = title;
            this.url = url;
        }

        public ArticleBuilder setUniqueID(String uniqueID) {
            this.uniqueID = uniqueID;
            return this;
        }

        public ArticleBuilder generateUniqueID() {
            // TODO
            return this;
        }

        public ArticleBuilder setImageURL(URL url) {
            this.imageURL = url;
            return this;
        }

        public ArticleBuilder setSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }

}
