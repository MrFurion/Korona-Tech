package by.trubeckij.repositories;

import java.io.IOException;

public interface ProcessorRepository {
    void processFiles(String sortType, String order, boolean generateStats, String output, String path) throws IOException;
}
