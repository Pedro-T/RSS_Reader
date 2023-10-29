package feedview;

import feedcontroller.FeedController;
import feedmodel.Article;
import feedmodel.Feed;

import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class UserInterface {

    private static final String FRAME_TITLE = "RSS Reader";
    private final JPanel mainPanel = createBorderPanel();
    private final JFrame mainFrame = createFrame();

    private final FeedController feedController;
    private final FeedPanel feedPane;


    public UserInterface(FeedController feedController) {
        this.feedController = feedController;
        mainFrame.setContentPane(mainPanel);
        mainPanel.add(createMenu(), BorderLayout.NORTH);
        feedPane = new FeedPanel(feedController);
        mainPanel.add(feedPane.getPane(), BorderLayout.CENTER);
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(FRAME_TITLE);
        frame.setPreferredSize(new Dimension(1024, 768));
        frame.setResizable(true);
        frame.pack();
        return frame;
    }

    private JPanel createBorderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem addFeedMenuItem = new JMenuItem("Add Feed");
        addFeedMenuItem.addActionListener(event -> feedController.addFeed(promptURL()));
        menu.add(addFeedMenuItem);
        return menuBar;
    }

    private String promptURL() {
        return JOptionPane.showInputDialog(this.mainFrame, "Enter feed URL");
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }

    public void updateArticleList(List<Article> articles) {
        feedPane.update(articles);
        mainFrame.pack();
    }
}
