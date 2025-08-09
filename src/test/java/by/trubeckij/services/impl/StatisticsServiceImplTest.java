package by.trubeckij.services.impl;

import by.trubeckij.models.Department;
import by.trubeckij.models.Employee;
import by.trubeckij.models.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {
    private StatisticsServiceImpl statisticsService;

    @BeforeEach
    void setUp() {
        statisticsService = new StatisticsServiceImpl();
    }

    @Test
    void generate_shouldWriteToFile_whenOutputIsFile(@TempDir Path tempDir) throws IOException {
        // given
        String output = "file";
        Path filePath = tempDir.resolve("stats.csv");
        List<Department> departments = List.of(
                createDepartment("HR", 1000, 2000, 1500),
                createDepartment("IT", 3000, 5000, 4000)
        );

        // when
        statisticsService.generate(departments, output, filePath.toString());

        // then
        String content = Files.readString(filePath);
        assertThat(content).startsWith("department,min,max,mid");
        assertThat(content).contains("HR,1000,00,2000,00,1500,00");
        assertThat(content).contains("IT,3000,00,5000,00,4000,00");
    }


    private Department createDepartment(String deptName, double min, double max, double avg) {
        Manager manager = new Manager();
        Department dept = new Department(manager);
        try {
            Field mDept = Manager.class.getDeclaredField("department");
            mDept.setAccessible(true);
            mDept.set(manager, deptName);

            dept.getEmployees().add(createEmployee(min));
            dept.getEmployees().add(createEmployee(avg));
            dept.getEmployees().add(createEmployee(max));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dept;
    }

    private Employee createEmployee(double salary) {
        Employee employee = new Employee();
        try {
            Field salaryField = Employee.class.getDeclaredField("salary");
            salaryField.setAccessible(true);
            salaryField.set(employee, salary);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return employee;
    }
}