package ua.com.foxminded.sql_jdbc_school.services;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class Generator {
    private static final String HYPHEN = "-";
    
    public List<StudentDTO> generateStudents(List<String> firstNames, 
                                              List<String> lastNames) {
        return Stream.generate(() -> new StudentDTO(
                firstNames.get(new Random().nextInt(firstNames.size())), 
                lastNames.get(new Random().nextInt(lastNames.size()))))
            .limit(200).collect(Collectors.toList());
    }
    
    public List<String> generateGroups() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return Stream.generate(() -> new StringBuilder()
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(HYPHEN)
                .append(new Random().nextInt(9))
                .append(new Random().nextInt(9))
                .toString())
            .limit(10).collect(Collectors.toList());
    }
}
