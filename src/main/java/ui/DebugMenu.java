package ui;

import controller.AppSettings;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * DebugMenu.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Provides a very basic debug menu that currently has a single function - clear the UID
 * list to more easily test refreshing
 */

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
