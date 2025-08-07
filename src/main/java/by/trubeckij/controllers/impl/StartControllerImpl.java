package by.trubeckij.controllers.impl;

import by.trubeckij.controllers.StartController;
import by.trubeckij.services.FileServices;
import by.trubeckij.services.impl.FileServicesImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartControllerImpl implements StartController {

    private final FileServices fileServices = new FileServicesImpl();

    public void sortedFiles(String[] args) {
        fileServices.sortedParamsFiles(args);
    }
}
