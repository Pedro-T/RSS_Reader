package controller;

import ui.NotificationObserver;

public interface NotificationSubject {

    void addObserver(NotificationObserver o);

    void removeObserver(NotificationObserver o);

    void notifyUpdates(String s);
}
