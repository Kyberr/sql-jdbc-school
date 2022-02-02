package ua.com.foxminded.sql_jdbc_school.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ua.com.foxminded.sql_jdbc_school.services.dto.StudentDTO;

public class Generator {
    private static final String HYPHEN = "-";

    public List<Integer> generateStudentNumber(int studentsNumber, int groupsNumber) {
        List<Integer> result = new ArrayList<>();
        int remainder = studentsNumber;

        for (int i = 0; i < groupsNumber; i++) {
            double probability = new Random().nextInt(9) / 10.0;
            int zeroProbability = (int) ((probability + 0.5) - (probability - 0.5));
            double studentsNumberProbability = (new Random().nextInt(10)) / 10.0;
            int studentsOfGroup = (int) (zeroProbability * (studentsNumber / groupsNumber + 10 * studentsNumberProbability));

            if (studentsOfGroup <= remainder || studentsOfGroup == 0 && remainder != 0) {
                result.add(studentsOfGroup);
                remainder -= studentsOfGroup;
            } else if (studentsOfGroup > remainder && remainder > (studentsNumber / groupsNumber)) {
                result.add(remainder);
                remainder = 0;
            } else if (remainder != 0) {
                for (int j = 0; j < result.size(); j++) {
                    if (result.get(j) < (1.5 * studentsNumber / groupsNumber)
                        && result.get(j) >= (studentsNumber / groupsNumber) 
                        && remainder != 0) {
                        result.set(j, result.get(j) + 1);
                        remainder -= 1;

                        if (j == (result.size() - 1) && remainder > 0) {
                            j = 0;
                        } else if (remainder == 0) {
                            result.add(0);
                            break;
                        }
                    } 
                }
            } else {
                result.add(0);
            }
        }
        return result;
    }

    public List<StudentDTO> generateStudents(List<String> firstNames, List<String> lastNames) {
        return Stream.generate(() -> new StudentDTO(firstNames.get(new Random().nextInt(firstNames.size())),
                                                    lastNames.get(new Random().nextInt(lastNames.size()))))
                     .limit(200).collect(Collectors.toList());
    }

    public List<String> generateGroups() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return Stream.generate(() -> new StringBuilder()
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(alphabet.charAt(new Random().nextInt(alphabet.length()))).append(HYPHEN)
                .append(new Random().nextInt(9)).append(new Random().nextInt(9)).toString())
            .limit(10).collect(Collectors.toList());
    }
}
