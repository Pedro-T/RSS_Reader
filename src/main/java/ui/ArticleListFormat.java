package ui;

import controller.AppController;
import feedmodel.Article;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * ArticleListFormat.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-18
 * Provides two alternate methods for arranging ArticleDisplayPanel instances - aggregate and by feed
 * Assignment Note: Strategy Pattern - see FeedDisplay for usage of these classes
 */

public abstract class ArticleListFormat {

    protected AppController controller;

    public ArticleListFormat(AppController controller) {
        this.controller = controller;
    }

    abstract List<JPanel> create(Map<String, List<Article>> articles);
}

class AggregateArticlePanel extends ArticleListFormat {

    public AggregateArticlePanel(AppController controller) {
        super(controller);
    }

    /**
     * This one is a simple list. Flatten the list of lists (since this is a map of feed names to article lists)
     * and then create a simple article display panel for each article
     * @param articles map of feed names and articles
     * @return list of JPanels
     */
    @Override
    public List<JPanel> create(Map<String, List<Article>> articles) {
        List<Article> articleFlatList = articles.values().stream().flatMap(List::stream).toList();
        return articleFlatList.stream().map(article -> new ArticleDisplayPanel(controller, article).getPanel()).toList();
    }
}

class ArticleByFeedPanel extends ArticleListFormat {

    public ArticleByFeedPanel(AppController controller) {
        super(controller);
    }

    /**
     * This one creates a subpanel for each feed, titled with the feed name and containing that feed's articles
     * @param articles map of feed names and articles
     * @return list of JPanels
     */
    @Override
    public List<JPanel> create(Map<String, List<Article>> articles) {
        return articles.entrySet().stream().map((entry) -> createFeedGroupPanel(controller, entry.getKey(), entry.getValue())).toList();
    }

    /**
     * Create and return an individual feed panel which contains several ArticleDisplayPanel instances
     * @param controller ref to AppController for button functionality to pass to the ArticleDisplayPanel
     * @param feedName feed name to display at top of panel
     * @param articles article list to pull from
     * @return JPanel representing the subsection of article list
     */
    private JPanel createFeedGroupPanel(AppController controller, String feedName, List<Article> articles) {
        JPanel feedGroupPanel = new JPanel();
        EventQueue.invokeLater(() -> {
            feedGroupPanel.setLayout(new BoxLayout(feedGroupPanel, BoxLayout.PAGE_AXIS));
            feedGroupPanel.setBorder(BorderFactory.createEtchedBorder());
            JLabel feedNameLabel = new JLabel(String.format("<html><b>%S</b></html>", feedName), SwingConstants.LEFT);
            feedGroupPanel.add(feedNameLabel);
            articles.forEach(article -> feedGroupPanel.add(new ArticleDisplayPanel(controller, article).getPanel()));
        });
        return feedGroupPanel;
    }
}
