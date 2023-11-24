package ui;

import controller.AppSettings;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.List;

public class DebugMenu {

    public JMenu create() {
        JMenu menu = new JMenu("Debug");
        JMenuItem clearUIDs = new JMenuItem("Clear Read List");

        clearUIDs.addActionListener(event -> {
            try {
                Field f = AppSettings.class.getDeclaredField("readUIDs");
                f.setAccessible(true);
                AppSettings settings = AppSettings.getInstance();
                ((List<String>) f.get(settings)).clear();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        });
        menu.add(clearUIDs);

        return menu;
    }
}
