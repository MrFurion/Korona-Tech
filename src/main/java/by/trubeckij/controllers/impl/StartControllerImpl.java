package by.trubeckij.controllers.impl;

import by.trubeckij.controllers.StartController;
import by.trubeckij.services.FileServices;
import by.trubeckij.services.impl.FileServicesImpl;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StartControllerImpl implements StartController {

    public static final String STARTING_FILE_PROCESSING_WITH_ARGUMENTS = "Starting file processing with arguments: {}";
    public static final String FILE_PROCESSING_COMPLETED_SUCCESSFULLY_WITH_ARGUMENTS = "File processing completed successfully with arguments: {}";
    public static final String UNEXPECTED_ERROR_DURING_FILE_PROCESSING_WITH_ARGUMENTS = "Unexpected error during file processing with arguments {}: {}";
    public static final String UNEXPECTED_ERROR_DURING_FILE_PROCESSING = "Unexpected error during file processing";
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
