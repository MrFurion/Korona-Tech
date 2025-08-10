package by.trubeckij.services.impl;

import by.trubeckij.enums.ArgsParameters;
import by.trubeckij.repositories.ProcessorRepository;
import by.trubeckij.repositories.impl.ProcessorRepositoryImpl;
import by.trubeckij.services.FileServices;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import static by.trubeckij.constants.ServicesConstants.CONTINUING_EXECUTION_SKIPPING_PROBLEMATIC_FILES;
import static by.trubeckij.constants.ServicesConstants.ERROR;
import static by.trubeckij.constants.ServicesConstants.IO_ERROR;
import static by.trubeckij.constants.ServicesConstants.JOIN_MESSAGE;
import static by.trubeckij.constants.ServicesConstants.PART_INCORRECT_PARAMETERS;
import static by.trubeckij.util.ParseArgs.parseArgs;
import static by.trubeckij.util.Validator.validateArgsParam;


@Slf4j
public class FileServicesImpl implements FileServices {

    private final ProcessorRepository processorService = new ProcessorRepositoryImpl();

    public void sortedParamsFiles(String[] args) {
        try {
            Map<String, String> params = parseArgs(args);
            validateArgsParam(params);
            processorService.processFiles(
                    params.get(ArgsParameters.SORT.getArgsParameter()),
                    params.get(ArgsParameters.ORDER.getArgsParameter()),
                    params.containsKey(ArgsParameters.STAT.getArgsParameter()),
                    params.getOrDefault(ArgsParameters.OUTPUT.getArgsParameter(), ArgsParameters.CONSOLE.getArgsParameter()),
                    params.get(ArgsParameters.PATH.getArgsParameter())
            );
        } catch (IllegalArgumentException e) {
            log.error(ERROR + JOIN_MESSAGE, e.getMessage());
            log.info(PART_INCORRECT_PARAMETERS);
        } catch (IOException e) {
            log.error(IO_ERROR + JOIN_MESSAGE, e.getMessage());
            log.info(CONTINUING_EXECUTION_SKIPPING_PROBLEMATIC_FILES);
        }
    }
}
