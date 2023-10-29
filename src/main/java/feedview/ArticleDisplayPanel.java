package feedview;

import feedcontroller.FeedController;
import feedmodel.Article;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class ArticleDisplayPanel {

    private final JPanel panel = new JPanel();
    private final FeedController controller;

    public ArticleDisplayPanel(FeedController controller, Article article) {
        this.controller = controller;
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 100));
        panel.add(new JLabel(article.getFeedName()), BorderLayout.NORTH);
        panel.add(new JLabel(article.getTitle()), BorderLayout.CENTER);
        panel.add(new JLabel(article.getSummary()), BorderLayout.SOUTH);
        panel.add(getButtonStack(article), BorderLayout.WEST);

        if (article.hasImageURL()) {
            Image image;
            try {
                image = ImageIO.read(article.getImageURL());
                panel.add(new JLabel(new ImageIcon(image.getScaledInstance(80, 80, Image.SCALE_SMOOTH))), BorderLayout.EAST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private JPanel getButtonStack(Article article) {
        JPanel buttonStackPanel = new JPanel();
        buttonStackPanel.setMinimumSize(new Dimension(30, 100));
        buttonStackPanel.setLayout(new BoxLayout(buttonStackPanel, BoxLayout.Y_AXIS));

        JButton openFullButton = new JButton();
        openFullButton.setMinimumSize(new Dimension(30, 30));
        openFullButton.setToolTipText("Open in Browser");
        openFullButton.addActionListener(event -> controller.openInBrowser(article.getURL()));

        JButton removeButton = new JButton();
        URL iconURL = this.getClass().getResource("/images/X.png");
        if (iconURL != null) {
            try {
                Image image = ImageIO.read(iconURL);
                removeButton.setIcon(new ImageIcon(image));
            } catch (IOException e) {
                e.printStackTrace();
                removeButton.setText("X");
            }
        } else {
            removeButton.setText("X");
        }
        removeButton.setMinimumSize(new Dimension(30, 30));
        removeButton.setToolTipText("Mark as Read");
        removeButton.addActionListener(event -> controller.requestRemoveArticle(article));
        buttonStackPanel.add(openFullButton);
        buttonStackPanel.add(removeButton);
        return buttonStackPanel;
    }

    public JPanel getPanel() {
        return panel;
    }

}
