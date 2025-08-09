package by.trubeckij.repositories.impl;

import by.trubeckij.models.Employee;
import by.trubeckij.models.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ProcessorRepositoryImplTest {
    private ProcessorRepositoryImpl processorRepositoryImpl;

    @BeforeEach
    void setUp() {
        processorRepositoryImpl = new ProcessorRepositoryImpl();
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Manager> getManagers() throws Exception {
        Field field = ProcessorRepositoryImpl.class.getDeclaredField("managers");
        field.setAccessible(true);
        return (Map<Integer, Manager>) field.get(processorRepositoryImpl);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, Employee> getEmployees() throws Exception {
        Field field = ProcessorRepositoryImpl.class.getDeclaredField("employees");
        field.setAccessible(true);
        return (Map<Integer, Employee>) field.get(processorRepositoryImpl);
    }

    @SuppressWarnings("unchecked")
    private List<String> getErrors() throws Exception {
        Field field = ProcessorRepositoryImpl.class.getDeclaredField("errors");
        field.setAccessible(true);
        return (List<String>) field.get(processorRepositoryImpl);
    }

    private void callProcessLine(String line) throws Exception {
        Method method = ProcessorRepositoryImpl.class.getDeclaredMethod("processLine", String.class);
        method.setAccessible(true);
        method.invoke(processorRepositoryImpl, line);
    }

    @Test
    void processLineWhenManagerValid() throws Exception {
        // Given
        String line = "Manager,1,Jane Smith,5000,HR";

        // When
        callProcessLine(line);

        // Then
        Map<Integer, Manager> managers = getManagers();
        assertEquals(1, managers.size());
        Manager manager = managers.get(1);
        assertNotNull(manager);
        assertEquals(1, manager.getId());
        assertEquals("Jane Smith", manager.getName());
        assertEquals(5000.0, manager.getSalary());
        assertEquals("HR", manager.getDepartment());
    }

    @Test
    void processLineWhenEmployeeValid() throws Exception {
        // Given
        callProcessLine("Manager,1,Jane Smith,5000,HR");
        String line = "Employee,2,John Doe,3000,1";

        // When
        callProcessLine(line);

        // Then
        Map<Integer, Employee> employees = getEmployees();
        assertEquals(1, employees.size());
        Employee employee = employees.get(2);
        assertNotNull(employee);
        assertEquals(2, employee.getId());
        assertEquals("John Doe", employee.getName());
        assertEquals(3000.0, employee.getSalary());
        assertEquals(1, employee.getManagerId());
    }

    @Test
    void processLineWhenInvalidPartsCount() throws Exception {
        // Given
        String line = "Manager,1,Jane Smith,5000";

        // When
        callProcessLine(line);

        // Then
        assertTrue(getErrors().contains(line));
        assertEquals(0, getManagers().size());
    }

    @Test
    void processLineWhenInvalidSalary() throws Exception {
        // Given
        String line = "Manager,1,Jane Smith,abc,HR";

        // When
        callProcessLine(line);

        // Then
        assertTrue(getErrors().contains(line));
        assertEquals(0, getManagers().size());
    }

    @Test
    void processLineWhenUnknownType() throws Exception {
        // Given
        String line = "Boss,1,Jane Smith,5000,HR";

        // When
        callProcessLine(line);

        // Then
        assertTrue(getErrors().contains(line));
        assertEquals(0, getManagers().size());
        assertEquals(0, getEmployees().size());
    }
}