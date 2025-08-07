package by.trubeckij.models;

import by.trubeckij.enums.ArgsParameters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data

public class Department {
    private static final double DEFAULT_ZERO_SALARY = 0.0;
    private static final int MINIMUM_SALARY_THRESHOLD = 0;

    private Manager manager;
    private List<Employee> employees = new ArrayList<>();

    public Department(Manager manager) {
        this.manager = manager;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void sortEmployees(String sortType, String order) {
        if (sortType == null) return;
        Comparator<Employee> comparator;
        if (ArgsParameters.NAME.getArgsParameter().equals(sortType)) {
            comparator = Comparator.comparing(Employee::getName);
        } else {
            comparator = Comparator.comparingDouble(Employee::getSalary);
        }
        if (ArgsParameters.DESC.getArgsParameter().equals(order)) {
            comparator = comparator.reversed();
        }
        employees.sort(comparator);
    }

    public double getMinSalary() {
        return employees.stream()
                .filter(e -> e.getSalary() > MINIMUM_SALARY_THRESHOLD)
                .mapToDouble(Employee::getSalary)
                .min()
                .orElse(DEFAULT_ZERO_SALARY);
    }

    public double getMaxSalary() {
        return employees.stream()
                .filter(e -> e.getSalary() > MINIMUM_SALARY_THRESHOLD)
                .mapToDouble(Employee::getSalary)
                .max()
                .orElse(DEFAULT_ZERO_SALARY);
    }

    public double getAverageSalary() {
        return employees.stream()
                .filter(e -> e.getSalary() > MINIMUM_SALARY_THRESHOLD)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(DEFAULT_ZERO_SALARY);
    }
}
