package by.trubeckij.services.impl;

import by.trubeckij.enums.ArgsParameters;
import by.trubeckij.repositories.ProcessorRepository;
import by.trubeckij.repositories.impl.ProcessorRepositoryImpl;
import by.trubeckij.services.FileServices;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import static by.trubeckij.util.ParseArgs.parseArgs;
import static by.trubeckij.util.Validator.validateArgsParam;


@Slf4j
public class FileServicesImpl implements FileServices {
    private static final String ERROR = "Error: ";
    private static final String IO_ERROR = "IO Error: ";
    private static final int EXIT_CODE_INVALID_ARGS = 1;
    private static final int EXIT_CODE_IO_ERROR = 1;
    public static final String JOIN_MESSAGE = "{}";
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
            System.exit(EXIT_CODE_INVALID_ARGS);
        } catch (IOException e) {
            log.error(IO_ERROR + JOIN_MESSAGE, e.getMessage());
            System.exit(EXIT_CODE_IO_ERROR);
        }
    }
}
