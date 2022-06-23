package ua.com.foxminded.sql_jdbc_school.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reader {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_TO_LIST = "The content converting of the "
            + "file to a list of strings is failed.";

    public List<String> read(String fileName) throws ServiceException {
        URL fileURL = this.getClass().getClassLoader().getResource(fileName);
        String filePath = new File(fileURL.getFile()).getPath();

        try (Stream<String> data = Files.lines(Paths.get(filePath))) {
            return data.collect(Collectors.toList());
        } catch (InvalidPathException | IOException e) {
            LOGGER.error(ERROR_TO_LIST, e);
            throw new ServiceException(ERROR_TO_LIST, e);
        }
    }
}
