package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import java.awt.*;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

/**
 * FeedPanel.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-12
 * GUI section to show list of articles and their associated controls
 */

public class FeedPanel {

    private final JPanel panel = new JPanel();
    private final JScrollPane scrollPane;
    private final AppController controller;

    private ArticleListFormat listFormat;

    public FeedPanel(AppController controller) {
        this.controller = controller;
        this.listFormat = new AggregateArticlePanel(controller); // default, TODO settings for this
        scrollPane = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

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
