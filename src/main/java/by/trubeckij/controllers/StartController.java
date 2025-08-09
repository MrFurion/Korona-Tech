package by.trubeckij.controllers;

/**
 * Interface for starting file processing with command-line arguments.
 */
public interface StartController {
    /**
     * Processes files based on provided arguments.
     *
     * @param args command-line arguments
     */
    void sortedFiles(String[] args);
}
