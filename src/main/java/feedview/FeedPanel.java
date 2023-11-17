package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import java.awt.*;
import java.util.List;

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

    public FeedPanel(AppController controller) {
        this.controller = controller;
        scrollPane = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void update(List<Article> articles)  {
        panel.removeAll();
        if (articles.size() != 0) {
            articles.forEach(article -> panel.add(new ArticleDisplayPanel(controller, article).getPanel()));
        } else {
            panel.add(new JLabel("No articles to show!", JLabel.CENTER));
        }

        // avoids issue where panel isn't refreshed until frame is resized. Based on https://stackoverflow.com/questions/11069807/jpanel-doesnt-update-until-resize-jframe
        EventQueue.invokeLater(panel::repaint);
    }

    public JScrollPane getPane() {
        return scrollPane;
    }
}
