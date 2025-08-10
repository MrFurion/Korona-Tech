package by.trubeckij.services.impl;

import by.trubeckij.enums.ArgsParameters;
import by.trubeckij.models.Department;
import by.trubeckij.services.StatisticsService;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

import static by.trubeckij.constants.ServicesConstants.DEPARTMENT_MIN_MAX_MID;
import static by.trubeckij.constants.ServicesConstants.FORMAT;
import static java.lang.System.out;

@Slf4j
public class StatisticsServiceImpl implements StatisticsService {


    public void generate(List<Department> departments, String output, String path) throws IOException {
        StringBuilder stats = new StringBuilder(DEPARTMENT_MIN_MAX_MID);
        departments.stream()
                .sorted(Comparator.comparing(d -> d.getManager().getDepartment()))
                .forEach(department -> {
                    double min = department.getMinSalary();
                    double max = department.getMaxSalary();
                    double avg = department.getAverageSalary();
                    stats.append(String.format(FORMAT,
                            department.getManager().getDepartment(),
                            min, max, avg));
                });

        if (ArgsParameters.FILE.getArgsParameter().equals(output)) {
            Files.createDirectories(Paths.get(path).getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
                writer.write(stats.toString());
            }
        } else {
            out.println(stats);
        }
    }
}
