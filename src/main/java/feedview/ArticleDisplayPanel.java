package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * ArticleDisplayPanel.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-14
 * UI panel to display the article details and action buttons
 */

public class ArticleDisplayPanel {

    private final JPanel panel = new JPanel();

    public ArticleDisplayPanel(AppController controller, Article article) {
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 100));
        panel.add(new JLabel(article.getFeedName()), BorderLayout.NORTH);
        panel.add(new JLabel(article.getTitle()), BorderLayout.CENTER);
        panel.add(new JLabel(article.getSummary()), BorderLayout.SOUTH);
        panel.add(getButtonStack(controller, article), BorderLayout.WEST);

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

    private JPanel getButtonStack(AppController controller, Article article) {
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
