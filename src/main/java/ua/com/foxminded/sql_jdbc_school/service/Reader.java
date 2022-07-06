package ua.com.foxminded.sql_jdbc_school.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reader {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ERROR_TO_LIST = "The content converting of the "
            + "file to a list of strings is failed.";

    public List<String> read(Path filePath) throws ServiceException {
        
        try (Stream<String> data = Files.lines(filePath)) {
            return data.collect(Collectors.toList());
        } catch (InvalidPathException | IOException e) {
            LOGGER.error(ERROR_TO_LIST, e);
            throw new ServiceException(ERROR_TO_LIST, e);
        }
    }
}
