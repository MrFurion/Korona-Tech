package by.trubeckij.services.impl;

import by.trubeckij.models.Department;
import by.trubeckij.services.StatisticsService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

public class StatisticsServiceImpl implements StatisticsService {


    public void generate(List<Department> departments, String output, String path) throws IOException {
        StringBuilder stats = new StringBuilder("department,min,max,mid\n");
        departments.stream()
                .sorted()
                .forEach(department -> {
                    double min = department.getMinSalary();
                    double max = department.getMaxSalary();
                    double avg = department.getAverageSalary();
                    stats.append(String.format("%s,%.2f,%.2f,%.2f\n",
                            department.getManager().getDepartment(),
                            min, max, avg));
                });

        if ("file".equals(output)) {
            Files.createDirectories(Paths.get(path).getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(stats.toString());
            }
        } else {
            System.out.println(stats);
        }
    }
}
