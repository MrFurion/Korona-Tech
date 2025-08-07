package by.trubeckij.util;

import java.util.HashMap;
import java.util.Map;

public class ParseArgs {
    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> params = new HashMap<>();
        for (String arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.substring(2).split("=", 2);
                params.put(parts[0], parts.length > 1 ? parts[1] : "");
            } else if (arg.startsWith("-")) {
                String[] parts = arg.substring(1).split("=", 2);
                String key = parts[0].equals("s") ? "sort" : parts[0].equals("o") ? "output" : parts[0];
                params.put(key, parts.length > 1 ? parts[1] : "");
            }
        }
        return params;
    }
}
