package ui;

import controller.AppController;
import feedmodel.Article;
import feedmodel.FeedManager;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.*;
import java.util.Map;

/**
 * UserInterface.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Core UI, main frame for application and menus
 * MVC - View (along with associated UI classes)
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
        EventQueue.invokeLater(() -> mainFrame.setVisible(true));
    }

    /**
     * Create the frame and add a listener to call the save to cache functionality on closing
     * WindowListener adapted from https://stackoverflow.com/questions/16372241/run-function-on-jframe-close
     * @return the primary JFrame for the application
     */
    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                appController.exitSaveCache();
            }
        });
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

    /**
     * Create a simple menu bar to access the feed manager and settings windows, an about popup, and the debug clear UIDs option
     * @return the created JMenuBar object
     */
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

    /**
     * toggles the display mode to aggregate or separated by feed
     * @param aggregate
     */
    public void setAggregateDisplayMode(boolean aggregate) {
        feedDisplay.setListFormat(aggregate ? new AggregateArticlePanel(appController) : new ArticleByFeedPanel(appController));
    }

    /**
     * Create a generic message popup to show various errors to the user
     * @param message string to populate in body of dialog box
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this.mainFrame, message);
    }

    /**
     * pass the article list to the feed display panel for updating
     * @param articles map of feed names and articles
     */
    public void updateArticleList(Map<String, List<Article>> articles) {
        feedDisplay.update(articles);
        mainFrame.setVisible(true);
    }

    private JButton createRefreshButton() {
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> appController.refreshAll());
        return refreshButton;
    }

    /**
     * Display a basic 'about' popup, including attribution for free icons used
     */
    public void showAbout() {
        String aboutText = "<html><h2>RSS Reader Project</h2><h3>Pedro Teixeira</h3><p>This application uses free icon images from Vecteeze<br />https://www.vecteezy.com/free-vector/</p></html>";
        JOptionPane.showMessageDialog(mainFrame, aboutText);
    }
}
