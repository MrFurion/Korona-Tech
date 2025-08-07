package by.trubeckij.repositories.constants;

public class ProcessorConstants {
    private ProcessorConstants() {
    }
    public static final String MANAGER = "Manager";
    public static final String EMPLOYEE = "Employee";
    public static final String OUTPUT = "output";
    public static final String SB = ".sb";
    public static final String OUTPUT_ERROR_LOG = "output/error.log";
    public static final String OUTPUT_WITH_SLASH = "output/";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String FILES_WITH_SB_NOT_FOUND = "Files with .sb not found";
    public static final String INVALID_LINE_FORMAT = "Invalid line format: {}";
    public static final String DUPLICATE_EMPLOYEE_ID = "Duplicate employee ID: {}";
    public static final String UNKNOWN_RECORD_TYPE = "Unknown record type: {}";
    public static final String NUMBER_FORMAT_ERROR_IN_LINE = "Number format error in line: {}";
    public static final String INVALID_SALARY_FORMAT_RETURNING_1 = "Invalid salary format, returning -1: {}";
    public static final String EMPLOYEE_WITH_NO_MANAGER = "Employee with no manager: {}";
    public static final String EMPLOYEE_WITH_INVALID_SALARY_SKIPPED = "Employee with invalid salary skipped: {}";
    public static final String FAILED_TO_WRITE_DEPARTMENT_FILE = "Failed to write department file {}: {}";
    public static final String FAILED_TO_WRITE_ERROR_LOG = "Failed to write error log: {}";
    public static final String INVALID_SALARY_FORMAT = "Invalid salary format: {}";
}
