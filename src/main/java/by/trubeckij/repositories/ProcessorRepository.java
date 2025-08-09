package by.trubeckij.repositories;

import java.io.IOException;

/**
 * Repository interface for processing files and generating statistics.
 */
public interface ProcessorRepository {
    /**
     * Processes files with specified parameters and generates statistics if required.
     *
     * @param sortType sort type (name or salary)
     * @param order order (asc or desc)
     * @param generateStats true to generate statistics
     * @param output output mode (console or file)
     * @param path file path for output
     * @throws IOException if I/O error occurs
     */
    void processFiles(String sortType, String order, boolean generateStats, String output, String path) throws IOException;
}
