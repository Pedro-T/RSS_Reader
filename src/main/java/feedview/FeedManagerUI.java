package feedview;

import feedcontroller.AppController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FeedManagerUI {

    private final AppController controller;
    private final JDialog feedManagerDialog;
    private final JPanel feedManagerPanel = new JPanel();
    private JTable feedTable;
    private final JButton deleteButton = new JButton("Remove Selected");
    private String[][] tableData;

    private static final String[] COLUMN_NAMES = new String[]{"Name", "URL"};

    public FeedManagerUI(JFrame parent, AppController controller) {
        feedManagerDialog = new JDialog(parent, "Manage Feeds", true);
        this.controller = controller;
        tableData = controller.getFeedNamesAndURLs();

        feedManagerPanel.setPreferredSize(new Dimension(400, 300));
        feedManagerPanel.setLayout(new BoxLayout(feedManagerPanel, BoxLayout.PAGE_AXIS));

        feedManagerPanel.add(buildFeedListPanel());
        feedManagerPanel.add(addFeedPanel());

        feedManagerDialog.setContentPane(feedManagerPanel);
        feedManagerDialog.pack();
    }

    public void show() {
        feedManagerDialog.setVisible(true);
    }

    private JPanel buildFeedListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        feedTable = new JTable(new DefaultTableModel(COLUMN_NAMES, 5));
        feedTable.setPreferredSize(new Dimension(200, 200));
        feedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedTable.getSelectionModel().setSelectionInterval(0, 0);
        feedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = feedTable.getTableHeader();
        header.setReorderingAllowed(false);
        panel.add(new JScrollPane(feedTable));
        deleteButton.addActionListener(event -> {
            controller.removeFeedByURL(tableData[feedTable.getSelectionModel().getSelectedIndices()[0]][1]);
            updateFeedTable();
        });
        panel.add(deleteButton);
        return panel;
    }

    private void updateFeedTable() {
        tableData = controller.getFeedNamesAndURLs();
        if (tableData.length != 0) {
            feedTable.setModel(new DefaultTableModel(tableData, COLUMN_NAMES));
            EventQueue.invokeLater(feedManagerPanel::repaint);
        }
        deleteButton.setEnabled(tableData.length != 0);
    }

    private JPanel addFeedPanel() {
        JPanel panel = new JPanel();
        String defaultText = "enter feed url";
        JTextField urlField = new JTextField(defaultText, 40);
        urlField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (urlField.getText().equals(defaultText)) {
                    urlField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (urlField.getText().isEmpty()) {
                    urlField.setText(defaultText);
                }
            }
        });
        JButton addFeedButton = new JButton("Add");
        addFeedButton.addActionListener(event -> {
            controller.subscribeToFeed(urlField.getText());
            updateFeedTable();
            urlField.setText(defaultText);
        });
        panel.add(urlField);
        panel.add(addFeedButton);
        return panel;
    }

    public JPanel getFeedManagerPanel() {
        return this.feedManagerPanel;
    }


}
