package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * ArticleDisplayPanel.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-17
 * Provides a UI panel to display the article details and action buttons
 */

public class ArticleDisplayPanel {

    private final JPanel panel = new JPanel();

    public ArticleDisplayPanel(AppController controller, Article article) {
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
                throw new RuntimeException(e);
            }
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
