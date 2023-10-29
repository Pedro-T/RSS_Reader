package feedview;

import feedcontroller.FeedController;
import feedmodel.Article;
import java.util.List;

import javax.swing.*;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class FeedPanel {

    private final JPanel panel = new JPanel();
    private final JScrollPane scrollPane;
    private final FeedController controller;

    public FeedPanel(FeedController controller) {
        this.controller = controller;
        scrollPane = new JScrollPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void update(List<Article> articles)  {
        panel.removeAll();
        articles.forEach(article -> panel.add(new ArticleDisplayPanel(controller, article).getPanel()));
    }

    public JScrollPane getPane() {
        return scrollPane;
    }
}
