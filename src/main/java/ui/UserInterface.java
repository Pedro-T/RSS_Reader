package ui;

import controller.AppController;
import feedmodel.Article;
import feedmodel.FeedManager;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.util.Map;

/**
 * UserInterface.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-18
 * Core UI, main frame for application and menus
 */

public class UserInterface {

    private static final String FRAME_TITLE = "RSS Reader";
    private final JPanel mainPanel = createBorderPanel();
    private final JFrame mainFrame = createFrame();
    private final AppController appController;
    private final FeedDisplay feedDisplay;
    private final FeedManagerUI feedManager;


    public UserInterface(AppController appController) {
        this.appController = appController;
        feedDisplay = new FeedDisplay(appController);
        feedManager = new FeedManagerUI(mainFrame, appController);
        EventQueue.invokeLater(() -> {
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setContentPane(mainPanel);
            mainPanel.add(createMenu(), BorderLayout.NORTH);
            mainPanel.add(feedDisplay.getPane(), BorderLayout.CENTER);
            mainPanel.add(createRefreshButton(), BorderLayout.SOUTH);
        });
    }

    public void show() {
        mainFrame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle(FRAME_TITLE);
        frame.setPreferredSize(new Dimension(500, 800));
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
        EventQueue.invokeLater(() -> {
            JMenu menu = new JMenu("Menu");
            menuBar.add(menu);
            JMenuItem addFeedMenuItem = new JMenuItem("Manage Feeds");
            addFeedMenuItem.addActionListener(event -> feedManager.show());
            menu.add(addFeedMenuItem);
            JMenuItem settingsMenuItem = new JMenuItem("Settings");
            settingsMenuItem.addActionListener(event -> {
                SettingsUI sui = new SettingsUI(appController);
                sui.show();
            });
            menu.add(settingsMenuItem);
            JMenuItem aboutMenuItem = new JMenuItem("About");
            aboutMenuItem.addActionListener(event -> showAbout());
            menu.add(aboutMenuItem);
            menuBar.add(new DebugMenu().create());
        });
        return menuBar;
    }

    public void setAggregateDisplayMode(boolean aggregate) {
        feedDisplay.setListFormat(aggregate ? new AggregateArticlePanel(appController) : new ArticleByFeedPanel(appController));
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }

    public void updateArticleList(Map<String, List<Article>> articles) {
        feedDisplay.update(articles);
        mainFrame.setVisible(true);
    }

    private JButton createRefreshButton() {
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> appController.refreshAll());
        return refreshButton;
    }

    public void showAbout() {
        String aboutText = "<html><h2>RSS Reader Project</h2><h3>Pedro Teixeira</h3><p>This application uses free icon images from Vecteeze<br />https://www.vecteezy.com/free-vector/</p></html>";
        JOptionPane.showMessageDialog(mainFrame, aboutText);
    }
}
