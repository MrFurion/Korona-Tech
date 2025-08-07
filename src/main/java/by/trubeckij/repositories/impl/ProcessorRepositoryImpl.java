package by.trubeckij.repositories.impl;

import by.trubeckij.models.Department;
import by.trubeckij.models.Employee;
import by.trubeckij.models.Manager;
import by.trubeckij.repositories.ProcessorRepository;
import by.trubeckij.services.StatisticsService;
import by.trubeckij.services.impl.StatisticsServiceImpl;

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

public class ProcessorRepositoryImpl implements ProcessorRepository {
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
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".sb"));
        if (files == null) return;

        for (File file : files) {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                processLine(line.trim());
            }
        }
    }

    private void processLine(String line) {
        if (line.isEmpty()) return;
        String[] parts = line.split(",");
        if (parts.length != 5) {
            errors.add(line);
            return;
        }

        try {
            int id = Integer.parseInt(parts[1].trim());
            String name = parts[2].trim();
            String salaryStr = parts[3].trim();
            String lastField = parts[4].trim();

            if ("Manager".equals(parts[0])) {
                double salary = parseSalary(salaryStr);
                if (salary <= 0 || managers.containsKey(id)) {
                    errors.add(line);
                    return;
                }
                managers.put(id, new Manager(id, name, salary, lastField));
            } else if ("Employee".equals(parts[0])) {
                double salary = parseSalary(salaryStr);
                int managerId = Integer.parseInt(lastField);
                if (employees.containsKey(id)) {
                    errors.add(line);
                    return;
                }
                employees.put(id, new Employee(id, name, salary, managerId));
            } else {
                errors.add(line);
            }
        } catch (NumberFormatException e) {
            errors.add(line);
        }
    }

    private double parseSalary(String salaryStr) {
        try {
            return Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            return -1;
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
            }
        }
        return new ArrayList<>(departments.values());
    }

    private void writeDepartments(String sortType, String order) throws IOException {
        Files.createDirectories(Paths.get("output"));
        for (Department department : getDepartments()) {
            department.sortEmployees(sortType, order);
            String fileName = "output/" + department.getManager().getDepartment() + ".sb";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(department.getManager().toString());
                writer.newLine();
                for (Employee employee : department.getEmployees()) {
                    if (employee.getSalary() > 0) {
                        writer.write(employee.toString());
                        writer.newLine();
                    } else {
                        errors.add(employee.toString());
                    }
                }
            }
        }
    }

    private void writeErrors() throws IOException {
        if (!errors.isEmpty()) {
            Files.createDirectories(Paths.get("output"));
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output/error.log"))) {
                for (String error : errors) {
                    writer.write(error);
                    writer.newLine();
                }
            }
        }
    }
}
