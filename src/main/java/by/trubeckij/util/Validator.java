package by.trubeckij.util;

import by.trubeckij.enums.ArgsParameters;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

import static by.trubeckij.constants.ValidatorConstants.INVALID_ORDER_TYPE;
import static by.trubeckij.constants.ValidatorConstants.INVALID_OUTPUT_TYPE;
import static by.trubeckij.constants.ValidatorConstants.INVALID_SORT_TYPE;
import static by.trubeckij.constants.ValidatorConstants.ORDER_SPECIFIED_WITHOUT_SORT_PARAMETER;
import static by.trubeckij.constants.ValidatorConstants.OUTPUT_FILE_SPECIFIED_WITHOUT_PATH;
import static by.trubeckij.constants.ValidatorConstants.OUTPUT_OR_PATH_SPECIFIED_WITHOUT_STAT;
import static by.trubeckij.constants.ValidatorConstants.PATH_SPECIFIED_WITHOUT_OUTPUT_FILE;
import static by.trubeckij.constants.ValidatorConstants.UNKNOWN_PARAMETER;

@Slf4j
public class Validator {

    private Validator() {
    }

    public static void validateArgsParam(Map<String, String> params) {
        checkOrderWithoutSort(params);
        checkSortType(params);
        checkOrderType(params);
        checkOutputType(params);
        checkFileOutputWithPath(params);
        checkPathWithoutFileOutput(params);
        checkStatRequirement(params);
        checkUnknownParameters(params);
    }

    private static void checkOrderWithoutSort(Map<String, String> params) {
        if (params.containsKey(ArgsParameters.ORDER.getArgsParameter()) &&
            !params.containsKey(ArgsParameters.SORT.getArgsParameter())) {
            throw new IllegalArgumentException(ORDER_SPECIFIED_WITHOUT_SORT_PARAMETER);
        }
    }

    private static void checkSortType(Map<String, String> params) {
        if (params.containsKey(ArgsParameters.SORT.getArgsParameter()) &&
            !Set.of(ArgsParameters.NAME.getArgsParameter(), ArgsParameters.SALARY.getArgsParameter())
                    .contains(params.get(ArgsParameters.SORT.getArgsParameter()))) {
            throw new IllegalArgumentException(INVALID_SORT_TYPE + params.get(ArgsParameters.SORT.getArgsParameter()));
        }
    }

    private static void checkOrderType(Map<String, String> params) {
        String orderParam = params.get(ArgsParameters.ORDER.getArgsParameter());
        if (params.containsKey(orderParam) && !Set.of(ArgsParameters.ASC.getArgsParameter(), ArgsParameters.DESC.getArgsParameter())
                .contains(params.get(orderParam))) {
            throw new IllegalArgumentException(INVALID_ORDER_TYPE + params.get(ArgsParameters.ORDER.getArgsParameter()));
        }
    }

    private static void checkOutputType(Map<String, String> params) {
        if (params.containsKey(ArgsParameters.OUTPUT.getArgsParameter()) &&
            !Set.of(ArgsParameters.CONSOLE.getArgsParameter(), ArgsParameters.FILE.getArgsParameter())
                    .contains(params.get(ArgsParameters.OUTPUT.getArgsParameter()))) {
            throw new IllegalArgumentException(INVALID_OUTPUT_TYPE + params.get(ArgsParameters.OUTPUT.getArgsParameter()));
        }
    }

    private static void checkFileOutputWithPath(Map<String, String> params) {
        if (ArgsParameters.FILE.getArgsParameter().equals(params.get(ArgsParameters.OUTPUT.getArgsParameter())) &&
            !params.containsKey(ArgsParameters.PATH.getArgsParameter())) {
            throw new IllegalArgumentException(OUTPUT_FILE_SPECIFIED_WITHOUT_PATH);
        }
    }

    private static void checkPathWithoutFileOutput(Map<String, String> params) {
        if (params.containsKey(ArgsParameters.PATH.getArgsParameter()) &&
            !ArgsParameters.FILE.getArgsParameter().equals(params.get(ArgsParameters.OUTPUT.getArgsParameter()))) {
            throw new IllegalArgumentException(PATH_SPECIFIED_WITHOUT_OUTPUT_FILE);
        }
    }

    private static void checkStatRequirement(Map<String, String> params) {
        if (!params.containsKey(ArgsParameters.STAT.getArgsParameter()) &&
            (params.containsKey(ArgsParameters.OUTPUT.getArgsParameter()) ||
             params.containsKey(ArgsParameters.PATH.getArgsParameter()))) {
            throw new IllegalArgumentException(OUTPUT_OR_PATH_SPECIFIED_WITHOUT_STAT);
        }
    }

    private static void checkUnknownParameters(Map<String, String> params) {
        Set<String> validParams = Set.of(
                ArgsParameters.SORT.getArgsParameter(),
                ArgsParameters.ORDER.getArgsParameter(),
                ArgsParameters.STAT.getArgsParameter(),
                ArgsParameters.OUTPUT.getArgsParameter(),
                ArgsParameters.PATH.getArgsParameter()
        );
        for (String key : params.keySet()) {
            if (!validParams.contains(key)) {
                throw new IllegalArgumentException(UNKNOWN_PARAMETER + key);
            }
        }
    }
}
