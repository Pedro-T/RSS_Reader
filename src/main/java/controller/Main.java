package controller;

import javax.swing.*;

/**
 * controller.Main.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Entry point
 */

public class Main {

    public static void main(String[] args) {
        // attempt to set Swing look and feel to something reasonable for your OS
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        AppController controller = new AppController();
        controller.start();
    }
}
