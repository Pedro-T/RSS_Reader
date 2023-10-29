package feedmodel;

import java.net.URL;

public class Article {

    private final String feedName;
    private final String title;
    private final String summary;
    private final String url;
    private URL imageURL;

    public String getFeedName() {
        return feedName;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    private Article(ArticleBuilder builder) {
        this.feedName = builder.feedName;
        this.title = builder.title;
        this.url = builder.url;
        this.summary = builder.summary != null ? builder.summary : "No Summary Available";
        this.imageURL = builder.imageURL;
    }

    public boolean hasImageURL() {
        return imageURL != null;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public String getURL() {
        return url;
    }

    public static class ArticleBuilder {
        private String feedName;
        private String title;
        private String summary;
        private String url;

        private int uniqueID = 0;
        private URL imageURL = null;

        public ArticleBuilder(String feedName, String title, String url) {
            this.feedName = feedName;
            this.title = title;
            this.url = url;
        }

        public ArticleBuilder setUnqiueID(int unqiueID) {
            this.uniqueID = unqiueID;
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
