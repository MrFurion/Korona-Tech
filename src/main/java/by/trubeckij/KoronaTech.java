package by.trubeckij;

import by.trubeckij.controllers.StartController;
import by.trubeckij.controllers.impl.StartControllerImpl;

public class KoronaTech {
    public static void main(String[] args) {
        StartController startController = new StartControllerImpl();
        startController.sortedFiles(args);
    }
}