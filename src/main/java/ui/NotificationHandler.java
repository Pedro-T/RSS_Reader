package ui;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationHandler implements NotificationObserver {


    private final Logger logger = Logger.getLogger("app");
    private final TrayIcon trayIcon;
    private boolean enabled = true;

    public NotificationHandler() {
        SystemTray sysTray = SystemTray.getSystemTray();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/images/rss_logo.png")), "RSS Reader");
        trayIcon.setImageAutoSize(true);
        try {
            sysTray.add(trayIcon);
        } catch (AWTException e) {
            enabled = false;
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
