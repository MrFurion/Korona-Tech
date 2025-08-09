package by.trubeckij.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
}
