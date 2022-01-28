package ua.com.foxminded.university.generator;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ua.com.foxminded.university.dto.GroupData;
import ua.com.foxminded.university.dto.StudentData;

public class Generator {
    
    public List<StudentData> generateStudents(List<String> firstNames, 
                                              List<String> lastNames) {
        return Stream.generate(() -> new StudentData(firstNames.get(new Random().nextInt(firstNames.size())), 
                                                     lastNames.get(new Random().nextInt(lastNames.size()))))
                     .limit(200).collect(Collectors.toList());
    }
    
    public List<GroupData> generateGroups() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int index = new Random().nextInt(25);
        String randomChar = String.valueOf(alphabet.charAt(index));
        String randomChars = randomChar.concat(randomChar);
        int randomNumber = new Random().nextInt(9);
        String randomNumbers = new StringBuilder().append(randomNumber)
                                                  .append(randomNumber).toString();
        return Stream.generate(() -> randomChars.concat("-").concat(randomNumbers))
                     .limit(10).map((name) -> new GroupData(name))
                     .collect(Collectors.toList());
    }
    
    
}
