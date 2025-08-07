package by.trubeckij.util;

import by.trubeckij.enums.ArgsParameters;

import java.util.HashMap;
import java.util.Map;

import static by.trubeckij.constants.ParseArgsConstants.ARGUMENT_ARRAY_CANNOT_BE_NULL;
import static by.trubeckij.constants.ParseArgsConstants.EMPTY_VALUE;
import static by.trubeckij.constants.ParseArgsConstants.INVALID_LONG_ARGUMENT;
import static by.trubeckij.constants.ParseArgsConstants.INVALID_SHORT_ARGUMENT;
import static by.trubeckij.constants.ParseArgsConstants.KEY_VALUE_SEPARATOR;
import static by.trubeckij.constants.ParseArgsConstants.LONG_FLAG_PREFIX;
import static by.trubeckij.constants.ParseArgsConstants.SHORT_FLAG_PREFIX;
import static by.trubeckij.constants.ParseArgsConstants.SHORT_KEY_OUTPUT;
import static by.trubeckij.constants.ParseArgsConstants.SHORT_KEY_SORT;

public class ParseArgs {

    private static final int MAX_PARTS_TO_SPLIT_FOR_LONG = 2;
    private static final int SINGLE_PART_INDEX = 1;
    private static final int LIMIT_FOR_LONG = 2;
    private static final int MIN_PARTS_LENGTH = 0;
    private static final int FIRST_PART_INDEX = 0;
    private static final int LIMIT_FOR_SHORT = 2;
    private static final int MAX_PARTS_TO_SPLIT_FOR_SHORT = 1;

    private ParseArgs() {
    }

    public static Map<String, String> parseArgs(String[] args) {
        if (args == null) {
            throw new IllegalArgumentException(ARGUMENT_ARRAY_CANNOT_BE_NULL);
        }

        Map<String, String> params = new HashMap<>();
        for (String arg : args) {
            if (arg == null || arg.trim().isEmpty()) {
                continue;
            }

            if (arg.startsWith(LONG_FLAG_PREFIX)) {
                parseLongArg(arg, params);
            } else if (arg.startsWith(SHORT_FLAG_PREFIX)) {
                parseShortArg(arg, params);
            }
        }
        return params;
    }

    private static void parseLongArg(String arg, Map<String, String> params) {
        String[] parts = arg.substring(MAX_PARTS_TO_SPLIT_FOR_LONG).split(KEY_VALUE_SEPARATOR, LIMIT_FOR_LONG);
        if (parts.length == MIN_PARTS_LENGTH || parts[FIRST_PART_INDEX].isEmpty()) {
            throw new IllegalArgumentException(INVALID_LONG_ARGUMENT + arg);
        }
        params.put(parts[FIRST_PART_INDEX], parts.length > SINGLE_PART_INDEX ? parts[SINGLE_PART_INDEX] : EMPTY_VALUE);
    }

    private static void parseShortArg(String arg, Map<String, String> params) {
        String[] parts = arg.substring(MAX_PARTS_TO_SPLIT_FOR_SHORT).split(KEY_VALUE_SEPARATOR, LIMIT_FOR_SHORT);
        if (parts.length == MIN_PARTS_LENGTH || parts[FIRST_PART_INDEX].isEmpty()) {
            throw new IllegalArgumentException(INVALID_SHORT_ARGUMENT + arg);
        }
        String key = switch (parts[FIRST_PART_INDEX]) {
            case SHORT_KEY_SORT -> ArgsParameters.SORT.getArgsParameter();
            case SHORT_KEY_OUTPUT -> ArgsParameters.OUTPUT.getArgsParameter();
            default -> parts[FIRST_PART_INDEX];
        };
        params.put(key, parts.length > SINGLE_PART_INDEX ? parts[SINGLE_PART_INDEX] : EMPTY_VALUE);
    }
}
