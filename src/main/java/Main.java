import feedcontroller.AppController;

import javax.swing.*;

/**
 * Main.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-14
 * Entry point
 */

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        AppController controller = new AppController();
        controller.start();
    }
}
