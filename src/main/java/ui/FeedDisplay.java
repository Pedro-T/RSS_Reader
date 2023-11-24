package ui;

import controller.AppController;
import feedmodel.Article;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * FeedDisplay.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-12
 * GUI section to show list of articles and their associated controls
 */

public class FeedDisplay {

    private final JPanel panel = new JPanel();
    private final JScrollPane scrollPane;
    private final AppController controller;

    private ArticleListFormat listFormat; // Assignment Note - strategy pattern usage

    public FeedDisplay(AppController controller) {
        this.controller = controller;
        this.listFormat = new ArticleByFeedPanel(controller); // just the default
        scrollPane = new JScrollPane(panel);
        EventQueue.invokeLater(() -> {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        });
    }

    public void setListFormat(ArticleListFormat listFormat) {
        this.listFormat = listFormat;
    }

    /**
     * Update the item list with articles from the current list in memory
     * @param articles map of articles relayed by controller from feed manager object in String, Article arrangement
     */
    public void update(Map<String, List<Article>> articles)  {
        EventQueue.invokeLater(() -> {
            panel.removeAll();
            if (articles.size() != 0) {
                List<JPanel> articlePanels = listFormat.create(articles);
                for (JPanel ap : articlePanels) {
                    panel.add(ap);
                }
            } else {
                panel.add(new JLabel("No articles to show!", JLabel.CENTER));
            }
        });
    }

    public JScrollPane getPane() {
        return scrollPane;
    }
}
