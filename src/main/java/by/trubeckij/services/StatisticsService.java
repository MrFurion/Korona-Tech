package by.trubeckij.services;

import by.trubeckij.models.Department;

import java.io.IOException;
import java.util.List;

public interface StatisticsService {
    void generate(List<Department> departments, String output, String path) throws IOException;
}
