package by.trubeckij.repositories.impl;

import by.trubeckij.models.Department;
import by.trubeckij.models.Employee;
import by.trubeckij.models.Manager;
import by.trubeckij.repositories.ProcessorRepository;
import by.trubeckij.services.StatisticsService;
import by.trubeckij.services.impl.StatisticsServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.trubeckij.repositories.constants.ProcessorConstants.COMMA;
import static by.trubeckij.repositories.constants.ProcessorConstants.DOT;
import static by.trubeckij.repositories.constants.ProcessorConstants.DUPLICATE_EMPLOYEE_ID;
import static by.trubeckij.repositories.constants.ProcessorConstants.EMPLOYEE;
import static by.trubeckij.repositories.constants.ProcessorConstants.EMPLOYEE_WITH_INVALID_SALARY_SKIPPED;
import static by.trubeckij.repositories.constants.ProcessorConstants.EMPLOYEE_WITH_NO_MANAGER;
import static by.trubeckij.repositories.constants.ProcessorConstants.FAILED_TO_WRITE_DEPARTMENT_FILE;
import static by.trubeckij.repositories.constants.ProcessorConstants.FAILED_TO_WRITE_ERROR_LOG;
import static by.trubeckij.repositories.constants.ProcessorConstants.FILES_WITH_SB_NOT_FOUND;
import static by.trubeckij.repositories.constants.ProcessorConstants.INVALID_LINE_FORMAT;
import static by.trubeckij.repositories.constants.ProcessorConstants.INVALID_SALARY_FORMAT;
import static by.trubeckij.repositories.constants.ProcessorConstants.INVALID_SALARY_FORMAT_RETURNING_1;
import static by.trubeckij.repositories.constants.ProcessorConstants.MANAGER;
import static by.trubeckij.repositories.constants.ProcessorConstants.NUMBER_FORMAT_ERROR_IN_LINE;
import static by.trubeckij.repositories.constants.ProcessorConstants.OUTPUT;
import static by.trubeckij.repositories.constants.ProcessorConstants.OUTPUT_ERROR_LOG;
import static by.trubeckij.repositories.constants.ProcessorConstants.OUTPUT_WITH_SLASH;
import static by.trubeckij.repositories.constants.ProcessorConstants.SB;
import static by.trubeckij.repositories.constants.ProcessorConstants.UNKNOWN_RECORD_TYPE;

@Slf4j
public class ProcessorRepositoryImpl implements ProcessorRepository {

    private static final int ID_INDEX = 1;
    private static final int NAME_INDEX = 2;
    private static final int SALARY_INDEX = 3;
    private static final int LAST_FIELD_INDEX = 4;
    private static final int EXPECTED_PARTS_COUNT = 5;
    private static final int RECORD_TYPE_INDEX = 0;
    private static final int MINIMUM_SALARY = 0;
    private static final double INVALID_SALARY_VALUE = -1.0;
    private final Map<Integer, Manager> managers = new HashMap<>();
    private final Map<Integer, Employee> employees = new HashMap<>();
    private final List<String> errors = new ArrayList<>();
    private final StatisticsService statisticsService = new StatisticsServiceImpl();

    public void processFiles(String sortType, String order, boolean generateStats, String output, String path) throws IOException {
        readFiles();
        writeDepartments(sortType, order);
        if (generateStats) {
            statisticsService.generate(getDepartments(), output, path);
        }
        writeErrors();
    }

    private void readFiles() throws IOException {
        File dir = new File(DOT);
        File[] files = dir.listFiles((d, name) -> name.endsWith(SB));
        if (files == null) {
            log.error(FILES_WITH_SB_NOT_FOUND);
            return;
        }

        for (File file : files) {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                processLine(line.trim());
            }
        }
    }

    private void processLine(String line) {
        if (line.isEmpty()) return;
        String[] parts = line.split(COMMA);
        if (parts.length != EXPECTED_PARTS_COUNT) {
            errors.add(line);
            log.error(INVALID_LINE_FORMAT, line);
            return;
        }

        try {
            int id = Integer.parseInt(parts[ID_INDEX].trim());
            String name = parts[NAME_INDEX].trim();
            String salaryStr = parts[SALARY_INDEX].trim();
            String lastField = parts[LAST_FIELD_INDEX].trim();

            if (MANAGER.equals(parts[RECORD_TYPE_INDEX])) {
                double salary = parseSalary(salaryStr);
                if (salary <= MINIMUM_SALARY || managers.containsKey(id)) {
                    errors.add(line);
                    log.error(INVALID_LINE_FORMAT, line);
                    return;
                }
                managers.put(id, new Manager(id, name, salary, lastField));
            } else if (EMPLOYEE.equals(parts[RECORD_TYPE_INDEX])) {
                double salary = parseSalary(salaryStr);
                int managerId = Integer.parseInt(lastField);
                if (salary <= MINIMUM_SALARY) {
                    errors.add(line);
                    log.error(INVALID_SALARY_FORMAT, line);
                    return;
                }
                if (employees.containsKey(id)) {
                    errors.add(line);
                    log.error(DUPLICATE_EMPLOYEE_ID, line);
                    return;
                }
                employees.put(id, new Employee(id, name, salary, managerId));
            } else {
                errors.add(line);
                log.error(UNKNOWN_RECORD_TYPE, line);
            }
        } catch (NumberFormatException e) {
            errors.add(line);
            log.error(NUMBER_FORMAT_ERROR_IN_LINE, line);
        }
    }

    private double parseSalary(String salaryStr) {
        try {
            return Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            log.warn(INVALID_SALARY_FORMAT_RETURNING_1, salaryStr);
            return INVALID_SALARY_VALUE;
        }
    }

    private List<Department> getDepartments() {
        Map<String, Department> departments = new HashMap<>();
        for (Manager manager : managers.values()) {
            departments.put(manager.getDepartment(), new Department(manager));
        }
        for (Employee employee : employees.values()) {
            Manager manager = managers.get(employee.getManagerId());
            if (manager != null) {
                departments.computeIfAbsent(manager.getDepartment(), k -> new Department(manager))
                        .addEmployee(employee);
            } else {
                errors.add(employee.toString());
                log.error(EMPLOYEE_WITH_NO_MANAGER, employee);
            }
        }
        return new ArrayList<>(departments.values());
    }

    private void writeDepartments(String sortType, String order) throws IOException {
        Files.createDirectories(Paths.get(OUTPUT));
        for (Department department : getDepartments()) {
            department.sortEmployees(sortType, order);
            String fileName = OUTPUT_WITH_SLASH + department.getManager().getDepartment() + SB;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(department.getManager().toString());
                writer.newLine();
                for (Employee employee : department.getEmployees()) {
                    if (employee.getSalary() > MINIMUM_SALARY) {
                        writer.write(employee.toString());
                        writer.newLine();
                    } else {
                        errors.add(employee.toString());
                        log.warn(EMPLOYEE_WITH_INVALID_SALARY_SKIPPED, employee);
                    }
                }
            } catch (IOException e) {
                log.error(FAILED_TO_WRITE_DEPARTMENT_FILE, fileName, e.getMessage());
                throw e;
            }
        }
    }

    private void writeErrors() throws IOException {
        if (!errors.isEmpty()) {
            Files.createDirectories(Paths.get(OUTPUT));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_ERROR_LOG))) {
                for (String error : errors) {
                    writer.write(error);
                    writer.newLine();
                }
            } catch (IOException e) {
                log.error(FAILED_TO_WRITE_ERROR_LOG, e.getMessage());
                throw e;
            }
        }
    }
}
