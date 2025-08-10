package by.trubeckij.controllers.impl;

import by.trubeckij.controllers.StartController;
import by.trubeckij.services.FileServices;
import by.trubeckij.services.impl.FileServicesImpl;
import lombok.extern.slf4j.Slf4j;

import static by.trubeckij.constants.ControllerConstants.FILE_PROCESSING_COMPLETED_SUCCESSFULLY_WITH_ARGUMENTS;
import static by.trubeckij.constants.ControllerConstants.STARTING_FILE_PROCESSING_WITH_ARGUMENTS;
import static by.trubeckij.constants.ControllerConstants.UNEXPECTED_ERROR_DURING_FILE_PROCESSING;
import static by.trubeckij.constants.ControllerConstants.UNEXPECTED_ERROR_DURING_FILE_PROCESSING_WITH_ARGUMENTS;


@Slf4j
public class StartControllerImpl implements StartController {

    private final FileServices fileServices = new FileServicesImpl();

    public void sortedFiles(String[] args) {
        log.info(STARTING_FILE_PROCESSING_WITH_ARGUMENTS, (Object) args);
        try {
            fileServices.sortedParamsFiles(args);
            log.info(FILE_PROCESSING_COMPLETED_SUCCESSFULLY_WITH_ARGUMENTS, (Object) args);
        } catch (Exception e) {
            log.error(UNEXPECTED_ERROR_DURING_FILE_PROCESSING_WITH_ARGUMENTS, args, e.getMessage(), e);
            throw new IllegalArgumentException(UNEXPECTED_ERROR_DURING_FILE_PROCESSING, e);
        }
    }
}
