package ua.com.foxminded.university.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Reader {
    private static final String ERROR_TO_LIST = "The content converting of the "
            + "file to a list of strings is a failure."; 
    
    public List<String> toList(String fileName) throws ServicesException.ReadFail {
        URL fileURL = this.getClass().getClassLoader().getResource(fileName);
        String filePath = new File(fileURL.getFile()).getPath();
        
        try (Stream<String> data = Files.lines(Paths.get(filePath))) {
            return data.collect(Collectors.toList());
        } catch (InvalidPathException | IOException e) {
            throw new ServicesException.ReadFail(ERROR_TO_LIST, e);
        }
    }
}
