package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import java.util.List;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * UserInterface.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-14
 * Core UI, main frame for application and menus
 */

public class UserInterface {

    private static final String FRAME_TITLE = "RSS Reader";
    private final JPanel mainPanel = createBorderPanel();
    private final JFrame mainFrame = createFrame();

    private final AppController appController;
    private final FeedPanel feedPane;
    private final FeedManagerUI feedManager;


    public UserInterface(AppController appController) {
        this.appController = appController;
        mainFrame.setContentPane(mainPanel);
        mainPanel.add(createMenu(), BorderLayout.NORTH);
        feedPane = new FeedPanel(appController);
        feedManager = new FeedManagerUI(mainFrame, appController);
        mainPanel.add(feedPane.getPane(), BorderLayout.CENTER);
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
        return menuBar;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }

    public void updateArticleList(List<Article> articles) {
        feedPane.update(articles);
        mainFrame.setVisible(true);
    }
}
