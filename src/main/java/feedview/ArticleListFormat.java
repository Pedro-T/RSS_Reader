package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public abstract class ArticleListFormat {

    AppController controller;

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
     * @return simple list of JPanels
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

    @Override
    public List<JPanel> create(Map<String, List<Article>> articles) {
        return articles.entrySet().stream().map((entry) -> createFeedGroupPanel(controller, entry.getKey(), entry.getValue())).toList();
    }

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
