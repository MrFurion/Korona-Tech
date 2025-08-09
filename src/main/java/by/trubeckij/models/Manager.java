package by.trubeckij.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Manager {
    public static final String FORMAT = "Manager,%d,%s,%.0f,%s";
    private int id;
    private String name;
    private double salary;
    private String department;

    @Override
    public String toString() {
        return String.format(FORMAT, id, name, salary, department);
    }
}
