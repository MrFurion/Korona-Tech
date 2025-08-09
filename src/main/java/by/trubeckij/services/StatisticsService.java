package by.trubeckij.services;

import by.trubeckij.models.Department;

import java.io.IOException;
import java.util.List;

/**
 * Service interface for generating statistics.
 */
public interface StatisticsService {
    /**
     * Generates statistics for departments.
     *
     * @param departments list of departments
     * @param output output mode (console or file)
     * @param path file path for output
     * @throws IOException if I/O error occurs
     */
    void generate(List<Department> departments, String output, String path) throws IOException;
}
