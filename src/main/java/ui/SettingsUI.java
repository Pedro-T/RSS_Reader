package ui;

import controller.AppController;
import controller.AppSettings;

import javax.swing.*;
import java.awt.*;

public class SettingsUI {

    private final JFrame settingsFrame = new JFrame("Settings");

    private final JCheckBox aggregateFeedsCheckBox = new JCheckBox("Aggregate Feeds");
    private final JCheckBox autoRefreshCheckBox = new JCheckBox("Auto-Refresh");

    private final JPanel settingsPanel = new JPanel();
    private JSlider updateIntervalSlider;
    private JSlider articlesPerFeedSlider;
    private final AppSettings settings = AppSettings.getInstance();

    private final AppController controller;

    public SettingsUI(AppController controller) {
        this.controller = controller;
        EventQueue.invokeLater(() -> {
            settingsFrame.setResizable(false);
            settingsFrame.setPreferredSize(new Dimension(400, 300));
            settingsFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            settingsFrame.setContentPane(getSettingsPanel());
            settingsFrame.pack();
        });
    }

    public void show() {
        EventQueue.invokeLater(() -> settingsFrame.setVisible(true));
    }

    private JPanel getSettingsPanel() {
        EventQueue.invokeLater(() -> {
            settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));

            if (settings.isAutoRefresh()) {
                autoRefreshCheckBox.doClick();
            }
            if (settings.isAggregateFeeds()) {
                aggregateFeedsCheckBox.doClick();
            }
            settingsPanel.add(autoRefreshCheckBox);
            settingsPanel.add(aggregateFeedsCheckBox);

            updateIntervalSlider = new JSlider(JSlider.HORIZONTAL, 1, 30, settings.getUpdateInterval());
            updateIntervalSlider.setMajorTickSpacing(5);
            updateIntervalSlider.setPaintTicks(true);
            updateIntervalSlider.setPaintLabels(true);
            settingsPanel.add(new JLabel("Update Interval (minutes)"));
            settingsPanel.add(updateIntervalSlider);

            articlesPerFeedSlider = new JSlider(JSlider.HORIZONTAL, 1 , 10, settings.getArticlesPerFeed());
            articlesPerFeedSlider.setMajorTickSpacing(2);
            articlesPerFeedSlider.setPaintTicks(true);
            articlesPerFeedSlider.setPaintLabels(true);
            settingsPanel.add(new JLabel("Articles per Feed"));
            settingsPanel.add(articlesPerFeedSlider);
            settingsPanel.add(getSaveButton());
            settingsPanel.add(getCloseButton());
        });
        return settingsPanel;
    }

    private JButton getSaveButton() {
        JButton savebutton = new JButton("Save");
        savebutton.addActionListener(event -> controller.setSettings(
                this.updateIntervalSlider.getValue(),
                this.articlesPerFeedSlider.getValue(),
                this.autoRefreshCheckBox.isSelected(),
                this.aggregateFeedsCheckBox.isSelected()));
        return savebutton;
    }

    private JButton getCloseButton() {
        JButton CloseButton = new JButton("Close");
        CloseButton.addActionListener(event -> settingsFrame.dispose());
        return CloseButton;
    }

    private void promptRestart() {
        JOptionPane.showMessageDialog(settingsFrame, "Please restart the app for\nsettings to take effect.");
    }
}
