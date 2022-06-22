package ua.com.foxminded.sql_jdbc_school.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ua.com.foxminded.sql_jdbc_school.dto.StudentDto;

public class Generator {
    
	private static final String HYPHEN = "-";
    private static final int MIN_STUDENTS = 10; 
    private static final int MAX_STUDENTS = 30; 
    private static final int SINGLE_DIDGIT_OF_MAX_VALUE = 9; 
    private static final int MAX_NUMBER_OF_GROUPS = 10; 
    private static final int AMPLITUDE = 20; 
    private static final double ONE_HALF = 0.5; 
    private static final double DOUBLE_PROBABILITY_VALUE = 4.0;
    private static final int INT_PROBABILITY_VALUE = 3;
    private static final int ONE_STUDENT = 1;
    private static final int ZERO_STUDENTS = 0;
    private static final double DOUBLE_AMPL_PROBABILITY = 10.0;
    private static final int INT_AMPL_PROBABILITY = 10;
    private static final int STUDENTS_WITHOUT_GROUP = 21;
    private static final int CROURSE_AMPLITUDE = 3;
    private static final int MIN_NUMBER_OF_CROURSES = 1;
    private static final int ONE_STEP = 1;
    
    public List<List<Integer>> getStudentCourseIndexRelation(int numberOfStudents, int numberOfCourses) {
        Map<Integer, Integer> numberOfCoursesOfEachStudent = new HashMap<>();
        
        for (int i = 0; i < numberOfStudents; i++) {
            int numberOfCoursesPerStudent = MIN_NUMBER_OF_CROURSES + new Random().nextInt(CROURSE_AMPLITUDE);
            numberOfCoursesOfEachStudent.put(i, numberOfCoursesPerStudent);
        }
        
        List<List<Integer>> studentCourseIndexRelation = new ArrayList<>();
        
        for (int i = 0; i < numberOfStudents; i++) {
            List<Integer> cache = new ArrayList<>();
            
            for (int j = 0; j < numberOfCoursesOfEachStudent.get(i); j++) {
                int courseIndex = new Random().nextInt(numberOfCourses);
                List<Integer> studentAndCourseIndex = new ArrayList<>();
                
                if (!cache.contains(courseIndex)) {
                    cache.add(courseIndex);
                    studentAndCourseIndex.add(i);
                    studentAndCourseIndex.add(courseIndex);
                    studentCourseIndexRelation.add(studentAndCourseIndex);
                } else {
                    j -= ONE_STEP;
                }
            }
        }
        return studentCourseIndexRelation;
    }
    
    public List<Integer> getNumberOfStudentsInGroup(int studentsNumber, int groupsNumber) {
        List<Integer> result = new ArrayList<>();
        int noGroupStudents = new Random().nextInt(STUDENTS_WITHOUT_GROUP); 
        int remainder = studentsNumber - noGroupStudents;

        for (int i = 0; i < groupsNumber; i++) {
            double probability = new Random().nextInt(INT_PROBABILITY_VALUE) / DOUBLE_PROBABILITY_VALUE;
            int zeroProbability = (int) ((probability + ONE_HALF) - (probability - ONE_HALF));
            double amplitudeProbability = new Random().nextInt(INT_AMPL_PROBABILITY) / DOUBLE_AMPL_PROBABILITY;
            int studentsInGroup = (int) (zeroProbability * (MIN_STUDENTS + AMPLITUDE * amplitudeProbability));

            if (studentsInGroup <= remainder && remainder != 0 || studentsInGroup == 0) {
                result.add(studentsInGroup);
                remainder -= studentsInGroup;
            } else if (studentsInGroup > remainder && remainder >= MIN_STUDENTS) {
                result.add(remainder);
                remainder = 0;
            } else if (remainder != 0) {
                for (int j = 0; j < result.size(); j++) {
                    if (result.get(j) < MAX_STUDENTS
                        && result.get(j) >= MIN_STUDENTS 
                        && remainder != 0) {
                        result.set(j, result.get(j) + ONE_STUDENT);
                        remainder -= ONE_STUDENT;

                        if (j == (result.size() - ONE_STEP) && remainder > 0) {
                            j = 0;
                        } else if (remainder == 0) {
                            result.add(ZERO_STUDENTS);
                            break;
                        }
                    } 
                }
            } else {
                result.add(ZERO_STUDENTS);
            }
        }
        return result;
    }

    public List<StudentDto> getStudentData(List<String> firstNames, List<String> lastNames) {
        return Stream.generate(() -> new StudentDto(firstNames.get(new Random().nextInt(firstNames.size())),
                                                    lastNames.get(new Random().nextInt(lastNames.size()))))
                     .limit(200).collect(Collectors.toList());
    }

    public List<String> getGroupName() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        return Stream.generate(() -> new StringBuilder()
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(alphabet.charAt(new Random().nextInt(alphabet.length())))
                .append(HYPHEN)
                .append(new Random().nextInt(SINGLE_DIDGIT_OF_MAX_VALUE))
                .append(new Random().nextInt(SINGLE_DIDGIT_OF_MAX_VALUE))
                .toString())
            .limit(MAX_NUMBER_OF_GROUPS).collect(Collectors.toList());
    }
}
