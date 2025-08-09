package by.trubeckij.services.impl;

import by.trubeckij.repositories.ProcessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FileServicesImplTest {

    @Mock
    private ProcessorRepository processorRepository;

    @InjectMocks
    private FileServicesImpl fileServices;
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        fileServices = new FileServicesImpl();

        Field field = FileServicesImpl.class.getDeclaredField("processorService");
        field.setAccessible(true);
        field.set(fileServices, processorRepository);
    }

    @Test
    void sortedParamsFiles_shouldCallProcessorServiceWithCorrectArgs() throws Exception {
        String[] args = {
                "--sort=name",
                "--order=asc",
                "--stat",
                "--output=file",
                "--path=/tmp/data"
        };

        fileServices.sortedParamsFiles(args);

        verify(processorRepository).processFiles("name", "asc", true, "file", "/tmp/data");
    }
}