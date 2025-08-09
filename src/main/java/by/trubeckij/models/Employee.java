package by.trubeckij.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    public static final String FORMAT = "Employee,%d,%s,%.0f,%d";
    private int id;
    private String name;
    private double salary;
    private int managerId;

    @Override
    public String toString() {
        return String.format(FORMAT, id, name, salary, managerId);
    }
}
