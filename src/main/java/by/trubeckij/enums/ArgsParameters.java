package by.trubeckij.enums;

public enum ArgsParameters {
    CONSOLE("console"),
    FILE("file"),
    PATH("path"),
    OUTPUT("output"),
    DESC("desc"),
    ASC("asc"),
    SALARY("salary"),
    NAME("name"),
    STAT("stat"),
    ORDER("order"),
    SORT("sort");
    private final String argsParameter;

    ArgsParameters(String argsParameter) {
        this.argsParameter = argsParameter;
    }

    public String getArgsParameter() {
        return argsParameter;
    }
}
