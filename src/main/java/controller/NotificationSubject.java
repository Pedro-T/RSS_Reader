package controller;

import ui.NotificationObserver;

/**
 * NotificationSubject.java
 * CPSC6119
 * Assignments 5-7
 * @author Pedro Teixeira
 * @version 2023-11-26
 * Interface for subject
 */

public interface NotificationSubject {

    void addObserver(NotificationObserver o);

    void removeObserver(NotificationObserver o);

    void notifyUpdates(String s);
}
