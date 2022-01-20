package ua.com.foxminded.university.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reader {
    
    public List<String> toList(String filePath) throws IOException, 
                                                       InvalidPathException {

        try (Stream<String> data = Files.lines(Paths.get(filePath))) {
            return data.collect(Collectors.toList());
        } 
    }
}
