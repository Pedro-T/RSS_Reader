package ui;

import controller.AppController;
import feedmodel.Article;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ArticleDisplayPanel.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-30
 * Provides a UI panel to display the article details and action buttons
 */

public class ArticleDisplayPanel {

    private static final ImageIcon openInBrowserIcon = loadImage("/images/browser_icon.png");
    private static final ImageIcon markReadIcon = loadImage("/images/remove_icon.png");
    private final JPanel panel = new JPanel();

    public ArticleDisplayPanel(AppController controller, Article article) {

        EventQueue.invokeLater(() -> {
            panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            panel.setLayout(new BorderLayout());
            panel.setPreferredSize(new Dimension(200, 100));
            panel.add(new JLabel(article.getFeedName()), BorderLayout.SOUTH);
            panel.add(new JLabel(article.getTitle()), BorderLayout.NORTH);
            panel.add(getSummaryArea(article), BorderLayout.CENTER);
            panel.add(getButtonStack(controller, article), BorderLayout.WEST);
            if (article.hasImageURL()) {
                Image image;
                try {
                    image = ImageIO.read(article.getImageURL());
                    panel.add(new JLabel(new ImageIcon(image.getScaledInstance(80, 80, Image.SCALE_SMOOTH))), BorderLayout.EAST);
                } catch (IOException e) {
                    Logger.getLogger("app").log(Level.WARNING, "Failed to retrieve image");
                }
            }
        });
    }

    /**
     * Load and scale image icons for the two action buttons
     * @param url local path to image file
     * @return scaled ImageIcon instance
     */
    private static ImageIcon loadImage(String url) {
        URL iconURL = ArticleDisplayPanel.class.getResource(url);
        try {
            Image image = ImageIO.read(iconURL);
            return new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create and return a JTextArea for the article summary text. This is mostly for word wrapping capabilities vs a JLabel
     * @param article article for which to generate the summary area
     * @return a JTextAre properly formatted with the summary text
     */
    private JTextArea getSummaryArea(Article article) {
        JTextArea area = new JTextArea();
        area.setText(article.getSummary());
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setBackground(UIManager.getColor("Panel.background"));
        area.setFont(UIManager.getFont("Label.font"));
        return area;
    }

    /**
     * Create and return the per-article control stack. This consists of buttons for three actions
     * Open in Browser, Remove, Save
     * @param controller appcontroller to contact for actions
     * @param article article which these buttons are relevant for
     * @return a panel with the three buttons
     */
    private JPanel getButtonStack(AppController controller, Article article) {
        JPanel buttonStackPanel = new JPanel();
        buttonStackPanel.setMinimumSize(new Dimension(30, 100));
        buttonStackPanel.setLayout(new BoxLayout(buttonStackPanel, BoxLayout.Y_AXIS));
        buttonStackPanel.add(createOpenButton(controller, article));
        buttonStackPanel.add(createMarkReadButton(controller, article));
        return buttonStackPanel;
    }

    private JButton createMarkReadButton(AppController controller, Article article) {
        JButton removeButton = new JButton();
        if (markReadIcon == null) {
            removeButton.setText("X");
        } else {
            removeButton.setIcon(markReadIcon);
        }
        removeButton.setToolTipText("Mark as Read");
        removeButton.addActionListener(event -> controller.requestRemoveArticle(article));
        return removeButton;
    }

    private JButton createOpenButton(AppController controller, Article article) {
        JButton openFullButton = new JButton();
        if (openInBrowserIcon == null) {
            openFullButton.setText("open");
        } else {
            openFullButton.setIcon(openInBrowserIcon);
        }
        openFullButton.setToolTipText("Open in Browser");
        openFullButton.addActionListener(event -> controller.openInBrowser(article.getURL()));
        return openFullButton;
    }

    public JPanel getPanel() {
        return panel;
    }
}
