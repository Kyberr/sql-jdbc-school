package ua.com.foxminded.sql_jdbs_school.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.sql_jdbc_school.service.Reader;
import ua.com.foxminded.sql_jdbc_school.service.ServiceException;

@ExtendWith(MockitoExtension.class)
class ReaderTest {
    private static final int LINE_QUANTITY = 10;
    private static final String LINE_IN_FILE = "some string";
    private static final String NAME_OF_TEST_FILE = "test.txt";
    
       
    
    @Test
    void read_ReadingFile_CorrectNumberOfLinesInFile(@TempDir Path tempDir) throws IOException, 
                                                                                   ServiceException {
        Path tempFile = tempDir.resolve(NAME_OF_TEST_FILE);
        List<String> courseNameList = Stream.generate(() -> LINE_IN_FILE)
                                            .limit(LINE_QUANTITY)
                                            .collect(Collectors.toList());
        Files.write(tempFile, courseNameList);
        Reader reader = new Reader();
        assertEquals(LINE_QUANTITY, reader.read(tempFile).size());
    }
}
