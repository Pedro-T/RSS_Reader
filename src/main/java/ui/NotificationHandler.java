package ui;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * NotificationHandler.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Assignment note: Observer pattern
 * Simple class to send desktop notifications. Usage of Observer pattern as this implements NotificationObserver and
 * subscribes to notifications from a NotificationSubject (only FeedManager is a NotificationSubject). Note that in this
 * implementation, AppController subscribes the handler (within start())
 */

public class NotificationHandler implements NotificationObserver {


    private final TrayIcon trayIcon;
    private boolean enabled = true;

    public NotificationHandler() {
        SystemTray sysTray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/images/rss_logo.png")), "RSS Reader");
        trayIcon.setImageAutoSize(true);
        try {
            sysTray.add(trayIcon);
        } catch (AWTException e) {
            // If we fail to set up, just disable notifications log an error and proceed
            enabled = false;
            Logger logger = Logger.getLogger("app");
            logger.log(Level.WARNING, "Error creating system tray icon, notifications disabled");
        }
    }

    @Override
    public void update(String s) {
        if (enabled) {
            trayIcon.displayMessage("RSS Reader", s, MessageType.INFO);
        }
    }
}
