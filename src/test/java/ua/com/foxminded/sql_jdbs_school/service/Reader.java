package ua.com.foxminded.sql_jdbs_school.service;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ua.com.foxminded.sql_jdbc_school.model.CourseModel;

public class Reader {
    
    
    @Test
    void createWithoutId(@TempDir Path tempDir) throws IOException {
        Path tempFile = tempDir.resolve(TEST_FILE_NAME);
        Stream.generate(() -> COURSE_NAME)
               .limit(GROUP_QUANTITY)
               .forEach((name) -> {
            try {
                Files.write(tempFile, name.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        when(readerMock.read(anyString())).thenReturn();
        List<CourseModel> courses = courseService.createWithoutId();
        

}
